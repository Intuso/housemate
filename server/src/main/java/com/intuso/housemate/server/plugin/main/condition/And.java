package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.intuso.housemate.annotations.plugin.FactoryInformation;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.real.ServerNonLeafCondition;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.utilities.log.Log;

import java.util.Collection;
import java.util.Map;

/**
 * Part of a condition tree whose node is true iff all child nodes are true
 *
 */
@FactoryInformation(id = "and", name = "And", description = "True only when all child conditions are true")
public class And extends ServerNonLeafCondition {

	/**
	 * Create a new And condition
     * @param log
	 * @param name
	 * @throws HousemateException
	 */
    @Inject
	public And(Log log, String id, String name, String description, ServerRealConditionOwner owner,
               LifecycleHandler lifecycleHandler) throws HousemateException {
		super(log, id, name, description, owner, lifecycleHandler);
    }
	
	/**
	 * Check if all of the sub-conditions are satisfied or not
	 * @return true iff all of the sub-conditions are satisfied
	 */
    @Override
	protected final boolean checkIfSatisfied(Map<ServerRealCondition, Boolean> satisfiedMap) {
		// get all the satisfied values
		Collection<Boolean> satisfied = satisfiedMap.values();
		
		// go through all of them
		for(Boolean b : satisfied) {
			// if it's false then we're false
			if(!b) {
				getLog().d("One sub-condition is unsatisfied");
				return false;
			}
		}
		
		getLog().d("All sub-conditions are satisfied");
		// we only get here if all sub-conditions are satisfied
		return true;
	}
}