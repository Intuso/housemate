package com.intuso.housemate.plugin.rfxcom.lighting2;

import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.lighting2.Lighting2;

/**
 * Created by tomc on 02/12/15.
 */
public class Lighting2ACHandler extends Lighting2Handler {

    static Lighting2ACHandler INSTANCE;

    public Lighting2ACHandler(Log log, RFXtrx rfxtrx, RealDevice.Container deviceContainer) {
        super(log, Lighting2.forAC(rfxtrx), deviceContainer);
        INSTANCE = this;
    }

    @Override
    public String getDeviceName(int houseId, byte unitCode) {
        return "Lighting2 AC " + houseId + "/" + (int)unitCode;
    }

    @Override
    public String getDriverId() {
        return "rfxcom-lighting2-ac";
    }
}