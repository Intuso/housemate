package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeInstancesMapper;
import com.intuso.housemate.client.api.bridge.v1_0.object.ValueMapper;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.Value;
import com.intuso.housemate.client.api.internal.object.view.ValueView;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyValueBridge
        extends ProxyValueBaseBridge<com.intuso.housemate.client.v1_0.api.object.Value.Data, Value.Data, Value.Listener<? super ProxyValueBridge>, ValueView, ProxyValueBridge>
        implements Value<Type.Instances, ProxyTypeBridge, ProxyValueBridge> {

    @Inject
    protected ProxyValueBridge(@Assisted Logger logger,
                               ValueMapper valueMapper,
                               TypeInstancesMapper typeInstancesMapper,
                               ManagedCollectionFactory managedCollectionFactory,
                               com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                               Sender.Factory v1_0SenderFactory) {
        super(logger, Value.Data.class, valueMapper, typeInstancesMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
    }

    @Override
    public ProxyObjectBridge<?, ?, ?, ?> getChild(String id) {
        return null;
    }
}
