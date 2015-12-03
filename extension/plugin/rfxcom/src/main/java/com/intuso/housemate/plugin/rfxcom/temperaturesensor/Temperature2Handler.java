package com.intuso.housemate.plugin.rfxcom.temperaturesensor;

import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensors;

/**
 * Created by tomc on 02/12/15.
 */
public class Temperature2Handler extends TemperatureHandler {

    static Temperature2Handler INSTANCE;

    public Temperature2Handler(Log log, RFXtrx rfxtrx, RealDevice.Container deviceContainer) {
        super(log, TemperatureSensors.forTemp2(rfxtrx), deviceContainer);
        INSTANCE = this;
    }

    @Override
    public String getNewDeviceName(int sensorId) {
        return "Temperature 2 " + sensorId;
    }

    @Override
    public String getDriverId() {
        return "rfxcom-temperature-2";
    }
}