package com.intuso.housemate.platform.android.service.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.access.ConnectionStatus;
import com.intuso.housemate.comms.v1_0.serialiser.javabin.JavabinSerialiser;
import com.intuso.housemate.comms.v1_0.transport.socket.client.SocketClient;
import com.intuso.housemate.comms.v1_0.transport.socket.client.ioc.SocketClientModule;
import com.intuso.housemate.platform.android.common.SharedPreferencesPropertyRepository;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionService extends Service {

    private final static String LOG_LEVEL = "log.level";

    public final static String NETWORK_AVAILABLE_ACTION = "networkAvailable";
    public final static String NETWORK_AVAILABLE = "networkAvailable";

    private final Binder binder = new Binder();

    private final ListenersFactory listenersFactory;
    private final PropertyRepository defaultProperties;

    private PropertyRepository properties;
    private Logger logger;
    private SocketClient router;
    private ListenerRegistration routerListenerRegistration;

    public ConnectionService() {

        // create a listeners factory
        listenersFactory = new ListenersFactory() {
            @Override
            public <LISTENER extends Listener> Listeners<LISTENER> create() {
                return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };

        // setup the default properties
        defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(listenersFactory);
        // use the normal Guice modules to set the default properties
        new SocketClientModule(defaultProperties);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create all the required objects
        properties = new SharedPreferencesPropertyRepository(listenersFactory, defaultProperties, getApplicationContext());
        logger = LoggerFactory.getLogger(ConnectionService.class);
        router = new SocketClient(logger, listenersFactory, properties, new JavabinSerialiser.Factory());

        logger.debug("Connection Service created");

        // listen on the router root object, then connect the router
        routerListenerRegistration = router.addListener(new Router.Listener<Router>() {

            @Override
            public void serverConnectionStatusChanged(Router root, ConnectionStatus connectionStatus) {
                logger.debug("Server connection status: " + connectionStatus);
                if(connectionStatus == ConnectionStatus.DisconnectedPermanently) {
                    router.connect();
                }
            }

            @Override
            public void newServerInstance(Router root, String serverId) {
                // do nothing
            }
        });
        // connections happen in a different thread so we can call this without blocking
        router.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.debug("Connection Service destroyed");
        routerListenerRegistration.removeListener();
        router.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && NETWORK_AVAILABLE_ACTION.equals(intent.getAction())) {
            if(intent.getExtras().containsKey(NETWORK_AVAILABLE)) {
                logger.debug("Received network available update: " + intent.getBooleanExtra(NETWORK_AVAILABLE, true));
                router.networkAvailable(intent.getBooleanExtra(NETWORK_AVAILABLE, true));
            }
        }
        return START_STICKY;
    }

    public class Binder extends android.os.Binder {
        public Router<?> getRouter() {
            return router;
        }
    }
}

