package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class TaskDriverFactoryBridge implements TaskDriver.Factory<TaskDriver> {

    private final com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver.Factory<?> factory;
    private final TaskDriverMapper taskDriverMapper;

    @Inject
    public TaskDriverFactoryBridge(@Assisted com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver.Factory<?> factory,
                                   TaskDriverMapper taskDriverMapper) {
        this.factory = factory;
        this.taskDriverMapper = taskDriverMapper;
    }

    public com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver.Factory<?> getFactory() {
        return factory;
    }

    @Override
    public TaskDriver create(Logger logger, TaskDriver.Callback callback) {
        return taskDriverMapper.map(factory.create(logger, new CallbackBridge(callback)));
    }

    private class CallbackBridge implements com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver.Callback {

        private final TaskDriver.Callback callback;

        private CallbackBridge(TaskDriver.Callback callback) {
            this.callback = callback;
        }

        @Override
        public void setError(String error) {
            callback.setError(error);
        }
    }

    public interface Factory {
        TaskDriverFactoryBridge create(com.intuso.housemate.client.v1_0.real.api.driver.TaskDriver.Factory<?> taskDriverFactory);
    }
}
