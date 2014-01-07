package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.real.ServerNonLeafCondition;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 * Condition which is true iff the sub-condition is false
 *
 */
@FactoryInformation(id = "not", name = "Not", description = "Negation of the child condition")
public class Not extends ServerNonLeafCondition {
	
	/**
	 * Create a new Not condition
     * @param log
	 * @param name
	 * @throws HousemateException
	 */
    @Inject
	public Not(Log log, String id, String name, String description, ServerRealConditionOwner owner,
               LifecycleHandler lifecycleHandler) throws HousemateException {
		super(log, id, name, description, owner, lifecycleHandler);

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
    protected boolean checkIfSatisfied(Map<ServerRealCondition, Boolean> satisfiedMap) {
        return !satisfiedMap.values().iterator().next();
    }
}