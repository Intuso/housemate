package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.hardware.HardwareData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealHardware;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.object.real.annotations.AnnotationProcessor;
import com.intuso.housemate.object.real.annotations.Property;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.homeeasy.Appliance;
import com.rfxcom.rfxtrx.homeeasy.HomeEasy;
import com.rfxcom.rfxtrx.homeeasy.House;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by tomc on 16/03/15.
 */
@TypeInfo(id = "rfxtrx433", name = "RFXtr433", description = "RFXCom 433MHz Transceiver")
public class RFXtrx433Hardware extends RealHardware implements HomeEasy.Callback {

    public static RFXtrx433Hardware INSTANCE;

    private final RFXtrx rfxtrx = new RFXtrx(getLog(), Lists.<Pattern>newArrayList());
    private final HomeEasy homeEasy = HomeEasy.forUK(rfxtrx);
    private ListenerRegistration messageListener;
    private final SetMultimap<Integer, Byte> knownAppliances = HashMultimap.create();
    private final DeviceFactory<HomeEasyAppliance> homeEasyApplianceFactory;
    private final AnnotationProcessor annotationProcessor;

    @Inject
    public RFXtrx433Hardware(Log log, ListenersFactory listenersFactory, @Assisted HardwareData data, DeviceFactory<HomeEasyAppliance> homeEasyApplianceFactory, AnnotationProcessor annotationProcessor) {
        super(log, listenersFactory, data);
        this.homeEasyApplianceFactory = homeEasyApplianceFactory;
        this.annotationProcessor = annotationProcessor;
        INSTANCE = this;
    }

    @Property(id = "serial-pattern", name = "Serial port pattern", description = "Regex matching acceptable serial port names", typeId = "string", initialValue = ".*ttyUSB.*")
    public void setPattern(String pattern) {
        rfxtrx.setPatterns(Lists.newArrayList(Pattern.compile(pattern)));
    }

    @Property(id = "create", name = "Create devices", description = "Create a new device when a command is received for it", typeId = "boolean", initialValue = "true")
    public void setCreate(boolean create) {
        if(messageListener != null) {
            messageListener.removeListener();
            messageListener = null;
        }
        if(create)
            messageListener = homeEasy.addCallback(this);
    }

    @Override
    public void turnedOn(int houseId, byte unitCode) {
        ensureAppliance(houseId, unitCode, true);
    }

    @Override
    public void turnedOnAll(int houseId) {

    }

    @Override
    public void turnedOff(int houseId, byte unitCode) {
        ensureAppliance(houseId, unitCode, false);
    }

    @Override
    public void turnedOffAll(int houseId) {

    }

    @Override
    public void setLevel(int houseId, byte unitCode, byte level) {
        ensureAppliance(houseId, unitCode, level != 0);
    }

    @Override
    public void setLevelAll(int houseId, byte level) {

    }

    public Appliance makeAppliance(int houseId, byte unitCode) {
        knownAppliances.put(houseId, unitCode);
        return new Appliance(new House(homeEasy, houseId), unitCode);
    }

    public void ensureAppliance(int houseId, byte unitcode, boolean on) {
        if(!knownAppliances.containsEntry(houseId, unitcode)) {
            try {
                String name = houseId + "/" + (int)unitcode;
                HomeEasyAppliance appliance = homeEasyApplianceFactory.create(new DeviceData(UUID.randomUUID().toString(), name, name));
                annotationProcessor.process(((RealRoot)getRealRoot()).getTypes(), appliance);
                CommandPerformListener<RealCommand> dummmyListener = new CommandPerformListener<RealCommand>() {
                    @Override
                    public void commandStarted(RealCommand command) {}

                    @Override
                    public void commandFinished(RealCommand command) {}

                    @Override
                    public void commandFailed(RealCommand command, String error) {}
                };
                appliance.houseId.set(new TypeInstances(new TypeInstance("" + houseId)), dummmyListener);
                appliance.unitCode.set(new TypeInstances(new TypeInstance("" + (int) unitcode)), dummmyListener);
                if(on)
                    appliance.setOn();
                else
                    appliance.setOff();
                ((RealRoot)getRealRoot()).addDevice(appliance);
            } catch (HousemateException e) {
                getLog().e("Failed to auto-create device " + houseId + "/" + (int)unitcode);
            }
        }
    }
}
