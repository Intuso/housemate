package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the chil resources
 * @param <TYPE> the type of the type
 * @param <SET_COMMAND> the type of the set command
 * @param <PROPERTY> the type of the property
 */
public abstract class ProxyProperty<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, CommandWrappable, SET_COMMAND>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            TYPE extends ProxyType<?, ?, ?, ?, ?, ?>,
            SET_COMMAND extends ProxyCommand<?, ?, ?, ?, SET_COMMAND>,
            PROPERTY extends ProxyProperty<RESOURCES, CHILD_RESOURCES, TYPE, SET_COMMAND, PROPERTY>>
        extends ProxyValueBase<RESOURCES, CHILD_RESOURCES, PropertyWrappable, CommandWrappable, SET_COMMAND, TYPE, PROPERTY>
        implements Property<TYPE, SET_COMMAND, PROPERTY> {

    private SET_COMMAND setCommand;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param wrappable {@inheritDoc}
     */
    public ProxyProperty(RESOURCES resources, CHILD_RESOURCES childResources, PropertyWrappable wrappable) {
        super(resources, childResources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        setCommand = (SET_COMMAND) getWrapper(SET_COMMAND_ID);
    }

    @Override
    public void set(final TypeInstance value, CommandListener<? super SET_COMMAND> listener) {
        getSetCommand().perform(new TypeInstances() {
            {
                put(VALUE_ID, value);
            }
        }, listener);
    }

    @Override
    public SET_COMMAND getSetCommand() {
        return setCommand;
    }
}
