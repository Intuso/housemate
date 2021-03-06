package com.intuso.housemate.client.proxy.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.PropertyMapper;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeInstancesMapper;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Type;
import com.intuso.housemate.client.api.internal.object.view.PropertyView;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.v1_0.messaging.api.Sender;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyPropertyBridge
        extends ProxyValueBaseBridge<com.intuso.housemate.client.v1_0.api.object.Property.Data, Property.Data, Property.Listener<? super ProxyPropertyBridge>, PropertyView, ProxyPropertyBridge>
        implements Property<Type.Instances, ProxyTypeBridge, ProxyCommandBridge, ProxyPropertyBridge> {

    private final ProxyCommandBridge setCommand;

    @Inject
    public ProxyPropertyBridge(@Assisted Logger logger,
                               PropertyMapper propertyMapper,
                               TypeInstancesMapper typeInstancesMapper,
                               ManagedCollectionFactory managedCollectionFactory,
                               com.intuso.housemate.client.messaging.api.internal.Receiver.Factory internalReceiverFactory,
                               Sender.Factory v1_0SenderFactory,
                               ProxyObjectBridge.Factory<ProxyCommandBridge> commandFactory) {
        super(logger, Property.Data.class, propertyMapper, typeInstancesMapper, managedCollectionFactory, internalReceiverFactory, v1_0SenderFactory);
        setCommand = commandFactory.create(ChildUtil.logger(logger, Property.SET_COMMAND_ID));
    }

    @Override
    protected void initChildren(String versionName, String internalName) {
        super.initChildren(versionName, internalName);
        setCommand.init(
                com.intuso.housemate.client.proxy.internal.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Property.SET_COMMAND_ID),
                ChildUtil.name(internalName, Property.SET_COMMAND_ID)
        );
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        setCommand.uninit();
    }

    @Override
    public ProxyCommandBridge getSetCommand() {
        return setCommand;
    }

    @Override
    public ProxyObjectBridge<?, ?, ?, ?> getChild(String id) {
        if(SET_COMMAND_ID.equals(id))
            return setCommand;
        return null;
    }

    public interface Factory {
        ProxyPropertyBridge create(Logger logger);
    }

    @Override
    public void set(final Type.Instances value, Command.PerformListener<? super ProxyCommandBridge> listener) {
        Type.InstanceMap values = new Type.InstanceMap();
        values.put(Property.VALUE_ID, value);
        getSetCommand().perform(values, listener);
    }
}
