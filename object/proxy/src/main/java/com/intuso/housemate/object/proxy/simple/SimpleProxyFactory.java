package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.parameter.ParameterFactory;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.api.object.automation.AutomationFactory;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.command.CommandFactory;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.condition.ConditionFactory;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.task.TaskFactory;
import com.intuso.housemate.api.object.task.TaskWrappable;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.list.ListFactory;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.OptionFactory;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.property.PropertyFactory;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeFactory;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;
import com.intuso.housemate.api.object.type.TypeFactory;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.api.object.user.UserFactory;
import com.intuso.housemate.api.object.user.UserWrappable;
import com.intuso.housemate.api.object.value.ValueFactory;
import com.intuso.housemate.api.object.value.ValueWrappable;
import com.intuso.housemate.object.proxy.NoChildrenProxyObjectFactory;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyResources;

/**
 * Container class for factories of all the simple proxy object implementations.
 *
 * @see SimpleProxyObject
 */
public class SimpleProxyFactory {

    private final static All allFactory = new All();
    private final static Automation automationFactory = new Automation();
    private final static Command commandFactory = new Command();
    private final static Condition conditionFactory = new Condition();
    private final static Device deviceFactory = new Device();
    private final static GenericList listFactory = new GenericList();
    private final static Option optionFactory = new Option();
    private final static Parameter parameterFactory = new Parameter();
    private final static Property propertyFactory = new Property();
    private final static SubType subTypeFactory = new SubType();
    private final static Task taskFactory = new Task();
    private final static Type typeFactory = new Type();
    private final static User userFactory = new User();
    private final static Value valueFactory = new Value();

    public static class All implements HousemateObjectFactory<ProxyResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> {
        @Override
        public ProxyObject<?, ?, ?, ?, ?, ?, ?> create(ProxyResources<?> resources, HousemateObjectWrappable<?> wrappable) throws HousemateException {
            if(wrappable instanceof ParameterWrappable)
                return parameterFactory.create(resources, (ParameterWrappable) wrappable);
            else if(wrappable instanceof CommandWrappable)
                return commandFactory.create(resources, (CommandWrappable) wrappable);
            else if(wrappable instanceof ConditionWrappable)
                return conditionFactory.create(resources, (ConditionWrappable) wrappable);
            else if(wrappable instanceof UserWrappable)
                return userFactory.create(resources, (UserWrappable) wrappable);
            else if(wrappable instanceof TaskWrappable)
                return taskFactory.create(resources, (TaskWrappable) wrappable);
            else if(wrappable instanceof DeviceWrappable)
                return deviceFactory.create(resources, (DeviceWrappable) wrappable);
            else if(wrappable instanceof ListWrappable)
                return listFactory.create(resources, (ListWrappable<HousemateObjectWrappable<?>>) wrappable);
            else if(wrappable instanceof OptionWrappable)
                return optionFactory.create(resources, (OptionWrappable) wrappable);
            else if(wrappable instanceof PropertyWrappable)
                return propertyFactory.create(resources, (PropertyWrappable) wrappable);
            else if(wrappable instanceof AutomationWrappable)
                return automationFactory.create(resources, (AutomationWrappable) wrappable);
            else if(wrappable instanceof SubTypeWrappable)
                return subTypeFactory.create(resources, (SubTypeWrappable) wrappable);
            else if(wrappable instanceof TypeWrappable)
                return typeFactory.create(resources, (TypeWrappable) wrappable);
            else if(wrappable instanceof ValueWrappable)
                return valueFactory.create(resources, (ValueWrappable) wrappable);
            else
                throw new HousemateException("Don't know how to create an object from " + wrappable.getClass().getName());
        }
    }

    public static class Automation implements AutomationFactory<
            ProxyResources<?>,
            SimpleProxyObject.Automation> {
        @Override
        public SimpleProxyObject.Automation create(ProxyResources<?> resources, AutomationWrappable wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Automation(r, resources, wrappable);
        }
    }

    public static class Command implements CommandFactory<ProxyResources<?>, SimpleProxyObject.Command> {
        @Override
        public SimpleProxyObject.Command create(ProxyResources<?> resources, CommandWrappable wrappable) throws HousemateException {
            ProxyResources<List<ParameterWrappable, SimpleProxyObject.Parameter>> r = changeFactoryType(resources, new List<ParameterWrappable, SimpleProxyObject.Parameter>());
            ProxyResources<Parameter> sr = changeFactoryType(resources, parameterFactory);
            return new SimpleProxyObject.Command(r, sr, wrappable);
        }
    }

    public static class Condition implements ConditionFactory<ProxyResources<?>, SimpleProxyObject.Condition> {
        @Override
        public SimpleProxyObject.Condition create(ProxyResources<?> resources, ConditionWrappable wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Condition(r, resources, wrappable);
        }
    }

    public static class Device implements DeviceFactory<ProxyResources<?>, SimpleProxyObject.Device> {
        @Override
        public SimpleProxyObject.Device create(ProxyResources<?> resources, DeviceWrappable wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Device(r, resources, wrappable);
        }
    }

