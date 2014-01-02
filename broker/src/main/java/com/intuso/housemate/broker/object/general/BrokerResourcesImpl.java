package com.intuso.housemate.broker.object.general;

import com.google.inject.Injector;
import com.intuso.housemate.object.broker.BrokerResources;
import com.intuso.utilities.log.Log;

import java.util.Map;

/**
 */
public abstract class BrokerResourcesImpl<R> implements BrokerResources<R> {

    private final Log log;
    private final Map<String, String> properties;
    private final Injector injector;
    private R root;

    public BrokerResourcesImpl(Log log, Map<String, String> properties, Injector injector) {
        this.log = log;
        this.properties = properties;
        this.injector = injector;
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public Injector getInjector() {
        return injector;
    }

    @Override
    public R getRoot() {
        return root;
    }

    public void setRoot(R root) {
        this.root = root;
    }
}
