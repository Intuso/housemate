package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxyParameter;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 09/02/13
 * Time: 08:45
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyParameter extends ProxyParameter<GWTResources<NoChildrenProxyObjectFactory>, GWTProxyType,
        GWTProxyParameter> {
    public GWTProxyParameter(GWTResources<NoChildrenProxyObjectFactory> resources, ParameterWrappable wrappable) {
        super(resources, wrappable);
    }
}
