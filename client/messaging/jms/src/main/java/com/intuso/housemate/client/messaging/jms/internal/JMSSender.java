package com.intuso.housemate.client.messaging.jms.internal;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.messaging.api.internal.MessageConstants;
import com.intuso.housemate.client.messaging.api.internal.Sender;
import org.slf4j.Logger;

import javax.jms.*;
import java.io.Serializable;

/**
 * Created by tomc on 21/02/17.
 */
public class JMSSender implements Sender {

    private final Logger logger;
    private final MessageConverter messageConverter;
    private final Session session;
    private final MessageProducer producer;

    @Inject
    public JMSSender(@Assisted Logger logger,
                     @Assisted String name,
                     MessageConverter messageConverter,
                     Connection connection) throws JMSException {
        this.logger = logger;
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.messageConverter = messageConverter;
        this.producer = session.createProducer(session.createTopic(name));
    }

    @Override
    public void close() {
        if(producer != null) {
            try {
                producer.close();
            } catch (JMSException e) {
                logger.error("Failed to close producer");
            }
        }
        if(session != null) {
            try {
                session.close();
            } catch(JMSException e) {
                logger.error("Failed to close session");
            }
        }
    }

    @Override
    public void send(Serializable object, boolean persistent) {
        try {
            Message message = messageConverter.toMessage(session, object);
            if (persistent)
                message.setBooleanProperty(MessageConstants.STORE, true);
            producer.send(message);
            logger.trace("Sent {} on {}", object, producer.getDestination());
        } catch(JMSException e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    public interface Factory {
        JMSSender create(Logger logger, String name);
    }

    public static class FactoryImpl implements Sender.Factory {

        private final Factory factory;

        @Inject
        public FactoryImpl(Factory factory) {
            this.factory = factory;
        }

        @Override
        public Sender create(Logger logger, String name) {
            return factory.create(logger, name);
        }
    }
}
