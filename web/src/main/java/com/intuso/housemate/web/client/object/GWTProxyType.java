package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyType;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/08/12
 * Time: 00:03
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyType extends ProxyType<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            TypeWrappable<HousemateObjectWrappable<?>>,
            HousemateObjectWrappable<?>,
            ProxyObject<?, ?, ?, ?, ?, ?, ?>,
            GWTProxyType> {
    public GWTProxyType(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                        GWTResources<?> subResources,
                        TypeWrappable wrappable) {
        super(resources, subResources, wrappable);
    }
}