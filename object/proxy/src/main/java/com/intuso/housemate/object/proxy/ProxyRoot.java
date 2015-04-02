package com.intuso.housemate.object.proxy;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.realclient.HasRealClients;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.object.BaseObject;
import com.intuso.utilities.object.ObjectListener;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <ROOT> the type of the root
 */
public abstract class ProxyRoot<
            REAL_CLIENT extends ProxyRealClient<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>,
            REAL_CLIENTS extends ProxyList<?, REAL_CLIENT, REAL_CLIENTS>,
            ROOT extends ProxyRoot<REAL_CLIENT, REAL_CLIENTS, ROOT>>
        extends ProxyObject<RootData, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?>, ROOT, RootListener<? super ROOT>>
        implements Root<ROOT>, HasRealClients<REAL_CLIENTS>,
            ObjectListener<ProxyObject<?, ?, ?, ?, ?>> {

    public final static String REAL_CLIENTS_ID = "real-clients";

    private final Map<String, Listeners<ObjectLifecycleListener>> objectLifecycleListeners = Maps.newHashMap();

    private final Router.Registration routerRegistration;
    private final ConnectionManager connectionManager;

    /**
     * @param log {@inheritDoc}
     * @param router The router to connect through
     */
    public ProxyRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router) {
        super(log, listenersFactory, new RootData());
        connectionManager = new ConnectionManager(listenersFactory, properties, ClientType.Proxy, this);
        init(null);
        routerRegistration = router.registerReceiver(this);
    }

    @Override
    public ApplicationStatus getApplicationStatus() {
        return connectionManager.getApplicationStatus();
    }

    @Override
    public ApplicationInstanceStatus getApplicationInstanceStatus() {
        return connectionManager.getApplicationInstanceStatus();
    }

    @Override
    public void register(ApplicationDetails applicationDetails) {
        connectionManager.register(applicationDetails);
    }

    @Override
    public void unregister() {
        connectionManager.unregister();
        routerRegistration.unregister();
    }

    @Override
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    public void sendMessage(Message<?> message) {
        // if we're not allowed to send messages, and it's not a registration message, then throw an exception
        if(getApplicationInstanceStatus() != ApplicationInstanceStatus.Allowed
                && !(message.getPath().length == 1 &&
                (message.getType().equals(APPLICATION_REGISTRATION_TYPE)
                        || message.getType().equals(APPLICATION_UNREGISTRATION_TYPE))))
            throw new HousemateRuntimeException("Client application instance is not allowed access to the server");
        routerRegistration.sendMessage(message);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addChildListener(this));
        result.add(connectionManager.addStatusChangeListener(new ConnectionListener() {

            @Override
            public void serverConnectionStatusChanged(ServerConnectionStatus serverConnectionStatus) {
                for (RootListener<? super ROOT> listener : getObjectListeners())
                    listener.serverConnectionStatusChanged(getThis(), serverConnectionStatus);
            }

            @Override
            public void applicationStatusChanged(ApplicationStatus applicationStatus) {
                for (RootListener<? super ROOT> listener : getObjectListeners())
                    listener.applicationStatusChanged(getThis(), applicationStatus);
            }

            @Override
            public void applicationInstanceStatusChanged(ApplicationInstanceStatus applicationInstanceStatus) {
                for (RootListener<? super ROOT> listener : getObjectListeners())
                    listener.applicationInstanceStatusChanged(getThis(), applicationInstanceStatus);
            }

            @Override
            public void newApplicationInstance(String instanceId) {
                for (RootListener<? super ROOT> listener : getObjectListeners())
                    listener.newApplicationInstance(getThis(), instanceId);
            }

            @Override
            public void newServerInstance(String serverId) {
                Set<String> ids = Sets.newHashSet();
                for (HousemateObject<?, ?, ?, ?> child : getChildren()) {
                    child.uninit();
                    ids.add(child.getId());
                }
                for (String id : ids)
                    removeChild(id);
                for (RootListener<? super ROOT> listener : getObjectListeners())
                    listener.newServerInstance(getThis(), serverId);
            }
        }));
        result.add(addMessageListener(SERVER_INSTANCE_ID_TYPE, new Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) throws HousemateException {
                connectionManager.setServerInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(APPLICATION_INSTANCE_ID_TYPE, new Receiver<StringPayload>() {
            @Override
            public void messageReceived(Message<StringPayload> message) throws HousemateException {
                connectionManager.setApplicationInstanceId(message.getPayload().getValue());
            }
        }));
        result.add(addMessageListener(SERVER_CONNECTION_STATUS_TYPE, new Receiver<ServerConnectionStatus>() {
            @Override
            public void messageReceived(Message<ServerConnectionStatus> message) throws HousemateException {
                connectionManager.setServerConnectionStatus(message.getPayload());
            }
        }));
        result.add(addMessageListener(APPLICATION_STATUS_TYPE, new Receiver<ApplicationStatus>() {
            @Override
            public void messageReceived(Message<ApplicationStatus> message) throws HousemateException {
                connectionManager.setApplicationStatus(message.getPayload());
            }
        }));
        result.add(addMessageListener(APPLICATION_INSTANCE_STATUS_TYPE, new Receiver<ApplicationInstanceStatus>() {
            @Override
            public void messageReceived(Message<ApplicationInstanceStatus> message) throws HousemateException {
                connectionManager.setApplicationInstanceStatus(message.getPayload());
            }
        }));
        return result;
    }

    @Override
    public REAL_CLIENTS getRealClients() {
        return (REAL_CLIENTS) getChild(REAL_CLIENTS_ID);
    }

    @Override
    public void childObjectAdded(String childName, ProxyObject<?, ?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void childObjectRemoved(String childName, ProxyObject<?, ?, ?, ?, ?> child) {
        // do nothing
    }

    @Override
    public void ancestorObjectAdded(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        if(ancestor instanceof HousemateObject)
            objectAdded(ancestorPath, (HousemateObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was added
     * @param path the path of the object
     * @param object the object
     */
    private void objectAdded(String path, HousemateObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectCreated(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?> child : object.getChildren())
            objectAdded(path + PATH_SEPARATOR + child.getId(), child);
    }

    @Override
    public void ancestorObjectRemoved(String ancestorPath, BaseObject<?, ?, ?, ?> ancestor) {
        if(ancestor instanceof HousemateObject)
            objectRemoved(ancestorPath, (HousemateObject<?, ?, ?, ?>) ancestor);
    }

    /**
     * Notifies that an object was removed
     * @param path the path of the object
     * @param object the object
     */
    private void objectRemoved(String path, HousemateObject<?, ?, ?, ?> object) {
        if(objectLifecycleListeners.get(path) != null) {
            String splitPath[] = path.split(PATH_SEPARATOR);
            for(ObjectLifecycleListener listener : objectLifecycleListeners.get(path))
                listener.objectRemoved(splitPath, object);
        }
        for(HousemateObject<?, ?, ?, ?> child : object.getChildren())
            objectRemoved(path + PATH_SEPARATOR + child.getId(), child);
    }

    public final ListenerRegistration addObjectLifecycleListener(String[] ancestorPath, ObjectLifecycleListener listener) {
        String path = Joiner.on(PATH_SEPARATOR).join(ancestorPath);
        Listeners<ObjectLifecycleListener> listeners = objectLifecycleListeners.get(path);
        if(listeners == null) {
            listeners = getListenersFactory().create();
            objectLifecycleListeners.put(path, listeners);
        }
        return listeners.addListener(listener);
    }

    public void clearLoadedObjects() {
        sendMessage("clear-loaded", NoPayload.INSTANCE);
        // clone the set so we can edit it while we iterate it
        for(String childName : Sets.newHashSet(getChildNames()))
            removeChild(childName);
    }
}
