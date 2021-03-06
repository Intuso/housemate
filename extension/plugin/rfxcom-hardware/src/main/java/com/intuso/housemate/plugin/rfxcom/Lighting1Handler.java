package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.annotation.Classes;
import com.intuso.housemate.client.v1_0.api.annotation.Component;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.util.lighting1.Lighting1;

import java.io.IOException;
import java.util.Map;

/**
 * Created by tomc on 02/02/17.
 */
public class Lighting1Handler extends Handler implements Lighting1.Callback {

    private final ManagedCollectionFactory managedCollectionFactory;
    private final Lighting1 lighting1;

    private final String idFormat, nameFormat, descriptionFormat;

    private final Map<Byte, Map<Byte, Device>> devices = Maps.newHashMap();

    protected Lighting1Handler(ManagedCollectionFactory managedCollectionFactory,
                               RFXtrx rfxtrx,
                               com.rfxcom.rfxtrx.message.Lighting1.SubType subType,
                               String idPrefix,
                               String idFormat,
                               String nameFormat,
                               String descriptionFormat) {
        super(idPrefix + "-");
        this.managedCollectionFactory = managedCollectionFactory;
        this.lighting1 = new Lighting1(rfxtrx, subType);
        this.idFormat = idPrefix + "-" + idFormat;
        this.nameFormat = nameFormat;
        this.descriptionFormat = descriptionFormat;
    }

    @Override
    public ManagedCollection.Registration initListener() {
        return lighting1.addCallback(this);
    }

    @Override
    public void turnedOn(byte houseCode, byte unitCode) {
        Device device = getOrCreate(houseCode, unitCode);
        if(device != null)
            device.setOn(true);
    }

    @Override
    public void turnedOnAll(byte houseCode) {
        if(devices.containsKey(houseCode))
            for(Device device : devices.get(houseCode).values())
                device.setOn(true);
    }

    @Override
    public void turnedOff(byte houseCode, byte unitCode) {
        Device device = getOrCreate(houseCode, unitCode);
        if(device != null)
            device.setOn(false);
    }

    @Override
    public void turnedOffAll(byte houseCode) {
        if(devices.containsKey(houseCode))
            for(Device device : devices.get(houseCode).values())
                device.setOn(false);
    }

    @Override
    public void dim(byte houseCode, byte unitCode) {
        // todo
    }

    @Override
    public void bright(byte houseCode, byte unitCode) {
        // todo
    }

    @Override
    public void chime(byte houseCode) {
        // todo
    }

    @Override
    void parseIdDetails(String details) {
        addDevice(Byte.parseByte(details.split("-")[0]), Byte.parseByte(details.split("-")[1]));
    }

    private Device getOrCreate(byte houseCode, byte unitCode) {
        if(devices.containsKey(houseCode) && devices.get(houseCode).containsKey(unitCode))
            return devices.get(houseCode).get(unitCode);
        else if(autoCreate)
            return addDevice(houseCode, unitCode);
        return null;
    }

    public Device addDevice(byte houseCode, byte unitCode) {
        Device device = new Device(managedCollectionFactory, houseCode, unitCode);
        if(!devices.containsKey(houseCode))
            devices.put(houseCode, Maps.<Byte, Device>newHashMap());
        devices.get(houseCode).put(unitCode, device);
        hardwareCallback.addDevice(
                idFormat.replaceAll("\\$\\{houseCode\\}", Byte.toString(houseCode)).replaceAll("\\$\\{unitCode\\}", Byte.toString(unitCode)),
                nameFormat.replaceAll("\\$\\{houseCode\\}", Byte.toString(houseCode)).replaceAll("\\$\\{unitCode\\}", Byte.toString(unitCode)),
                descriptionFormat.replaceAll("\\$\\{houseCode\\}", Byte.toString(houseCode)).replaceAll("\\$\\{unitCode\\}", Byte.toString(unitCode)),
                device);
        return device;
    }

    public void removeDevice(byte houseCode, byte unitCode) {
        if(devices.containsKey(houseCode) && devices.get(houseCode).containsKey(unitCode)) {
            Device device = devices.get(houseCode).remove(unitCode);
            if(devices.get(houseCode).size() == 0)
                devices.remove(houseCode);
            hardwareCallback.removeDevice(device);
        }
    }

    public class Device {

        @Component
        @Id(value = "power", name = "Power", description = "Power")
        private final PowerImpl powerImpl;

        public Device(ManagedCollectionFactory managedCollectionFactory, byte houseId, byte unitCode) {
            this.powerImpl = new PowerImpl(managedCollectionFactory, houseId, unitCode);
        }

        public void turnOn() {
            powerImpl.turnOn();
        }

        public void turnOff() {
            powerImpl.turnOff();
        }

        public void setOn(boolean on) {
            powerImpl.setOn(on);
        }

        @Classes(Classes.LIGHT)
        public class PowerImpl extends PowerBase {

            private final byte houseId;
            private final byte unitCode;

            public PowerImpl(ManagedCollectionFactory managedCollectionFactory, byte houseId, byte unitCode) {
                super(managedCollectionFactory);
                this.houseId = houseId;
                this.unitCode = unitCode;
            }

            @Override
            public synchronized void turnOn() {
                try {
                    lighting1.turnOn(houseId, unitCode);
                    setOn(true);
                } catch (IOException e) {
                    throw new HousemateException("Failed to turn device on");
                }
            }

            @Override
            public synchronized void turnOff() {
                try {
                    lighting1.turnOff(houseId, unitCode);
                    setOn(false);
                } catch (IOException e) {
                    throw new HousemateException("Failed to turn device off");
                }
            }
        }
    }

    public static class X10 extends Lighting1Handler {

        @Inject
        protected X10(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "x10",
                    "${houseCode}-${unitCode}",
                    "X10 Appliance ${unitCode}",
                    "X10 Appliance ${unitCode}, remote id ${houseCode}");
        }
    }

    public static class ARC extends Lighting1Handler {

        @Inject
        protected ARC(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "arc",
                    "${houseCode}-${unitCode}",
                    "ARC Appliance ${unitCode}",
                    "ARC Appliance ${unitCode}, remote id ${houseCode}");
        }
    }

    public static class ELROAB400D extends Lighting1Handler {

        @Inject
        protected ELROAB400D(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "elroab400d",
                    "${houseCode}-${unitCode}",
                    "ELROAB400D Appliance ${unitCode}",
                    "ELROAB400D Appliance ${unitCode}, remote id ${houseCode}");
        }
    }

    public static class Waveman extends Lighting1Handler {

        @Inject
        protected Waveman(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "waveman",
                    "${houseCode}-${unitCode}",
                    "Waveman Appliance ${unitCode}",
                    "Waveman Appliance ${unitCode}, remote id ${houseCode}");
        }
    }

    public static class ChaconEMW200 extends Lighting1Handler {

        @Inject
        protected ChaconEMW200(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "chaconemw200",
                    "${houseCode}-${unitCode}",
                    "ChaconEMW200 Appliance ${unitCode}",
                    "ChaconEMW200 Appliance ${unitCode}, remote id ${houseCode}");
        }
    }

    public static class IMPULS extends Lighting1Handler {

        @Inject
        protected IMPULS(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.Lighting1.SubType.X10,
                    "impuls",
                    "${houseCode}-${unitCode}",
                    "IMPULS Appliance ${unitCode}",
                    "IMPULS Appliance ${unitCode}, remote id ${houseCode}");
        }
    }
}
