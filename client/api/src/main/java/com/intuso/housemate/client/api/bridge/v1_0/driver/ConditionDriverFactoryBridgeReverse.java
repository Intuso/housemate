package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.driver.ConditionDriver;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by tomc on 05/11/15.
 */
public class ConditionDriverFactoryBridgeReverse implements ConditionDriver.Factory<ConditionDriver> {

    private final com.intuso.housemate.client.api.internal.driver.ConditionDriver.Factory<?> factory;
    private final ConditionDriverMapper conditionDriverMapper;

    @Inject
    public ConditionDriverFactoryBridgeReverse(@Assisted com.intuso.housemate.client.api.internal.driver.ConditionDriver.Factory<?> factory,
                                               ConditionDriverMapper conditionDriverMapper) {
        this.factory = factory;
        this.conditionDriverMapper = conditionDriverMapper;
    }

    public com.intuso.housemate.client.api.internal.driver.ConditionDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public ConditionDriver create(Logger logger, ConditionDriver.Callback callback) {
        return conditionDriverMapper.map(factory.create(logger, new ConditionDriverBridgeReverse.CallbackBridge(callback)));
    }

    public interface Factory {
        ConditionDriverFactoryBridgeReverse create(com.intuso.housemate.client.api.internal.driver.ConditionDriver.Factory<?> conditionDriverFactory);
    }
}
