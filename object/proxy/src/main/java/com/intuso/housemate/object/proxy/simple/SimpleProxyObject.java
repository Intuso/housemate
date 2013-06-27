package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxyParameter;
import com.intuso.housemate.object.proxy.ProxyAutomation;
import com.intuso.housemate.object.proxy.ProxyCommand;
import com.intuso.housemate.object.proxy.ProxyCondition;
import com.intuso.housemate.object.proxy.ProxyTask;
import com.intuso.housemate.object.proxy.ProxyDevice;
import com.intuso.housemate.object.proxy.ProxyList;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyOption;
import com.intuso.housemate.object.proxy.ProxyProperty;
import com.intuso.housemate.object.proxy.ProxyResources;
import com.intuso.housemate.object.proxy.ProxyRootObject;
import com.intuso.housemate.object.proxy.ProxySubType;
import com.intuso.housemate.object.proxy.ProxyType;
import com.intuso.housemate.object.proxy.ProxyUser;
import com.intuso.housemate.object.proxy.ProxyValue;

/**
 * Container class for a simple implementation of all the proxy objects
 */
public class SimpleProxyObject {

    public final static class Automation extends ProxyAutomation<
            ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            ProxyResources<?>,
            Command,
            Value,
            Condition,
            List<ConditionWrappable, Condition>, Task, List<TaskWrappable, Task>, Automation> {
        public Automation(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                          ProxyResources<?> childResources,
                          AutomationWrappable data) {
            super(resources, childResources, data);
        }
    }

    public final static class Command extends ProxyCommand<ProxyResources<SimpleProxyFactory.List<ParameterWrappable, Parameter>>,
                ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, ParameterWrappable, Parameter>>,
            Parameter,
                List<ParameterWrappable, Parameter>,
                Command> {
        public Command(ProxyResources<SimpleProxyFactory.List<ParameterWrappable, Parameter>> resources,
                       ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, ParameterWrappable, Parameter>> childResources,
                       CommandWrappable data) {
            super(resources, childResources, data);
        }
    }

    public final static class Condition extends ProxyCondition<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>,
                Value,
                List<PropertyWrappable, Property>,
                Command,
                Condition,
                List<ConditionWrappable, Condition>> {
        public Condition(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                         ProxyResources<?> childResources,
                         ConditionWrappable data) {
            super(resources, childResources, data);
        }
    }

    public final static class Device extends ProxyDevice<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>,
                Command,
                List<CommandWrappable, Command>, Value, List<ValueWrappable, Value>, Property,
                List<PropertyWrappable, Property>, Device> {
        public Device(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                      ProxyResources<?> childResources,
                      DeviceWrappable data) {
            super(resources, childResources, data);
        }
    }

    public final static class List<WBL extends HousemateObjectWrappable<?>, WR extends ProxyObject<?, ?, ? extends WBL, ?, ?, ?, ?>>
            extends ProxyList<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, WBL, WR>>,
                ProxyResources<?>,
                WBL,
                WR,
                List<WBL, WR>> {
        public List(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, WBL, WR>> resources,
                    ProxyResources<?> childResources,
                    ListWrappable<WBL> listWrappable) {
            super(resources, childResources, listWrappable);
        }
    }

    public final static class Option extends ProxyOption<
            ProxyResources<SimpleProxyFactory.List<SubTypeWrappable, SubType>>,
            ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, SubTypeWrappable, SubType>>,
            SubType,
            List<SubTypeWrappable, SubType>,
            Option> {
        public Option(ProxyResources<SimpleProxyFactory.List<SubTypeWrappable, SubType>> resources,
                      ProxyResources<SimpleProxyFactory.SubType> childResources,
                      OptionWrappable data) {
            super(resources, childResources, data);
        }
    }

    public final static class Parameter extends ProxyParameter<ProxyResources<NoChildrenProxyObjectFactory>, Type, Parameter> {
        public Parameter(ProxyResources<NoChildrenProxyObjectFactory> resources, ParameterWrappable data) {
            super(resources, data);
        }
    }

    public final static class Property extends ProxyProperty<ProxyResources<SimpleProxyFactory.Command>,
            ProxyResources<?>,
            Type,
            Command,
            Property> {
        public Property(ProxyResources<SimpleProxyFactory.Command> resources,
                        ProxyResources<SimpleProxyFactory.List<ParameterWrappable, Parameter>> childResources,
                        PropertyWrappable data) {
            super(resources, childResources, data);
        }
    }

    public final static class Root extends ProxyRootObject<
            ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            ProxyResources<?>,
            User, List<UserWrappable, User>,
            Type, List<TypeWrappable<?>, Type>,
            Device, List<DeviceWrappable, Device>,
            Automation, List<AutomationWrappable, Automation>,
            Command, Root> {
        public Root(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> childResources) {
            super(resources, childResources);
        }
    }

    public final static class SubType extends ProxySubType<ProxyResources<NoChildrenProxyObjectFactory>, Type, SubType> {
        public SubType(ProxyResources<NoChildrenProxyObjectFactory> resources, SubTypeWrappable data) {
            super(resources, data);
        }
    }

    public final static class Task extends ProxyTask<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            ProxyResources<?>,
            Value,
            List<PropertyWrappable, Property>,
            Task> {
        public Task(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> childResources,
                    TaskWrappable data) {
            super(resources, childResources, data);
        }
    }

    public final static class Type extends ProxyType<
                ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
                ProxyResources<?>,
                TypeWrappable<HousemateObjectWrappable<?>>,
                HousemateObjectWrappable<?>,
                ProxyObject<?, ?, ?, ?, ?, ?, ?>,
                Type> {
        public Type(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> childResources,
                    TypeWrappable data) {
            super(resources, childResources, data);
        }
    }

    public final static class User extends ProxyUser<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            ProxyResources<?>, Command, User> {
        public User(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                    ProxyResources<?> childResources,
                    UserWrappable data) {
            super(resources, childResources, data);
        }
    }

    public final static class Value extends ProxyValue<ProxyResources<NoChildrenProxyObjectFactory>, Type, Value> {
        public Value(ProxyResources<NoChildrenProxyObjectFactory> resources, ValueWrappable value) {
            super(resources, value);
        }
    }
}
