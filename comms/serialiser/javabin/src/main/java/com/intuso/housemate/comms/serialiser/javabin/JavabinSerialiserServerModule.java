package com.intuso.housemate.comms.serialiser.javabin;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.comms.serialiser.api.StreamSerialiserFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:32
 * To change this template use File | Settings | File Templates.
 */
public class JavabinSerialiserServerModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder.newSetBinder(binder(), StreamSerialiserFactory.class).addBinding().to(JavabinSerialiser.Factory.class);
    }
}
