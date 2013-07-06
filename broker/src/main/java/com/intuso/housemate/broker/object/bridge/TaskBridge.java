package com.intuso.housemate.broker.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.task.Task;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.api.object.task.TaskListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;

import javax.annotation.Nullable;
import java.util.List;

/**
 */
public class TaskBridge
        extends BridgeObject<TaskData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, TaskBridge,
        TaskListener<? super TaskBridge>>
        implements Task<ValueBridge, ValueBridge,
                            ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>,
        TaskBridge> {

    private ValueBridge executingValue;
    private ValueBridge errorValue;
    private ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> propertyList;

    public TaskBridge(BrokerBridgeResources resources, Task<?, ?, ?, ?> task) {
        super(resources, new TaskData(task.getId(), task.getName(), task.getDescription()));
        executingValue = new ValueBridge(resources,task.getExecutingValue());
        errorValue = new ValueBridge(resources,task.getErrorValue());
        propertyList = new ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge>(resources, task.getProperties(), new PropertyBridge.Converter(resources));
        addWrapper(executingValue);
        addWrapper(errorValue);
        addWrapper(propertyList);
    }

    @Override
    public ValueBridge getErrorValue() {
        return errorValue;
    }

    @Override
    public String getError() {
        List<String> errors = RealType.deserialiseAll(StringType.SERIALISER, errorValue.getTypeInstances());
        return errors != null && errors.size() > 0 ? errors.get(0) : null;
    }

    @Override
    public ValueBridge getExecutingValue() {
        return executingValue;
    }

    @Override
    public boolean isExecuting() {
        List<Boolean> executings = RealType.deserialiseAll(BooleanType.SERIALISER, executingValue.getTypeInstances());
        return executings != null && executings.size() > 0 && executings.get(0) != null ? executings.get(0) : false;
    }

    @Override
    public ListBridge<PropertyData, Property<?, ?, ?>, PropertyBridge> getProperties() {
        return propertyList;
    }

    public static class Converter implements Function<Task<?, ?, ?, ?>, TaskBridge> {

        private final BrokerBridgeResources resources;

        public Converter(BrokerBridgeResources resources) {
            this.resources = resources;
        }

        @Override
        public TaskBridge apply(@Nullable Task<?, ?, ?, ?> command) {
            return new TaskBridge(resources, command);
        }
    }
}
