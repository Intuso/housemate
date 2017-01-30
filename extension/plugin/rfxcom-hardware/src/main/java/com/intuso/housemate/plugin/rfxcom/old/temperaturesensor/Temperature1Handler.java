package com.intuso.housemate.plugin.rfxcom.old.temperaturesensor;

import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.temperaturesensor.TemperatureSensors;
import org.slf4j.Logger;

/**
 * Created by tomc on 02/12/15.
 */
public class Temperature1Handler extends TemperatureHandler {

    static Temperature1Handler INSTANCE;

    // todo use remote hardware in all devices

    public Temperature1Handler(Logger logger, RFXtrx rfxtrx) {
        super(logger, TemperatureSensors.forTemp1(rfxtrx));
        INSTANCE = this;
    }

    @Override
    public String getNewDeviceName(int sensorId) {
        return "Temperature 1 " + sensorId;
    }

    @Override
    public String getDriverId() {
        return "rfxcom-temperature-1";
    }
}