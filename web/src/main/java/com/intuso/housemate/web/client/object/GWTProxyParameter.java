package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.NoChildrenProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyParameter;
import com.intuso.housemate.comms.v1_0.api.payload.NoChildrenData;
import com.intuso.housemate.comms.v1_0.api.payload.ParameterData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyParameter extends ProxyParameter<GWTProxyParameter> {

    @Inject
    public GWTProxyParameter(Logger logger,
                             ListenersFactory listenersFactory,
                             @Assisted ParameterData data) {
        super(logger, listenersFactory, data);
    }

    @Override
    protected NoChildrenProxyObject createChildInstance(NoChildrenData noChildrenData) {
        return null;
    }
}
