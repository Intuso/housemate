package com.intuso.housemate.api.object;

import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.proxy.simple.SimpleProxyResources;
import org.junit.Ignore;

/**
 */
@Ignore
public class TestProxyRoot extends ProxyRootObject<
            SimpleProxyResources<SimpleProxyFactory.All>,
            SimpleProxyResources<?>,
            SimpleProxyObject.User, SimpleProxyObject.List<UserData, SimpleProxyObject.User>,
            SimpleProxyObject.Type, SimpleProxyObject.List<TypeData<?>, SimpleProxyObject.Type>,
            SimpleProxyObject.Device, SimpleProxyObject.List<DeviceData, SimpleProxyObject.Device>,
        SimpleProxyObject.Automation, SimpleProxyObject.List<AutomationData, SimpleProxyObject.Automation>,
            SimpleProxyObject.Command, TestProxyRoot> {

    public TestProxyRoot(SimpleProxyResources<SimpleProxyFactory.All> resources, SimpleProxyResources<?> childResources) {
        super(resources, childResources);
        super.addChild(new SimpleProxyObject.List<TypeData<?>, SimpleProxyObject.Type>(
                SimpleProxyFactory.changeFactoryType(getResources(), new SimpleProxyFactory.Type()), childResources, new ListData(TYPES_ID, TYPES_ID, TYPES_ID)));
        super.addChild(new SimpleProxyObject.List<DeviceData, SimpleProxyObject.Device>(
                SimpleProxyFactory.changeFactoryType(getResources(), new SimpleProxyFactory.Device()), childResources, new ListData(DEVICES_ID, DEVICES_ID, DEVICES_ID)));
        init(null);
    }

    public void addChild(ProxyObject<?, ?, ?, ?, ?, ?, ?> child) {
        removeChild(child.getId());
        super.addChild(child);
        child.init(this);
    }
}
