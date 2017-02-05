package com.intuso.housemate.client.api.bridge.v1_0.driver;

import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import org.slf4j.Logger;

/**
 * Created by tomc on 05/11/15.
 */
public class HardwareDriverBridge implements com.intuso.housemate.client.api.internal.driver.HardwareDriver {

    private final HardwareDriver hardwareDriver;

    public HardwareDriverBridge(HardwareDriver hardwareDriver) {
        this.hardwareDriver = hardwareDriver;
    }

    public HardwareDriver getHardwareDriver() {
        return hardwareDriver;
    }

    @Override
    public void init(Logger logger, Callback callback, Iterable<String> deviceIds) {
        hardwareDriver.init(logger, new CallbackBridge(callback), deviceIds);
    }

    @Override
    public void uninit() {
        hardwareDriver.uninit();
    }

    public static class CallbackBridge implements HardwareDriver.Callback {

        private final Callback callback;

        public CallbackBridge(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void setError(String error) {
            callback.setError(error);
        }

        @Override
        public void addConnectedDevice(String id, String name, String description, Object object) {
            callback.addConnectedDevice(id, name, description, object);
        }

        @Override
        public void removeConnectedDevice(Object object) {
            callback.removeConnectedDevice(object);
        }
    }
}
