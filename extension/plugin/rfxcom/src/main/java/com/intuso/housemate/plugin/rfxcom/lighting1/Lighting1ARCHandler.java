package com.intuso.housemate.plugin.rfxcom.lighting1;

import com.intuso.housemate.client.v1_0.real.api.RealDevice;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.lighting1.Lighting1;

/**
 * Created by tomc on 02/12/15.
 */
public class Lighting1ARCHandler extends Lighting1Handler {

    static Lighting1ARCHandler INSTANCE;

    public Lighting1ARCHandler(Log log, RFXtrx rfxtrx, RealDevice.Container deviceContainer) {
        super(log, Lighting1.forARC(rfxtrx), deviceContainer);
        INSTANCE = this;
    }

    @Override
    public String getDeviceName(byte houseId, byte unitCode) {
        return "Lighting1 ARC " + houseId + "/" + (int)unitCode;
    }

    @Override
    public String getDriverId() {
        return "rfxcom-lighting1-arc";
    }
}
