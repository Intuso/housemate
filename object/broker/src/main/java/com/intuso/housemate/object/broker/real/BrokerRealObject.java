package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.ObjectListener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/01/13
 * Time: 00:29
 * To change this template use File | Settings | File Templates.
 */
public class BrokerRealObject<WBL extends HousemateObjectWrappable<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends HousemateObject<?, ? extends SWBL, ?, ?, ?>,
            L extends ObjectListener>
        extends HousemateObject<BrokerRealResources, WBL, SWBL, SWR, L> {
    protected BrokerRealObject(BrokerRealResources resources, WBL wrappable) {
        super(resources, wrappable);
    }
}