package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;
import com.intuso.housemate.object.proxy.ProxyDevice;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTResources;

/**
 */
public class GWTProxyDevice
        extends ProxyDevice<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            GWTProxyCommand,
            GWTProxyList<CommandWrappable, GWTProxyCommand>,
            GWTProxyValue,
            GWTProxyList<ValueWrappable, GWTProxyValue>,
            GWTProxyProperty,
            GWTProxyList<PropertyWrappable, GWTProxyProperty>,
            GWTProxyDevice> {
    public GWTProxyDevice(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                          GWTResources<?> childResources,
                          DeviceWrappable data) {
        super(resources, childResources, data);
    }


}
