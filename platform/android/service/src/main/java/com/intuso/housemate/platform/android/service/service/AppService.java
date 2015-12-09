package com.intuso.housemate.platform.android.service.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import com.google.common.collect.Maps;
import com.intuso.housemate.comms.v1_0.api.ClientConnection;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.api.Router;
import com.intuso.housemate.comms.v1_0.api.access.ConnectionStatus;
import com.intuso.housemate.comms.v1_0.api.payload.StringPayload;
import com.intuso.housemate.platform.android.common.JsonMessage;
import com.intuso.housemate.platform.android.common.MessageCodes;
import com.intuso.housemate.platform.android.service.R;
import com.intuso.housemate.platform.android.service.activity.HousemateActivity;
import com.intuso.utilities.listener.ListenerRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class AppService extends Service implements ServiceConnection, Router.Listener<Router> {

    private final static int NOTIFICATION_ID = AppService.class.getName().hashCode();

    private final Map<String, Router.Registration> clientReceivers = Maps.newHashMap();
    private final Messenger messenger;

    private Logger logger;
    private Router<?> router;
    private ListenerRegistration routerListenerRegistration;

    public AppService() {
        this.messenger = new Messenger(new MessageHandler());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(NOTIFICATION_ID, getNotification(ConnectionStatus.DisconnectedPermanently));
        startConnectionService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(logger == null) {
            logger = null;
            router = null;
            unbindService(this);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        ConnectionService.Binder binder = (ConnectionService.Binder) iBinder;
        logger = LoggerFactory.getLogger(AppService.class);
        router = binder.getRouter();
        serverConnectionStatusChanged(router, router.getConnectionStatus());
        routerListenerRegistration = router.addListener(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        routerListenerRegistration.removeListener();
        serverConnectionStatusChanged(null, ConnectionStatus.DisconnectedPermanently);
        logger = null;
        router = null;
        startConnectionService();
    }

    private void startConnectionService() {
        Intent intent = new Intent(this, ConnectionService.class);
        startService(intent);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void serverConnectionStatusChanged(Router clientConnection, ConnectionStatus connectionStatus) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, getNotification(connectionStatus));
    }

    private Notification getNotification(ConnectionStatus connectionStatus) {
        Notification.Builder notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Housemate Server Connection");
        switch (connectionStatus) {
            case ConnectedToServer:
                notification.setContentText("Connected to server")
                    .setPriority(Notification.PRIORITY_MIN);
                break;
            case ConnectedToRouter:
                notification.setContentText("Connected to router")
                        .setPriority(Notification.PRIORITY_MIN);
                break;
            case Connecting:
                notification.setContentText("Connecting to server")
                        .setPriority(Notification.PRIORITY_MIN);
                break;
            case DisconnectedTemporarily:
                notification.setContentText("Disconnected temporarily - will automatically reconnect")
                        .setPriority(Notification.PRIORITY_MIN);
                break;
            case DisconnectedPermanently:
                notification.setContentText("Disconnected. Tap here to configure the connection settings")
                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), HousemateActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                        .setPriority(Notification.PRIORITY_HIGH);
                break;
        }
        return notification.build();
    }

    @Override
    public void newServerInstance(Router clientConnection, String serverId) {
        // do nothing
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {

            if(router == null)
                return;

            Router.Registration registration;
            String id;
            switch (msg.what) {
                case MessageCodes.CREATE_REGISTRATION:
                    id = UUID.randomUUID().toString();
                    while(clientReceivers.containsKey(id))
                        id = UUID.randomUUID().toString();
                    logger.debug("Registering new client " + id);
                    try {
                        android.os.Message reply = android.os.Message.obtain();
                        reply.what = MessageCodes.REGISTERED;
                        reply.getData().putString("id", id);
                        msg.replyTo.send(reply);
                    } catch (RemoteException e) {
                        logger.error("Failed to send message to client", e);
                    }
                    clientReceivers.put(id, router.registerReceiver(new ClientReceiver(id, msg.replyTo)));
                    break;
                case MessageCodes.RE_REGISTER:
                    id = msg.getData().getString("id");
                    logger.debug("Registering client " + id);
                    try {
                        android.os.Message reply = android.os.Message.obtain();
                        reply.what = MessageCodes.REGISTERED;
                        reply.getData().putString("id", id);
                        msg.replyTo.send(reply);
                    } catch (RemoteException e) {
                        logger.error("Failed to send message to client", e);
                    }
                    clientReceivers.put(id, router.registerReceiver(new ClientReceiver(id, msg.replyTo)));
                    break;
                case MessageCodes.UNREGISTER:
                    id = msg.getData().getString("id");
                    logger.debug("Unregistering client " + id);
                    registration = clientReceivers.remove(msg.getData().getString("id"));
                    if(registration != null)
                        registration.unregister();
                    else
                        logger.error("Could not find client with id " + id);
                    break;
                case MessageCodes.SEND_MESSAGE:
                    msg.getData().setClassLoader(Message.class.getClassLoader());
                    id = msg.getData().getString("id");
                    registration = clientReceivers.get(id);
                    if(registration != null) {
                        Message<?> message;
                        try {
                            message = ((JsonMessage) msg.getData().getParcelable("message")).getMessage();
                        } catch(Throwable t) {
                            logger.error("Failed to get message from parcelable");
                            break;
                        }
                        registration.sendMessage(message);
                    } else
                        logger.error("Could not find registration for client " + id);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private class ClientReceiver implements Router.Receiver {

        private final String id;
        private final Messenger clientReceiver;

        private ClientReceiver(String id, Messenger clientReceiver) {
            this.id = id;
            this.clientReceiver = clientReceiver;
        }

        @Override
        public void messageReceived(Message message) {
            try {
                android.os.Message msg = android.os.Message.obtain(null, MessageCodes.SEND_MESSAGE);
                msg.getData().putParcelable("message", new JsonMessage(message));
                clientReceiver.send(msg);
            } catch(DeadObjectException e) {
                logger.debug("Client no longer connected");
                clientReceivers.get(id).unregister();
            } catch(RemoteException e) {
                logger.error("Failed to send message to client", e);
            } catch(Throwable t) {
                logger.error("Problem sending message to client", t);
            }
        }

        @Override
        public void serverConnectionStatusChanged(ClientConnection clientConnection, ConnectionStatus connectionStatus) {
            messageReceived(new Message(new String[]{""}, ClientConnection.NEXT_CONNECTION_STATUS_TYPE, connectionStatus));
        }

        @Override
        public void newServerInstance(ClientConnection clientConnection, String serverId) {
            messageReceived(new Message(new String[] {""}, ClientConnection.SERVER_INSTANCE_ID_TYPE, new StringPayload(serverId)));
        }
    }
}

