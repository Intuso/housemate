package com.intuso.housemate.client.real.api.internal.driver;

/**
 * Created by tomc on 30/09/15.
 */
public interface DeviceDriver {

    void start();
    void stop();

    interface Callback {
        void setError(String error);
    }

    interface Factory<DRIVER extends DeviceDriver> {
        DRIVER create(Callback callback);
    }
}