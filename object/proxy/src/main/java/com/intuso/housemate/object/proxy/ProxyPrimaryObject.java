package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <DATA> the type of the data
 * @param <COMMAND> the type of the command
 * @param <VALUE> the type of the value
 * @param <PRIMARY_OBJECT> the type of the primary object
 * @param <LISTENER> the type of the listener
 */
public abstract class ProxyPrimaryObject<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            DATA extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            COMMAND extends ProxyCommand<?, ?, ?, ?, COMMAND>,
            VALUE extends ProxyValue<?, ?, VALUE>,
            PRIMARY_OBJECT extends ProxyPrimaryObject<RESOURCES, CHILD_RESOURCES, DATA, COMMAND, VALUE, PRIMARY_OBJECT, LISTENER>,
            LISTENER extends PrimaryListener<? super PRIMARY_OBJECT>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, DATA, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, PRIMARY_OBJECT, LISTENER>
        implements PrimaryObject<COMMAND, COMMAND, VALUE, VALUE, VALUE, PRIMARY_OBJECT, LISTENER> {

    private COMMAND remove;
    private VALUE connected;
    private VALUE running;
    private COMMAND start;
    private COMMAND stop;
    private VALUE error;

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param wrappable {@inheritDoc}
     */
    protected ProxyPrimaryObject(RESOURCES resources, CHILD_RESOURCES childResources, DATA wrappable) {
        super(resources, childResources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        remove = (COMMAND)getWrapper(REMOVE_COMMAND_ID);
        connected = (VALUE)getWrapper(CONNECTED_VALUE_ID);
        running = (VALUE)getWrapper(RUNNING_VALUE_ID);
        start = (COMMAND)getWrapper(START_COMMAND_ID);
        stop = (COMMAND)getWrapper(STOP_COMMAND_ID);
        error = (VALUE)getWrapper(ERROR_VALUE_ID);
    }

    @Override
    public COMMAND getRemoveCommand() {
        return remove;
    }

    @Override
    public boolean isConnected() {
        return connected.getTypeInstance() != null ? Boolean.parseBoolean(connected.getTypeInstance().getValue()) : null;
    }

    @Override
    public VALUE getConnectedValue() {
        return connected;
    }

    @Override
    public final boolean isRunning() {
        return running.getTypeInstance() != null ? Boolean.parseBoolean(running.getTypeInstance().getValue()) : null;
    }

    @Override
    public VALUE getRunningValue() {
        return running;
    }

    @Override
    public COMMAND getStartCommand() {
        return start;
    }

    @Override
    public COMMAND getStopCommand() {
        return stop;
    }

    @Override
    public final String getError() {
        return error.getTypeInstance() != null ? error.getTypeInstance().getValue() : null;
    }

    @Override
    public VALUE getErrorValue() {
        return error;
    }

    @Override
    public List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        if(running != null) {
            result.add(running.addObjectListener(new ValueListener<VALUE>() {

                @Override
                public void valueChanging(VALUE value) {
                    // do nothing
                }

                @Override
                public void valueChanged(VALUE value) {
                    for(PrimaryListener<? super PRIMARY_OBJECT> listener : getObjectListeners())
                        listener.running(getThis(), isRunning());
                }
            }));
        }
        if(error != null) {
            result.add(error.addObjectListener(new ValueListener<VALUE>() {

                @Override
                public void valueChanging(VALUE value) {
                    // do nothing
                }

                @Override
                public void valueChanged(VALUE value) {
                    for (PrimaryListener<? super PRIMARY_OBJECT> listener : getObjectListeners())
                        listener.error(getThis(), getError());
                }
            }));
        }
        return result;
    }
}