    public static class GenericList implements ListFactory<ProxyResources<?>, HousemateObjectWrappable<?>,
            ProxyObject<?, ?, ?, ?, ?, ?, ?>,
            SimpleProxyObject.List<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> {

        @Override
        public SimpleProxyObject.List<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>> create(ProxyResources<?> resources, ListWrappable<HousemateObjectWrappable<?>> wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.List<HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>(r, resources, wrappable);
        }
    }

    public static class List<
                SWBL extends HousemateObjectWrappable<?>,
                SWR extends ProxyObject<?, ?, ? extends SWBL, ?, ?, ?, ?>>
            implements ListFactory<ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, SWBL, SWR>>, SWBL, SWR, SimpleProxyObject.List<SWBL, SWR>> {

        @Override
        public SimpleProxyObject.List<SWBL, SWR> create(ProxyResources<? extends HousemateObjectFactory<ProxyResources<?>, SWBL, SWR>> resources,
                                                        ListWrappable<SWBL> wrappable) throws HousemateException {
            return new SimpleProxyObject.List<SWBL, SWR>(resources, resources, wrappable);
        }
    }

    public static class Option implements OptionFactory<ProxyResources<?>, SimpleProxyObject.Option> {
        @Override
        public SimpleProxyObject.Option create(ProxyResources<?> resources, OptionWrappable wrappable) throws HousemateException {
            ProxyResources<List<SubTypeWrappable, SimpleProxyObject.SubType>> r = changeFactoryType(resources,
                    new List<SubTypeWrappable, SimpleProxyObject.SubType>());
            ProxyResources<SubType> sr = changeFactoryType(resources, subTypeFactory);
            return new SimpleProxyObject.Option(r, sr, wrappable);
        }
    }

    public static class Parameter implements ParameterFactory<ProxyResources<?>, SimpleProxyObject.Parameter> {
        @Override
        public SimpleProxyObject.Parameter create(ProxyResources<?> resources, ParameterWrappable wrappable) throws HousemateException {
            return new SimpleProxyObject.Parameter(noFactoryType(resources), wrappable);
        }
    }

    public static class Property implements PropertyFactory<ProxyResources<?>, SimpleProxyObject.Property> {
        @Override
        public SimpleProxyObject.Property create(ProxyResources<?> resources, PropertyWrappable wrappable) throws HousemateException {
            ProxyResources<Command> r = changeFactoryType(resources, commandFactory);
            ProxyResources<List<ParameterWrappable, SimpleProxyObject.Parameter>> sr = changeFactoryType(resources, new List<ParameterWrappable, SimpleProxyObject.Parameter>());
            return new SimpleProxyObject.Property(r, sr, wrappable);
        }
    }

    public static class SubType implements SubTypeFactory<ProxyResources<?>, SimpleProxyObject.SubType> {
        @Override
        public SimpleProxyObject.SubType create(ProxyResources<?> resources, SubTypeWrappable wrappable) throws HousemateException {
            return new SimpleProxyObject.SubType(noFactoryType(resources), wrappable);
        }
    }

    public static class Task implements TaskFactory<ProxyResources<?>, SimpleProxyObject.Task> {
        @Override
        public SimpleProxyObject.Task create(ProxyResources<?> resources, TaskWrappable wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Task(r, resources, wrappable);
        }
    }

    public static class Type implements TypeFactory<ProxyResources<?>, SimpleProxyObject.Type> {
        @Override
        public SimpleProxyObject.Type create(ProxyResources<?> resources,
                                             TypeWrappable<?> wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.Type(r, resources, wrappable);
        }
    }

    public static class User implements UserFactory<ProxyResources<?>, SimpleProxyObject.User> {
        @Override
        public SimpleProxyObject.User create(ProxyResources<?> resources, UserWrappable wrappable) throws HousemateException {
            ProxyResources<All> r = changeFactoryType(resources, allFactory);
            return new SimpleProxyObject.User(r, resources, wrappable);
        }
    }

    public static class Value implements ValueFactory<ProxyResources<?>, SimpleProxyObject.Value> {
        @Override
        public SimpleProxyObject.Value create(ProxyResources<?> resources, ValueWrappable wrappable) throws HousemateException {
            return new SimpleProxyObject.Value(noFactoryType(resources), wrappable);
        }
    }

    public static
            <NF extends HousemateObjectFactory<? extends ProxyResources<?>, ?, ? extends ProxyObject<?, ?, ?, ?, ?, ?, ?>>>
            ProxyResources<NF> changeFactoryType(ProxyResources<?> resources, NF newFactory) {
        return new ProxyResources<NF>(resources.getLog(), resources.getProperties(), resources.getRouter(),
                newFactory, resources.getRegexMatcherFactory());
    }

    public static ProxyResources<NoChildrenProxyObjectFactory> noFactoryType(ProxyResources<?> resources) {
        return changeFactoryType(resources, null);
    }
}
