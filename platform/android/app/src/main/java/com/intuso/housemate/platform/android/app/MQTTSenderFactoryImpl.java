package com.intuso.housemate.platform.android.app;

import com.intuso.housemate.client.v1_0.messaging.mqtt.MQTTSender;
import com.intuso.housemate.client.v1_0.messaging.mqtt.MessageConverter;
import com.intuso.housemate.client.v1_0.serialisation.javabin.JavabinSerialiser;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;

/**
 * Created by tomc on 07/03/17.
 */
public class MQTTSenderFactoryImpl implements MQTTSender.Factory {

    private final MqttClient client;
    private final MessageConverter messageConverter = new MessageConverter.Javabin(new JavabinSerialiser());

    public MQTTSenderFactoryImpl(MqttClient client) {
        this.client = client;
    }

    @Override
    public MQTTSender create(Logger logger, String name) {
        return new MQTTSender(logger, name, client, messageConverter);
    }
}
