package com.intuso.housemate.web.client.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.proxy.api.LoggerUtil;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyList;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.comms.v1_0.api.payload.HousemateData;
import com.intuso.housemate.comms.v1_0.api.payload.ListData;
import com.intuso.housemate.web.client.ioc.GWTGinjector;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 */
public class GWTProxyList<CHILD_DATA extends HousemateData<?>,
            CHILD extends ProxyObject<? extends CHILD_DATA, ?, ?, ?, ?>>
        extends ProxyList<CHILD_DATA, CHILD, GWTProxyList<CHILD_DATA, CHILD>> {

    private final GWTGinjector injector;

    @Inject
    public GWTProxyList(Logger logger,
                        ListenersFactory listenersFactory,
                        GWTGinjector injector,
                        @Assisted ListData<CHILD_DATA> data) {
        super(logger, listenersFactory, data);
        this.injector = injector;
    }

    @Override
    protected CHILD createChild(CHILD_DATA data) {
        return (CHILD) injector.getObjectFactory().create(LoggerUtil.child(getLogger(), data.getId()), data);
    }
}
