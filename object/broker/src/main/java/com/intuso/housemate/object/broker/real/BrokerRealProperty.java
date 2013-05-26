package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.object.real.RealType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 26/05/12
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
public class BrokerRealProperty<O>
        extends BrokerRealValueBase<PropertyWrappable, CommandWrappable, BrokerRealCommand, O, BrokerRealProperty<O>>
        implements Property<RealType<?, ?, O>, BrokerRealCommand, BrokerRealProperty<O>> {

    private BrokerRealCommand setCommand;

    public BrokerRealProperty(BrokerRealResources resources, String id, String name, String description, RealType<?, ?, O> type, O value) {
        super(resources, new PropertyWrappable(id, name, description, type.getId(), type.serialise(value)), type);
        setCommand = new BrokerRealCommand(resources, SET_COMMAND, SET_COMMAND, "The command to change the property's value",
                Arrays.<BrokerRealArgument<?>>asList(new BrokerRealArgument<O>(resources, VALUE_PARAM, VALUE_PARAM, "The new value for the property", type))) {
            @Override
            public void perform(Map<String, String> values) throws HousemateException {
                String newValueString = values.get(VALUE_PARAM);
                O newValue = newValueString == null ? null : getType().deserialise(newValueString);
                BrokerRealProperty.this.setTypedValue(newValue);
            }
        };
        addWrapper(setCommand);
    }

    @Override
    protected final BrokerRealProperty getThis() {
        return this;
    }

    @Override
    public void set(final String value, CommandListener<? super BrokerRealCommand> listener) {
        getSetCommand().perform(new HashMap<String, String>() {
            {
                put(VALUE, value);
            }
        }, listener);
    }

    @Override
    public BrokerRealCommand getSetCommand() {
        return setCommand;
    }
}