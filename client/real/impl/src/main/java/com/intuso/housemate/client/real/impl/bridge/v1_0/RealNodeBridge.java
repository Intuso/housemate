package com.intuso.housemate.client.real.impl.bridge.v1_0;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.intuso.housemate.client.api.bridge.v1_0.object.NodeMapper;
import com.intuso.housemate.client.api.internal.object.Node;
import com.intuso.housemate.client.api.internal.object.view.NodeView;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import com.intuso.housemate.client.real.impl.internal.ChildUtil;
import com.intuso.housemate.client.real.impl.internal.ServerBaseNode;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class RealNodeBridge
        extends RealObjectBridge<com.intuso.housemate.client.v1_0.api.object.Node.Data, Node.Data, Node.Listener<? super RealNodeBridge>, NodeView>
        implements Node<RealCommandBridge, RealListBridge<RealTypeBridge>, RealListBridge<RealHardwareBridge>, RealNodeBridge>,
        ServerBaseNode<RealCommandBridge, RealListBridge<RealTypeBridge>, RealListBridge<RealHardwareBridge>, RealNodeBridge> {

    private final String id;
    private final String versionName;
    private final com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0SenderFactory;
    private final com.intuso.housemate.client.v1_0.messaging.api.Receiver.Factory v1_0ReceiverFactory;

    private final RealListBridge<RealTypeBridge> types;
    private final RealListBridge<RealHardwareBridge> hardwares;
    private final RealCommandBridge addHardwareCommand;

    @AssistedInject
    protected RealNodeBridge(@Assisted("id") String id,
                             @Assisted Logger logger,
                             @Assisted("versionName") String versionName,
                             @Assisted com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0SenderFactory,
                             @Assisted com.intuso.housemate.client.v1_0.messaging.api.Receiver.Factory v1_0ReceiverFactory,
                             NodeMapper nodeMapper,
                             ManagedCollectionFactory managedCollectionFactory,
                             RealObjectBridge.Factory<RealListBridge<RealTypeBridge>> typesFactory,
                             RealObjectBridge.Factory<RealListBridge<RealHardwareBridge>> hardwaresFactory,
                             RealObjectBridge.Factory<RealCommandBridge> commandFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Node.Data.class, nodeMapper, managedCollectionFactory);
        this.id = id;
        this.versionName = versionName;
        this.v1_0SenderFactory = v1_0SenderFactory;
        this.v1_0ReceiverFactory = v1_0ReceiverFactory;
        types = typesFactory.create(ChildUtil.logger(logger, Node.TYPES_ID));
        hardwares = hardwaresFactory.create(ChildUtil.logger(logger, Node.HARDWARES_ID));
        addHardwareCommand = commandFactory.create(ChildUtil.logger(logger, Node.ADD_HARDWARE_ID));
    }

    @Override
    public void init(String name, Sender.Factory senderFactory, Receiver.Factory receiverFactory) {
        init(versionName, name, senderFactory, receiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
    }

    @Override
    protected void initChildren(String versionName, String internalName, Sender.Factory internalSenderFactory, Receiver.Factory internalReceiverFactory, com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0SenderFactory, com.intuso.housemate.client.v1_0.messaging.api.Receiver.Factory v1_0ReceiverFactory) {
        super.initChildren(versionName, internalName, internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
        types.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.TYPES_ID),
                ChildUtil.name(internalName, Node.TYPES_ID),
                internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
        hardwares.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.HARDWARES_ID),
                ChildUtil.name(internalName, Node.HARDWARES_ID),
                internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
        addHardwareCommand.init(
                com.intuso.housemate.client.v1_0.real.impl.ChildUtil.name(versionName, com.intuso.housemate.client.v1_0.api.object.Node.ADD_HARDWARE_ID),
                ChildUtil.name(internalName, Node.ADD_HARDWARE_ID),
                internalSenderFactory, internalReceiverFactory, v1_0SenderFactory, v1_0ReceiverFactory);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        types.uninit();
        hardwares.uninit();
        addHardwareCommand.uninit();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public RealListBridge<RealTypeBridge> getTypes() {
        return types;
    }

    @Override
    public RealListBridge<RealHardwareBridge> getHardwares() {
        return hardwares;
    }

    @Override
    public RealCommandBridge getAddHardwareCommand() {
        return addHardwareCommand;
    }

    @Override
    public RealObjectBridge<?, ?, ?, ?> getChild(String id) {
        if(ADD_HARDWARE_ID.equals(id))
            return addHardwareCommand;
        else if(HARDWARES_ID.equals(id))
            return hardwares;
        else if(TYPES_ID.equals(id))
            return types;
        return null;
    }

    public interface Factory {
        RealNodeBridge create(@Assisted("id") String id, Logger logger, @Assisted("versionName") String versionName,
                              com.intuso.housemate.client.v1_0.messaging.api.Sender.Factory v1_0SenderFactory,
                              com.intuso.housemate.client.v1_0.messaging.api.Receiver.Factory v1_0ReceiverFactory);
    }
}
