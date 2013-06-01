package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/01/13
 * Time: 23:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyPrimaryObject<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SR extends ProxyResources<?>,
            WBL extends HousemateObjectWrappable<HousemateObjectWrappable<?>>,
            P extends ProxyProperty<?, ?, ?, ?, P>,
            C extends ProxyCommand<?, ?, ?, ?, C>, V extends ProxyValue<?, ?, V>,
            PO extends ProxyPrimaryObject<R, SR, WBL, P, C, V, PO, L>,
            L extends PrimaryListener<? super PO>>
        extends ProxyObject<R, SR, WBL, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>, PO, L>
        implements PrimaryObject<P, C, C, V, V, V, PO, L> {

    private C remove;
    private V connected;
    private V running;
    private C start;
    private C stop;
    private V error;

    protected ProxyPrimaryObject(R resources, SR subResources, WBL wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        remove = (C)getWrapper(REMOVE_COMMAND);
        connected = (V)getWrapper(CONNECTED_VALUE);
        running = (V)getWrapper(RUNNING_VALUE);
        start = (C)getWrapper(START_COMMAND);
        stop = (C)getWrapper(STOP_COMMAND);
        error = (V)getWrapper(ERROR_VALUE);
    }

    @Override
    public C getRemoveCommand() {
        return remove;
    }

    @Override
    public Boolean isConnected() {
        return Boolean.parseBoolean(connected.getValue());
    }

    @Override
    public V getConnectedValue() {
        return connected;
    }

    @Override
    public final Boolean isRunning() {
        return Boolean.parseBoolean(running.getValue());
    }

    @Override
    public V getRunningValue() {
        return running;
    }

    @Override
    public C getStartCommand() {
        return start;
    }

    @Override
    public C getStopCommand() {
        return stop;
    }

    @Override
    public final String getError() {
        return error.getValue();
    }

    @Override
    public V getErrorValue() {
        return error;
    }

    @Override
    public List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        if(running != null) {
            result.add(running.addObjectListener(new ValueListener<V>() {
                @Override
                public void valueChanged(V value) {
                    for(PrimaryListener<? super PO> listener : getObjectListeners())
                        listener.running(getThis(), isRunning());
                }
            }));
        }
        if(error != null) {
            result.add(error.addObjectListener(new ValueListener<V>() {
                @Override
                public void valueChanged(V value) {
                    for (PrimaryListener<? super PO> listener : getObjectListeners())
                        listener.error(getThis(), error.getValue());
                }
            }));
        }
        return result;
    }
}
