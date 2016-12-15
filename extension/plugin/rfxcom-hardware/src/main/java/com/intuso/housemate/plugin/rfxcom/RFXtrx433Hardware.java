package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.extension.homeeasyuk.api.HomeEasyUKHardwareAPI;
import com.intuso.housemate.plugin.v1_0.api.annotations.Id;
import com.intuso.housemate.plugin.v1_0.api.annotations.Property;
import com.intuso.housemate.plugin.v1_0.api.driver.HardwareDriver;
import com.intuso.utilities.listener.ListenersFactory;
import com.rfxcom.rfxtrx.RFXtrx;
import org.slf4j.Logger;

import java.util.regex.Pattern;

/**
 * Created by tomc on 16/03/15.
 */
@Id(value = "rfxtrx433", name = "RFXtr433", description = "RFXCom 433MHz Transceiver")
public class RFXtrx433Hardware implements HardwareDriver, HomeEasyUKHardwareAPI {

    private final Logger logger;

    private final ListenersFactory listenersFactory;

    private final RFXtrx rfxtrx;
    private String pattern = ".*ttyUSB.*";
    private boolean listen = true;

    @Inject
    public RFXtrx433Hardware(@Assisted Logger logger,
                             @Assisted Callback callback,
                             ListenersFactory listenersFactory) {

        this.logger = logger;
        this.listenersFactory = listenersFactory;

        // setup the connection to the USB device
        rfxtrx = new RFXtrx(logger, Lists.<Pattern>newArrayList());
    }

    @Property(value = "string")
    @Id(value = "serial-pattern", name = "Serial port pattern", description = "Regex matching acceptable serial port names")
    public void setPattern(String pattern) {
        this.pattern = pattern;
        rfxtrx.setPatterns(Lists.newArrayList(Pattern.compile(pattern)));
    }

    public String getPattern() {
        return pattern;
    }

    @Property(value = "boolean")
    @Id(value = "listen", name = "Listen for new devices", description = "Create a new device when a command is received for it")
    public void setListen(boolean listen) {
        this.listen = listen;
        // todo listen for new devices
    }

    @Override
    public void start() {
        rfxtrx.openPortSafe();
    }

    @Override
    public void stop() {
        rfxtrx.closePort();
    }

    @Override
    public Appliance appliance(int houseId, byte unitId) {
        return new HomeEasyUKAppliance(listenersFactory, rfxtrx, houseId, unitId);
    }
}
