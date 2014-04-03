package com.intuso.housemate.object.real;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.root.RootData;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.List;

public class RealRoot
        extends RealObject<RootData, HousemateData<?>, RealObject<?, ? extends HousemateData<?>, ?, ?>, RootListener<? super RealRoot>>
        implements com.intuso.housemate.api.object.root.real.RealRoot<
                RealType<?, ?, ?>,
                RealList<TypeData<?>, RealType<?, ?, ?>>,
                RealDevice,
                RealList<DeviceData, RealDevice>,
                RealRoot> {

    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;
    private final RealList<DeviceData, RealDevice> devices;
    private final Router.Registration routerRegistration;
    private final ConnectionManager connectionManager;

    private boolean resend = false;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     */
    public RealRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router) {
        this(log, listenersFactory, properties,
                router,
                new RealList<TypeData<?>, RealType<?, ?, ?>>(log, listenersFactory, TYPES_ID, TYPES_ID, "Defined types"),
                new RealList<DeviceData, RealDevice>(log, listenersFactory, DEVICES_ID, DEVICES_ID, "Defined devices"));
    }

    @Inject
    public RealRoot(Log log, ListenersFactory listenersFactory, PropertyRepository properties, Router router,
                    RealList<TypeData<?>, RealType<?, ?, ?>> types, RealList<DeviceData, RealDevice> devices) {
        super(log, listenersFactory, new RootData());

        this.types = types;
        this.devices = devices;
        this.connectionManager = new ConnectionManager(listenersFactory, properties, ClientType.Real);

        init(null);

        // need to do this once the connection manager is created and once the object is init'ed so the path is not null
        this.routerRegistration = router.registerReceiver(this);

        addChild(types);
        types.init(this);
        addChild(devices);
        devices.init(this);

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
        connectionManager.register(applicationDetails, routerRegistration);
    }

    @Override
    public void unregister() {
        connectionManager.unregister(routerRegistration);
    }

    @Override
    public ListenerRegistration addObjectLifecycleListener(String[] path, ObjectLifecycleListener listener) {
        throw new HousemateRuntimeException("This root object is not intended to have listeners on its child objects");
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
    public void messageReceived(Message<Message.Payload> message) throws HousemateException {
        distributeMessage(message);
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(connectionManager.addStatusChangeListener(new ConnectionListener() {

            @Override
            public void statusChanged(ServerConnectionStatus serverConnectionStatus, ApplicationStatus applicationStatus, ApplicationInstanceStatus applicationInstanceStatus) {
                if (resend && applicationInstanceStatus == ApplicationInstanceStatus.Allowed) {
                    types.resendElements();
                    devices.resendElements();
                    resend = false;
                }
                for (RootListener<? super RealRoot> listener : getObjectListeners())
                    listener.statusChanged(RealRoot.this, serverConnectionStatus, applicationStatus, applicationInstanceStatus);
            }

            @Override
            public void newApplicationInstance(String instanceId) {
                for (RootListener<? super RealRoot> listener : getObjectListeners())
                    listener.newApplicationInstance(RealRoot.this, instanceId);
            }

            @Override
            public void newServerInstance(String serverId) {
                resend = true; // set to true. when access allowed above method will resend objects
                for (RootListener<? super RealRoot> listener : getObjectListeners())
                    listener.newServerInstance(RealRoot.this, serverId);
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
        result.add(addMessageListener(CONNECTION_STATUS_TYPE, new Receiver<ConnectionStatus>() {
            @Override
            public void messageReceived(Message<ConnectionStatus> message) throws HousemateException {
                connectionManager.setConnectionStatus(message.getPayload().getServerConnectionStatus(),
                        message.getPayload().getApplicationStatus(), message.getPayload().getApplicationInstanceStatus());
            }
        }));
        return result;
    }

    @Override
    public final RealList<TypeData<?>, RealType<?, ?, ?>> getTypes() {
        return types;
    }

    @Override
    public final void addType(RealType<?, ?, ?> type) {
        types.add(type);
    }

    @Override
    public final void removeType(String name) {
        types.remove(name);
    }

    @Override
    public final RealList<DeviceData, RealDevice> getDevices() {
        return devices;
    }

    @Override
    public final void addDevice(RealDevice device) {
        devices.add(device);
    }

    @Override
    public final void removeDevice(String name) {
        devices.remove(name);
    }
}
