package com.intuso.housemate.broker.plugin.main.condition;

import com.google.inject.Inject;
import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.object.broker.real.BrokerRealConditionOwner;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerNonLeafCondition;
import com.intuso.housemate.object.broker.real.BrokerRealCondition;

import java.util.Map;

/**
 * Condition which is true iff the sub-condition is false
 *
 */
@FactoryInformation(id = "not", name = "Not", description = "Negation of the child condition")
public class Not extends BrokerNonLeafCondition {
	
	/**
	 * Create a new Not condition
     * @param resources
	 * @param name
	 * @throws HousemateException
	 */
    @Inject
	public Not(BrokerRealResources resources, String id, String name, String description, BrokerRealConditionOwner owner) throws HousemateException {
		super(resources, id, name, description, owner);

        // todo move this check somewhere else
        /*if(getConditions().size() > 1) {
            getLog().e("There are multiple sub-conditions for the Not condition \"" + getId() + "\"");
            throw new HousemateException("There are multiple sub-conditions for the Not condition \"" + getId() + "\"");
        } else if(getConditions().size() == 0) {
            getLog().e("There are no sub-conditions for the Not condition \"" + getId() + "\"");
            throw new HousemateException("There are no sub-conditions for the Not condition \"" + getId() + "\"");
        }*/
    }

    @Override
    protected boolean checkIfSatisfied(Map<BrokerRealCondition, Boolean> satisfiedMap) {
        return !satisfiedMap.values().iterator().next();
    }
}