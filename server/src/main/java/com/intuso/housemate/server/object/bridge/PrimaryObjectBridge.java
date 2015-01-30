package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
public abstract class PrimaryObjectBridge<WBL extends HousemateData<HousemateData<?>>,
            PO extends PrimaryObjectBridge<WBL, PO, L>, L extends PrimaryListener<? super PO>>
        extends BridgeObject<WBL, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, PO, L>
        implements PrimaryObject<CommandBridge, CommandBridge, CommandBridge, ValueBridge, ValueBridge, PO, L> {

    private CommandBridge renameCommand;
    private CommandBridge removeCommand;
    private ValueBridge runningValue;
    private CommandBridge startCommand;
    private CommandBridge stopCommand;
    private ValueBridge errorValue;

    protected PrimaryObjectBridge(Log log, ListenersFactory listenersFactory, WBL data, PrimaryObject<?, ?, ?, ?, ?, ?, ?> proxyObject,
                                  ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, listenersFactory, data);
        renameCommand = new CommandBridge(log, listenersFactory, proxyObject.getRenameCommand(), types);
        removeCommand = new CommandBridge(log, listenersFactory, proxyObject.getRemoveCommand(), types);
        runningValue = new ValueBridge(log, listenersFactory, proxyObject.getRunningValue(), types);
        startCommand = new CommandBridge(log, listenersFactory, proxyObject.getStartCommand(), types);
        stopCommand = new CommandBridge(log, listenersFactory, proxyObject.getStopCommand(), types);
        errorValue = new ValueBridge(log, listenersFactory, proxyObject.getErrorValue(), types);
        addChild(renameCommand);
        addChild(removeCommand);
        addChild(runningValue);
        addChild(startCommand);
        addChild(stopCommand);
        addChild(errorValue);
    }

    @Override
    public CommandBridge getRenameCommand() {
        return renameCommand;
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
    }

    @Override
    public boolean isRunning() {
        List<Boolean> isRunnings = RealType.deserialiseAll(BooleanType.SERIALISER, runningValue.getTypeInstances());
        return isRunnings != null && isRunnings.size() > 0 && isRunnings.get(0) != null ? isRunnings.get(0) : false;
    }

    @Override
    public ValueBridge getRunningValue() {
        return runningValue;
    }

    @Override
    public CommandBridge getStartCommand() {
        return startCommand;
    }

    @Override
    public CommandBridge getStopCommand() {
        return stopCommand;
    }

    @Override
    public String getError() {
        List<String> errors = RealType.deserialiseAll(StringType.SERIALISER, errorValue.getTypeInstances());
        return errors != null && errors.size() > 0 ? errors.get(0) : null;
    }

    @Override
    public ValueBridge getErrorValue() {
        return errorValue;
    }
}
