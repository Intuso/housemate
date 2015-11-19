package com.intuso.housemate.client.real.api.internal.device.feature;

import com.intuso.housemate.client.real.api.internal.annotations.Feature;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.annotations.Value;
import com.intuso.housemate.client.real.api.internal.annotations.Values;

/**
 * Interface to mark real devices that provide stateful power control
 */
@Feature
@TypeInfo(id = "temperature", name = "Temperature", description = "Temperature")
public interface TemperatureSensor {

    @Values
    interface TemperatureValues {

        /**
         * Callback to set the current temperature of the device
         * @param temperature the current temperature
         */
        @Value("double")
        @TypeInfo(id = "temperature", name = "Temperature", description = "The current temperature")
        void setTemperature(double temperature);
    }
}
