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
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child resources
 * @param <DATA> the type of the data
 * @param <CHILD_DATA> the type of the child data
 * @param <CHILD> the type of the children
 * @param <TYPE> the type of the type
 * @param <VALUE> the type of the value
 */
public abstract class ProxyValueBase<
            RESOURCES extends ProxyResources<? extends HousemateObjectFactory<CHILD_RESOURCES, CHILD_DATA, CHILD>>,
            CHILD_RESOURCES extends ProxyResources<?>,
            DATA extends ValueWrappableBase<CHILD_DATA>,
            CHILD_DATA extends HousemateObjectWrappable<?>,
            CHILD extends ProxyObject<?, ?, ? extends CHILD_DATA, ?, ?, ?, ?>,
            TYPE extends ProxyType<?, ?, ?, ?, ?, ?>,
            VALUE extends ProxyValueBase<RESOURCES, CHILD_RESOURCES, DATA, CHILD_DATA, CHILD, TYPE, VALUE>>
        extends ProxyObject<RESOURCES, CHILD_RESOURCES, DATA, CHILD_DATA, CHILD, VALUE, ValueListener<? super VALUE>>
        implements Value<TYPE, VALUE> {

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param wrappable {@inheritDoc}
     */
    public ProxyValueBase(RESOURCES resources, CHILD_RESOURCES childResources, DATA wrappable) {
        super(resources, childResources, wrappable);
    }

    @Override
    public final List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(addMessageListener(VALUE_ID, new Receiver<TypeInstance>() {
            @Override
            public void messageReceived(Message<TypeInstance> stringMessageValueMessage) {
                for(ValueListener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanging(getThis());
                getWrappable().setValue(stringMessageValueMessage.getPayload());
                for(ValueListener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanged(getThis());
            }
        }));
        return result;
    }

    @Override
    public TYPE getType() {
        return (TYPE) getProxyRoot().getTypes().get(getWrappable().getType());
    }

    @Override
    public final TypeInstance getTypeInstance() {
        return getWrappable().getValue();
    }

}
