package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.intuso.housemate.client.api.bridge.v1_0.object.ObjectMapper;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeInstancesMapper;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.api.internal.object.ValueBase;
import com.intuso.housemate.client.api.internal.object.view.ValueBaseView;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public abstract class ProxyValueBaseBridge<
        VERSION_DATA extends com.intuso.housemate.client.v1_0.api.object.ValueBase.Data,
        INTERNAL_DATA extends ValueBase.Data,
        LISTENER extends ValueBase.Listener<? super VALUE>,
        VIEW extends ValueBaseView,
        VALUE extends ProxyValueBaseBridge<VERSION_DATA, INTERNAL_DATA, LISTENER, VIEW, VALUE>>
        extends ProxyObjectBridge<VERSION_DATA, INTERNAL_DATA, LISTENER, VIEW>
        implements ValueBase<INTERNAL_DATA, Type.Instances, ProxyTypeBridge, LISTENER, VIEW, VALUE> {

    private final TypeInstancesMapper typeInstancesMapper;

    private Type.Instances value;

    private Sender valueSender;
    private com.intuso.housemate.client.messaging.api.internal.Receiver<Type.Instances> valueReceiver;

    protected ProxyValueBaseBridge(Logger logger,
                                   Class<INTERNAL_DATA> versionDataClass,
                                   ObjectMapper<VERSION_DATA, INTERNAL_DATA> dataMapper,
                                   TypeInstancesMapper typeInstancesMapper,
                                   ManagedCollectionFactory managedCollectionFactory,
                                   com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                                   Sender.Factory v1_0SenderFactory) {
        super(logger, versionDataClass, dataMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
        this.typeInstancesMapper = typeInstancesMapper;
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        valueSender = v1_0SenderFactory.create(logger, com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Value.VALUE_ID));
        valueReceiver = internalReceiverFactory.create(logger, ChildUtil.name(internalName, Value.VALUE_ID), Type.Instances.class);
        valueReceiver.listen((values, wasPersisted) -> {
            logger.debug("Values updated: {}", values);
            for(ValueBase.Listener<? super VALUE> listener : listeners)
                listener.valueChanging((VALUE) ProxyValueBaseBridge.this);
            value = values;
            try {
                valueSender.send(typeInstancesMapper.map(values), wasPersisted);
            } catch (Throwable t) {
                logger.error("Failed to send new values onto proxy versioned topic", t);
            }
            for(ValueBase.Listener<? super VALUE> listener : listeners)
                listener.valueChanged((VALUE) ProxyValueBaseBridge.this);
        });
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        if(valueSender != null) {
            valueSender.close();
            valueSender = null;
        }
        if(valueReceiver != null) {
            valueReceiver.close();
            valueReceiver = null;
        }
    }

    @Override
    public ProxyTypeBridge getType() {
        return null; // todo get the type from somewhere
    }

    @Override
    public Type.Instances getValues() {
        return value;
    }
}
