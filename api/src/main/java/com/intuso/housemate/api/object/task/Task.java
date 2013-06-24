package com.intuso.housemate.api.object.task;

import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.property.HasProperties;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.value.Value;

/**
 * @param <EXECUTING_VALUE> the type of the executing value
 * @param <ERROR_VALUE> the type of the error value
 * @param <PROPERTIES> the type of the properties list
 * @param <TASK> the type of the task
 */
public interface Task<
            EXECUTING_VALUE extends Value<?, ?>,
            ERROR_VALUE extends Value<?, ?>,
            PROPERTIES extends List<? extends Property<?, ?, ?>>,
            TASK extends Task<EXECUTING_VALUE, ERROR_VALUE, PROPERTIES, TASK>>
        extends BaseObject<TaskListener<? super TASK>>, HasProperties<PROPERTIES> {

    public final static String EXECUTING_TYPE = "executing";
    public final static String ERROR_ID = "error";
    public final static String PROPERTIES_ID = "properties";

    /**
     * Gets the error value object
     * @return the error value object
     */
    public ERROR_VALUE getErrorValue();

    /**
     * Gets the error value
     * @return the error value
     */
    public String getError();

    /**
     * Gets the executing value object
     * @return the executing value object
     */
    public EXECUTING_VALUE getExecutingValue();

    /**
     * Gets the executing value
     * @return the executing value
     */
    public boolean isExecuting();
}