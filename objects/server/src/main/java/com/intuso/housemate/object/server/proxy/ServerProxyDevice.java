package com.intuso.housemate.object.server.proxy;

import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.server.real.ServerRealValue;
import com.intuso.housemate.object.real.impl.type.BooleanType;

import java.util.List;

public class ServerProxyDevice
        extends ServerProxyPrimaryObject<
                DeviceData,
        ServerProxyDevice,
                    DeviceListener<? super ServerProxyDevice>>
        implements Device<
        ServerProxyCommand,
        ServerProxyCommand,
        ServerProxyCommand,
        ServerProxyList<CommandData, ServerProxyCommand>,
        ServerRealValue<Boolean>,
        ServerProxyValue,
        ServerProxyValue,
        ServerProxyValue,
        ServerProxyList<ValueData, ServerProxyValue>,
        ServerProxyProperty,
        ServerProxyList<PropertyData, ServerProxyProperty>,
        ServerProxyDevice> {

    private ServerProxyList<CommandData, ServerProxyCommand> commands;
    private ServerProxyList<ValueData, ServerProxyValue> values;
    private ServerProxyList<PropertyData, ServerProxyProperty> properties;
    private ServerRealValue<Boolean> connected;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ServerProxyDevice(ServerProxyResources<ServerProxyFactory.All> resources, DeviceData data) {
        super(resources, data);
        connected = new ServerRealValue<Boolean>(resources.getServerRealResources(), CONNECTED_ID, CONNECTED_ID,
                "Whether the server has a connection open to control the object",
                new BooleanType(resources.getRealResources()), true);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        commands = (ServerProxyList<CommandData, ServerProxyCommand>) getChild(COMMANDS_ID);
        values = (ServerProxyList<ValueData, ServerProxyValue>) getChild(VALUES_ID);
        properties = (ServerProxyList<PropertyData, ServerProxyProperty>) getChild(PROPERTIES_ID);
    }

    @Override
    public ServerProxyList<CommandData, ServerProxyCommand> getCommands() {
        return commands;
    }

    @Override
    public ServerProxyList<ValueData, ServerProxyValue> getValues() {
        return values;
    }

    @Override
    public ServerProxyList<PropertyData, ServerProxyProperty> getProperties() {
        return properties;
    }

    @Override
    public boolean isConnected() {
        return connected.getTypedValue() != null ? connected.getTypedValue() : false;
    }

    @Override
    public ServerRealValue<Boolean> getConnectedValue() {
        return connected;
    }

    @Override
    public final List<String> getFeatureIds() {
        return getData().getFeatureIds();
    }

    @Override
    public final List<String> getCustomCommandIds() {
        return getData().getCustomCommandIds();
    }

    @Override
    public final List<String> getCustomValueIds() {
        return getData().getCustomValueIds();
    }

    @Override
    public final List<String> getCustomPropertyIds() {
        return getData().getCustomPropertyIds();
    }
}