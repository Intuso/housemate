package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.api.object.value.ValueWrappableBase;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyValueBase<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, SWBL, SWR>>,
            SR extends ProxyResources<?>,
            WBL extends ValueWrappableBase<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>,
            T extends ProxyType<?, ?, ?, ?, ?, ?>,
            V extends ProxyValueBase<R, SR, WBL, SWBL, SWR, T, V>>
        extends ProxyObject<R, SR, WBL, SWBL, SWR, V, ValueListener<? super V>>
        implements Value<T, V> {

    public ProxyValueBase(R resources, SR subResources, WBL value) {
        super(resources, subResources, value);
    }

    @Override
    public final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(VALUE, new Receiver<TypeInstance>() {
            @Override
            public void messageReceived(Message<TypeInstance> stringMessageValueMessage) {
                for(ValueListener<? super V> listener : getObjectListeners())
                    listener.valueChanging(getThis());
                getWrappable().setValue(stringMessageValueMessage.getPayload());
                for(ValueListener<? super V> listener : getObjectListeners())
                    listener.valueChanged(getThis());
            }
        }));
        return result;
    }

    @Override
    public T getType() {
        return (T) getProxyRoot().getTypes().get(getWrappable().getType());
    }

    @Override
    public final TypeInstance getTypeInstance() {
        return getWrappable().getValue();
    }

}
