package com.intuso.housemate.object.broker;

import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.object.broker.proxy.BrokerProxyPrimaryObject;
import com.intuso.housemate.object.broker.real.BrokerRealCommand;
import com.intuso.housemate.object.broker.real.BrokerRealList;
import com.intuso.housemate.object.broker.real.BrokerRealRule;
import com.intuso.housemate.object.broker.real.BrokerRealUser;
import com.intuso.housemate.object.broker.real.condition.BrokerRealCondition;
import com.intuso.housemate.object.broker.real.consequence.BrokerRealConsequence;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 22/05/13
 * Time: 23:39
 * To change this template use File | Settings | File Templates.
 */
public interface LifecycleHandler {
    BrokerRealCommand createAddUserCommand(BrokerRealList<UserWrappable, BrokerRealUser> users);
    BrokerRealCommand createRemoveUserCommand(BrokerRealUser user);
    RealCommand createAddDeviceCommand(RealList<DeviceWrappable, RealDevice> devices);
    BrokerRealCommand createAddRuleCommand(BrokerRealList<RuleWrappable, BrokerRealRule> rules);
    void ruleRemoved(String[] path);
    <PO extends BrokerProxyPrimaryObject<?, ?, ?>> BrokerRealCommand createRemovePrimaryObjectCommand(
            Command<?, ?> originalCommand, PO source, BrokerProxyPrimaryObject.Remover<PO> remover);
    BrokerRealCommand createAddConditionCommand(BrokerRealList<ConditionWrappable, BrokerRealCondition> conditions);
    BrokerRealCommand createAddSatisfiedConsequenceCommand(BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> consequences);
    BrokerRealCommand createAddUnsatisfiedConsequenceCommand(BrokerRealList<ConsequenceWrappable, BrokerRealConsequence> consequences);
}