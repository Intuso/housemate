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
import com.rfxcom.rfxtrx.util.lighting2.Lighting2;

import java.io.IOException;
import java.util.Map;

/**
 * Created by tomc on 02/02/17.
 */
public class Lighting2Handler extends Handler implements Lighting2.Callback {

    private final ManagedCollectionFactory managedCollectionFactory;
    private final Lighting2 lighting2;
    
    private final String idFormat, nameFormat, descriptionFormat;
    
    private final Map<Integer, Map<Byte, Device>> devices = Maps.newHashMap();

    protected Lighting2Handler(ManagedCollectionFactory managedCollectionFactory,
                               RFXtrx rfxtrx,
                               com.rfxcom.rfxtrx.message.Lighting2.SubType subType,
                               String idPrefix,
                               String idFormat,
                               String nameFormat,
                               String descriptionFormat) {
        super(idPrefix + "-");
        this.managedCollectionFactory = managedCollectionFactory;
        this.lighting2 = new Lighting2(rfxtrx, subType);
        this.idFormat = idPrefix + "-" + idFormat;
        this.nameFormat = nameFormat;
        this.descriptionFormat = descriptionFormat;
    }

    @Override
    public ManagedCollection.Registration initListener() {
        return lighting2.addCallback(this);
    }

    @Override
    public void turnedOn(int houseId, byte unitCode) {
        Device device = getOrCreate(houseId, unitCode);
        if(device != null)
            device.setOn(true);
    }

    @Override
    public void turnedOnAll(int houseId) {
        if(devices.containsKey(houseId))
            for(Device device : devices.get(houseId).values())
                device.setOn(true);
    }

    @Override
    public void turnedOff(int houseId, byte unitCode) {
        Device device = getOrCreate(houseId, unitCode);
        if(device != null)
            device.setOn(false);
    }

    @Override
    public void turnedOffAll(int houseId) {
        if(devices.containsKey(houseId))
            for(Device device : devices.get(houseId).values())
                device.setOn(false);
    }

    @Override
    public void setLevel(int houseId, byte unitCode, byte level) {
        // todo
    }

    @Override
    public void setLevelAll(int houseId, byte level) {
        // todo
    }

    @Override
    void parseIdDetails(String details) {
        addDevice(Integer.parseInt(details.split("-")[0]), Byte.parseByte(details.split("-")[1]));
    }

    private Device getOrCreate(int houseId, byte unitCode) {
        if(devices.containsKey(houseId) && devices.get(houseId).containsKey(unitCode))
            return devices.get(houseId).get(unitCode);
        else if(autoCreate)
            return addDevice(houseId, unitCode);
        return null;
    }

    public Device addDevice(int houseId, byte unitCode) {
        Device device = new Device(managedCollectionFactory, houseId, unitCode);
        if(!devices.containsKey(houseId))
            devices.put(houseId, Maps.<Byte, Device>newHashMap());
        devices.get(houseId).put(unitCode, device);
        hardwareCallback.addDevice(
                idFormat.replaceAll("\\$\\{houseId\\}", Integer.toString(houseId)).replaceAll("\\$\\{unitCode\\}", Byte.toString(unitCode)),
                nameFormat.replaceAll("\\$\\{houseId\\}", Integer.toString(houseId)).replaceAll("\\$\\{unitCode\\}", Byte.toString(unitCode)),
                descriptionFormat.replaceAll("\\$\\{houseId\\}", Integer.toString(houseId)).replaceAll("\\$\\{unitCode\\}", Byte.toString(unitCode)),
                device);
        return device;
    }

    public void removeDevice(int houseId, byte unitCode) {
        if(devices.containsKey(houseId) && devices.get(houseId).containsKey(unitCode)) {
            Device device = devices.get(houseId).remove(unitCode);
            if(devices.get(houseId).size() == 0)
                devices.remove(houseId);
            hardwareCallback.removeDevice(device);
        }
    }

    public class Device {

        @Component
        @Id(value = "power", name = "Power", description = "Power")
        private final PowerImpl powerImpl;

        public Device(ManagedCollectionFactory managedCollectionFactory, int houseId, byte unitCode) {
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

            private final int houseId;
            private final byte unitCode;

            public PowerImpl(ManagedCollectionFactory managedCollectionFactory, int houseId, byte unitCode) {
                super(managedCollectionFactory);
                this.houseId = houseId;
                this.unitCode = unitCode;
            }

            @Override
            public synchronized void turnOn() {
                try {
                    lighting2.turnOn(houseId, unitCode);
                    setOn(true);
                } catch (IOException e) {
                    throw new HousemateException("Failed to turn device on");
                }
            }

            @Override
            public synchronized void turnOff() {
                try {
                    lighting2.turnOff(houseId, unitCode);
                    setOn(false);
                } catch (IOException e) {
                    throw new HousemateException("Failed to turn device off");
                }
            }
        }
    }

    public static class AC extends Lighting2Handler {

        @Inject
        protected AC(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.Lighting2.SubType.AC,
                    "ac",
                    "${houseId}-${unitCode}",
                    "AC Appliance ${unitCode}",
                    "AC Appliance ${unitCode}, remote id ${houseId}");
        }
    }
    
    public static class HomeEasyEU extends Lighting2Handler {

        @Inject
        protected HomeEasyEU(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.Lighting2.SubType.HomeEasyEU,
                    "homeeasyeu",
                    "${houseId}-${unitCode}",
                    "HomeEasy EU Appliance ${unitCode}",
                    "HomeEasy EU Appliance ${unitCode}, remote id ${houseId}");
        }
    }
    
    public static class ANSLUT extends Lighting2Handler {

        @Inject
        protected ANSLUT(ManagedCollectionFactory managedCollectionFactory, RFXtrx rfxtrx) {
            super(managedCollectionFactory, rfxtrx, com.rfxcom.rfxtrx.message.Lighting2.SubType.ANSLUT,
                    "anslut",
                    "${houseId}-${unitCode}",
                    "ANSLUT Appliance ${unitCode}",
                    "ANSLUT Appliance ${unitCode}, remote id ${houseId}");
        }
    }
}
