package com.intuso.housemate.plugin.arduinotempsensor.ioc;

import com.google.common.collect.Lists;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.plugin.AnnotatedPlugin;
import com.intuso.housemate.client.v1_0.api.plugin.FeatureDrivers;
import com.intuso.housemate.plugin.arduinotempsensor.ArduinoIndicator;
import com.intuso.housemate.plugin.arduinotempsensor.ArduinoTemperatureSensor;
import com.intuso.housemate.plugin.arduinotempsensor.SerialPortWrapper;
import jssc.SerialPort;
import org.slf4j.Logger;

import java.util.List;
import java.util.regex.Pattern;

@Id(value = "com.intuso.housemate.plugin.arduino-temp-sensor", name = "Arduino Temperature Sensor plugin", description = "Plugin for temperature sensing using an Arduino")
@FeatureDrivers({ArduinoTemperatureSensor.class,
        ArduinoIndicator.class})
public class ArduinoTemperatureSensorPlugin extends AnnotatedPlugin {

    private final static List<Pattern> PATTERNS = Lists.newArrayList(
            Pattern.compile(".*ttyACM.*")
    );

    @Provides
    @Singleton
    public SerialPortWrapper getSerialPortWrapper(Logger logger) {
        logger.debug("Initialising Arduino Temperature Sensor plugin");
        return new SerialPortWrapper(logger, PATTERNS,
                SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, // params
                SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT); // flow control mode
    }
}