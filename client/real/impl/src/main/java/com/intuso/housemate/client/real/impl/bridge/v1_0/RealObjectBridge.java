package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.intuso.housemate.client.api.bridge.v1_0.object.ObjectMapper;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Tree;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.List;

public abstract class RealObjectBridge<
        VERSION_DATA extends com.intuso.housemate.client.v1_0.api.object.Object.Data,
        INTERNAL_DATA extends Object.Data,
        LISTENER extends Object.Listener,
        VIEW extends View>
        implements Object<INTERNAL_DATA, LISTENER, VIEW> {

    protected final Logger logger;
    protected VERSION_DATA data;
    protected final Class<VERSION_DATA> versionDataClass;
    protected final ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper;
    protected final ManagedCollection<LISTENER> listeners;

    private com.intuso.housemate.client.messaging.api.internal.Sender sender;
    private Receiver<VERSION_DATA> receiver;

    protected RealObjectBridge(Logger logger,
                               Class<VERSION_DATA> versionDataClass,
                               ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper,
                               ManagedCollectionFactory managedCollectionFactory) {
        logger.debug("Creating");
        this.logger = logger;
        this.versionDataClass = versionDataClass;
        this.dataMapper = dataMapper;
        this.listeners = managedCollectionFactory.createSet();
    }

    @Override
    public VIEW createView(View.Mode mode) {
        throw new UnsupportedOperationException("This implementation should not be viewed");
    }

    @Override
    public Tree getTree(VIEW view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {
        throw new UnsupportedOperationException("This implementation should not be viewed");
    }

    public final void init(String versionName, String internalName, Sender.Factory internalSenderFactory, com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory, com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0SenderFactory, Receiver.Factory v1_0ReceiverFactory) {
        logger.debug("Init {} -> {}", versionName, internalName);
        sender = internalSenderFactory.create(logger, internalName);
        receiver = v1_0ReceiverFactory.create(logger, versionName, versionDataClass);
        receiver.listen(new Receiver.Listener<VERSION_DATA>() {
                    @Override
                    public void onMessage(VERSION_DATA data, boolean persistent) {
                        try {
                            sender.send(dataMapper.map(data), persistent);
                        } catch (Throwable t) {
                            logger.error("Failed to send data object", t);
                        }
                    }
                });
        initChildren(versionName, internalName, internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
    }

    protected void initChildren(String versionName, String internalName, Sender.Factory internalSenderFactory, com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory, com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0SenderFactory, Receiver.Factory v1_0ReceiverFactory) {}

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
    public INTERNAL_DATA getData() {
        return dataMapper.map(data);
    }

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

    public interface Factory<OBJECT extends RealObjectBridge<?, ?, ?, ?>> {
        OBJECT create(Logger logger);
    }
}
