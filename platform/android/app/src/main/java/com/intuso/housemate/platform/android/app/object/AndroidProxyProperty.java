package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.object.ProxyProperty;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyProperty extends ProxyProperty<AndroidProxyType, AndroidProxyCommand, AndroidProxyProperty> {

    /**
     * @param logger  {@inheritDoc}
     */
    protected AndroidProxyProperty(Logger logger, ManagedCollectionFactory managedCollectionFactory, AndroidObjectFactories factories) {
        super(logger, managedCollectionFactory, factories.command());
    }
}
