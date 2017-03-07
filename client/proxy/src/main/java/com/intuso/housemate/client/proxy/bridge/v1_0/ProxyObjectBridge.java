package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.intuso.housemate.client.api.bridge.v1_0.object.ObjectMapper;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

public abstract class ProxyObjectBridge<
        VERSION_DATA extends com.intuso.housemate.client.v1_0.api.object.Object.Data,
        INTERNAL_DATA extends Object.Data,
        LISTENER extends Object.Listener>
        implements Object<LISTENER> {

    protected final Logger logger;
    protected VERSION_DATA data;
    protected final Class<INTERNAL_DATA> internalDataClass;
    protected final ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper;
    protected final ManagedCollection<LISTENER> listeners;
    protected final com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory;
    protected final Sender.Factory v1_0SenderFactory;

    private Sender sender;
    private com.intuso.housemate.client.messaging.api.internal.Receiver<INTERNAL_DATA> receiver;

    protected ProxyObjectBridge(Logger logger,
                                Class<INTERNAL_DATA> internalDataClass,
                                ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper,
                                ManagedCollectionFactory managedCollectionFactory,
                                com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                Sender.Factory v1_0SenderFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.internalDataClass = internalDataClass;
        this.dataMapper = dataMapper;
        this.internalReceiverFactory = internalReceiverFactory;
        this.v1_0SenderFactory = v1_0SenderFactory;
        this.listeners = managedCollectionFactory.create();
    }

    public final void init(String versionName, String internalName) {
        logger.debug("Init {} -> {}", internalName, versionName);
        sender = v1_0SenderFactory.create(logger, versionName);
        receiver = internalReceiverFactory.create(logger, internalName, internalDataClass);
        receiver.listen(new com.intuso.housemate.client.messaging.api.internal.Receiver.Listener<INTERNAL_DATA>() {
                    @Override
                    public void onMessage(INTERNAL_DATA data, boolean wasPersisted) {
                        try {
                            sender.send(dataMapper.map(data), wasPersisted);
                        } catch (Throwable t) {
                            logger.error("Failed to send data object", t);
                        }
                    }
                });
        initChildren(versionName, internalName);
    }

    protected void initChildren(String versionName, String internalName) {}

    public final void uninit() {
        logger.debug("Uninit");
        uninitChildren();
        if(sender != null) {
            sender.close();
            sender = null;
        }
        if(receiver != null) {
            receiver.close();
            receiver = null;
        }
    }

    protected void uninitChildren() {}

    @Override
    public String getObjectClass() {
        return data.getObjectClass();
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public final String getName() {
        return data.getName();
    }

    @Override
    public final String getDescription() {
        return data.getDescription();
    }

    @Override
    public ManagedCollection.Registration addObjectListener(LISTENER listener) {
        return listeners.add(listener);
    }

    public interface Factory<OBJECT extends ProxyObjectBridge<?, ?, ?>> {
        OBJECT create(Logger logger);
    }
}