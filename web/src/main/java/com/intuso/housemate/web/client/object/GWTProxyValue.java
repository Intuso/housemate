package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxyValue;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyValue extends ProxyValue<GWTResources<NoChildrenProxyObjectFactory>, GWTProxyType, GWTProxyValue> {
    public GWTProxyValue(GWTResources<NoChildrenProxyObjectFactory> resources, ValueData value) {
        super(resources, value);
    }
}
