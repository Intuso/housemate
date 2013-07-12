package com.intuso.housemate.object.real;

import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectListener;
import com.intuso.housemate.api.object.root.real.RealRoot;


public abstract class RealObject<
            DATA extends HousemateData<CHILD_DATA>,
            CHILD_DATA extends HousemateData<?>,
            CHILD extends RealObject<? extends CHILD_DATA, ?, ?, ?>,
            LISTENER extends ObjectListener>
        extends HousemateObject<RealResources, DATA, CHILD_DATA, CHILD, LISTENER> {

    private RealRoot<?, ?, ?, ?, ?> realRoot;

    /**
     * @param resources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    protected RealObject(RealResources resources, DATA data) {
        super(resources, data);
    }

    /**
     * Sends a message to the broker
     * @param type the type of the message
     * @param payload the message payload
     * @param <MV> the type of the message's payload
     */
    protected final <MV extends Message.Payload> void sendMessage(String type, MV payload) {
        getRealRoot().sendMessage(new Message<MV>(
                getPath(), type, payload));
    }

    /**
     * Gets the root object for this object
     * @return the root object for this object
     */
    protected RealRoot<?, ?, ?, ?, ?> getRealRoot() {
        return realRoot;
    }

    @Override
    protected void initPreRecurseHook(HousemateObject<?, ?, ?, ?, ?> parent) {

        // get the broker for this object
        // get the broker for this object
        if(this instanceof RealRoot)
            realRoot = (RealRoot)this;
        else if(parent != null && parent instanceof RealObject)
            realRoot = ((RealObject)parent).realRoot;
    }
}
