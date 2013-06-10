package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;
import com.intuso.housemate.object.proxy.ProxyOption;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/08/12
 * Time: 00:03
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyOption extends ProxyOption<
            GWTResources<GWTProxyFactory.List<SubTypeWrappable, GWTProxySubType>>,
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SubTypeWrappable, GWTProxySubType>>,
            GWTProxySubType,
            GWTProxyList<SubTypeWrappable, GWTProxySubType>,
            GWTProxyOption> {
    public GWTProxyOption(GWTResources<GWTProxyFactory.List<SubTypeWrappable, GWTProxySubType>> resources,
                          GWTResources<? extends HousemateObjectFactory<GWTResources<?>, SubTypeWrappable, GWTProxySubType>> subResources,
                          OptionWrappable wrappable) {
        super(resources, subResources, wrappable);
    }
}
