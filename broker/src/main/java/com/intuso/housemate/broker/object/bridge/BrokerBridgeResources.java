package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.broker.object.general.BrokerGeneralResources;
import com.intuso.housemate.broker.object.general.BrokerResourcesImpl;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 12/03/13
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */
public class BrokerBridgeResources extends BrokerResourcesImpl<RootObjectBridge> {

    public BrokerBridgeResources(BrokerGeneralResources generalResources) {
        super(generalResources);
    }
}