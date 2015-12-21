package com.intuso.housemate.client.real.impl.internal.factory.device;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.impl.internal.factory.FactoryType;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 19/03/15.
 */
public class DeviceFactoryType extends FactoryType<DeviceDriver.Factory<?>> {

    public final static String TYPE_ID = "device-factory";
    public final static String TYPE_NAME = "Device Factory";
    public final static String TYPE_DESCRIPTION = "Available types for new devices";

    private final static Logger logger = LoggerFactory.getLogger(DeviceFactoryType.class);

    @Inject
    protected DeviceFactoryType(ListenersFactory listenersFactory) {
        super(logger, listenersFactory, TYPE_ID, TYPE_NAME, TYPE_DESCRIPTION);
    }
}
