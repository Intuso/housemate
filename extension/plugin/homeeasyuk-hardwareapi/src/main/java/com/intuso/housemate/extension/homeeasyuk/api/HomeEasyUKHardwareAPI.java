package com.intuso.housemate.extension.homeeasyuk.api;

import com.intuso.housemate.plugin.v1_0.api.annotations.Command;
import com.intuso.housemate.plugin.v1_0.api.annotations.HardwareAPI;
import com.intuso.housemate.plugin.v1_0.api.annotations.Id;
import com.intuso.housemate.plugin.v1_0.api.annotations.Value;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 08/12/16.
 */
@HardwareAPI("homeeasyuk-1.0")
public interface HomeEasyUKHardwareAPI {

    Appliance appliance(int houseId, byte unitId);
    
    interface Appliance {

        @Value
        @Id("is-on")
        boolean isOn();

        @Command
        @Id("on")
        void turnOn();

        @Command()
        @Id("off")
        void turnOff();

        ListenerRegistration addListener(Listener listener);

        interface Listener extends com.intuso.utilities.listener.Listener {

            @Value
            @Id("is-on")
            void on(boolean isOn);
        }
    }
}
