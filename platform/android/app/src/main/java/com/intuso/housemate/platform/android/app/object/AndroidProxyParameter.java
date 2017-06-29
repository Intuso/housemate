package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.messaging.api.Receiver;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyParameter;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/02/14
 * Time: 09:28
 * To change this template use File | Settings | File Templates.
 */
public class AndroidProxyParameter extends ProxyParameter<AndroidProxyType, AndroidProxyParameter> {

    /**
     * @param logger  {@inheritDoc}
     */
    public AndroidProxyParameter(Logger logger, String name, ManagedCollectionFactory managedCollectionFactory, Receiver.Factory receiverFactory) {
        super(logger, name, managedCollectionFactory, receiverFactory);
    }
}
