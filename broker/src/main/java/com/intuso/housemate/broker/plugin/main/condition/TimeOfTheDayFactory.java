package com.intuso.housemate.broker.plugin.main.condition;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerRealConditionOwner;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.real.impl.type.TimeType;
import com.intuso.housemate.plugin.api.BrokerConditionFactory;

/**
 */
public class TimeOfTheDayFactory implements BrokerConditionFactory<TimeOfTheDay> {

    private final TimeType timeType;

    @Inject
    public TimeOfTheDayFactory(TimeType timeType) {
        this.timeType = timeType;
    }

    @Override
    public String getTypeId() {
        return "time-of-the-day";
    }

    @Override
    public String getTypeName() {
        return "Time of the day";
    }

    @Override
    public String getTypeDescription() {
        return "Checks if the current time is within a set range";
    }

    @Override
    public TimeOfTheDay create(BrokerRealResources resources, String id, String name, String description,
                                  BrokerRealConditionOwner owner) throws HousemateException {
        return new TimeOfTheDay(resources, id, name, description, owner, timeType);
    }
}
