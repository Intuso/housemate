package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealValue;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * @param <DATA> the type of the data
 * @param <PRIMARY_OBJECT> the type of the primary object
 * @param <LISTENER> the type of the listener
 */
public class BrokerProxyPrimaryObject<
            DATA extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            PRIMARY_OBJECT extends BrokerProxyPrimaryObject<DATA, PRIMARY_OBJECT, LISTENER>,
            LISTENER extends PrimaryListener<? super PRIMARY_OBJECT>>
        extends BrokerProxyObject<DATA, HousemateObjectWrappable<?>, BrokerProxyObject<?, ?, ?, ?, ?>, PRIMARY_OBJECT, LISTENER>
        implements PrimaryObject<BrokerProxyCommand, BrokerProxyCommand, BrokerRealValue<Boolean>,
            BrokerProxyValue, BrokerProxyValue, PRIMARY_OBJECT, LISTENER> {

    private BrokerProxyCommand remove;
    private BrokerRealValue<Boolean> connected;
    private BrokerProxyValue running;
    private BrokerProxyCommand start;
    private BrokerProxyCommand stop;
    private BrokerProxyValue error;

    /**
     * @param resources {@inheritDoc}
     * @param realResources the resources for real objects
     * @param data {@inheritDoc}
     */
    protected BrokerProxyPrimaryObject(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, HousemateObjectWrappable<?>, ? extends BrokerProxyObject<?, ?, ?, ?, ?>>> resources,
                                       BrokerRealResources realResources, DATA data) {
        super(resources, data);
        connected = new BrokerRealValue<Boolean>(realResources, CONNECTED_VALUE_ID, CONNECTED_VALUE_ID,
                "Whether the server has a connection open to control the object",
                new BooleanType(realResources.getRealResources()), true);
    }

    @Override
    protected void getChildObjects() {
        remove = (BrokerProxyCommand)getWrapper(REMOVE_COMMAND_ID);
        running = (BrokerProxyValue)getWrapper(RUNNING_VALUE_ID);
        start = (BrokerProxyCommand)getWrapper(START_COMMAND_ID);
        stop = (BrokerProxyCommand)getWrapper(STOP_COMMAND_ID);
        error = (BrokerProxyValue)getWrapper(ERROR_VALUE_ID);
    }

    @Override
    public BrokerProxyCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public boolean isConnected() {
        return connected.getTypedValue() != null ? connected.getTypedValue() : false;
    }

    @Override
    public BrokerRealValue<Boolean> getConnectedValue() {
        return connected;
    }

    @Override
    public final boolean isRunning() {
        Boolean value = BooleanType.SERIALISER.deserialise(running.getTypeInstance());
        return value != null ? value : false;
    }

    @Override
    public BrokerProxyValue getRunningValue() {
        return running;
    }

    @Override
    public BrokerProxyCommand getStartCommand() {
        return start;
    }

    @Override
    public BrokerProxyCommand getStopCommand() {
        return stop;
    }

    @Override
    public final String getError() {
        return StringType.SERIALISER.deserialise(error.getTypeInstance());
    }

    @Override
    public BrokerProxyValue getErrorValue() {
        return error;
    }

    @Override
    public List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        if(running != null) {
            result.add(running.addObjectListener(new ValueListener<BrokerProxyValue>() {

                @Override
                public void valueChanging(BrokerProxyValue value) {
                    // do nothing
                }

                @Override
                public void valueChanged(BrokerProxyValue value) {
                    for(PrimaryListener<? super PRIMARY_OBJECT> listener : getObjectListeners())
                        listener.running(getThis(), isRunning());
                }
            }));
        }
        if(error != null) {
            result.add(error.addObjectListener(new ValueListener<BrokerProxyValue>() {

                @Override
                public void valueChanging(BrokerProxyValue value) {
                    // do nothing
                }

                @Override
                public void valueChanged(BrokerProxyValue value) {
                    for (PrimaryListener<? super PRIMARY_OBJECT> listener : getObjectListeners())
                        listener.error(getThis(), getError());
                }
            }));
        }
        return result;
    }
}
