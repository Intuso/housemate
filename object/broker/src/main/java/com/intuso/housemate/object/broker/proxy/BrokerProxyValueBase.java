package com.intuso.housemate.object.broker.proxy;

import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.message.StringMessageValue;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.api.object.value.ValueWrappableBase;
import com.intuso.housemate.object.broker.ClientPayload;
import com.intuso.listeners.ListenerRegistration;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:21
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyValueBase<WBL extends ValueWrappableBase<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends BrokerProxyObject<? extends SWBL, ?, ?, ?, ?>,
            V extends BrokerProxyValueBase<WBL, SWBL, SWR, V>>
        extends BrokerProxyObject<WBL, SWBL, SWR, V, ValueListener<? super V>>
        implements Value<BrokerProxyType, V> {

    private BrokerProxyType type;

    public BrokerProxyValueBase(BrokerProxyResources<? extends HousemateObjectFactory<BrokerProxyResources<?>, SWBL, ? extends SWR>> resources, WBL value) {
        super(resources, value);
    }

    @Override
    public BrokerProxyType getType() {
        return type;
    }

    @Override
    public String getValue() {
        return getWrappable().getValue();
    }

    @Override
    public final List<ListenerRegistration<?>> registerListeners() {
        List<ListenerRegistration<?>> result = super.registerListeners();
        result.add(addMessageListener(VALUE, new Receiver<ClientPayload<StringMessageValue>>() {
            @Override
            public void messageReceived(Message<ClientPayload<StringMessageValue>> stringMessageValueMessage) {
                getWrappable().setValue(stringMessageValueMessage.getPayload().getOriginal().getValue());
                for(ValueListener<? super V> listener : getObjectListeners())
                    listener.valueChanged(getThis());
            }
        }));
        return result;
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        type = getResources().getRoot().getTypes().get(getWrappable().getType());
        if(type == null)
            getLog().e("Could not unwrap value, value type \"" + getWrappable().getType() + "\" is not known");
    }

}