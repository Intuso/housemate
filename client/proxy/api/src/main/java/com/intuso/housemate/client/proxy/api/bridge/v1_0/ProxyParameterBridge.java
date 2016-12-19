package com.intuso.housemate.client.proxy.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.ParameterMapper;
import com.intuso.housemate.client.api.internal.object.Parameter;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 28/11/16.
 */
public class ProxyParameterBridge
        extends ProxyObjectBridge<com.intuso.housemate.client.v1_0.api.object.Parameter.Data, Parameter.Data, Parameter.Listener<? super ProxyParameterBridge>>
        implements Parameter<ProxyTypeBridge, ProxyParameterBridge> {

    @Inject
    protected ProxyParameterBridge(@Assisted Logger logger,
                                   ParameterMapper parameterMapper,
                                   ListenersFactory listenersFactory) {
        super(logger, com.intuso.housemate.client.v1_0.api.object.Parameter.Data.class, parameterMapper, listenersFactory);
    }

    @Override
    public ProxyTypeBridge getType() {
        return null; // todo get the type from somewhere
    }
}
