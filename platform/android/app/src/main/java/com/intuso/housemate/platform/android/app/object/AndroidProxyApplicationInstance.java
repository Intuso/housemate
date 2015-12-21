package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyApplicationInstance;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.ApplicationInstanceData;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyApplicationInstance extends ProxyApplicationInstance<AndroidProxyValue, AndroidProxyCommand,
        AndroidProxyApplicationInstance> {

    private final AndroidProxyFactory factory;

    /**
     * @param logger  {@inheritDoc}
     * @param data {@inheritDoc}
     * @param factory
     */
    protected AndroidProxyApplicationInstance(Logger logger, ListenersFactory listenersFactory, ApplicationInstanceData data, AndroidProxyFactory factory) {
        super(logger, listenersFactory, data);
        this.factory = factory;
    }

    @Override
    protected ProxyObject<?, ?, ?, ?, ?> createChild(HousemateData<?> data) {
        return factory.create(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}
