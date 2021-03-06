package com.intuso.housemate.client.proxy.internal.annotation.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.client.proxy.internal.annotation.ClassCreator;
import com.intuso.housemate.client.proxy.internal.annotation.ProxyWrapper;
import com.intuso.housemate.client.proxy.internal.annotation.ProxyWrapperInternal;

/**
 * Created by tomc on 11/01/17.
 */
public class ProxyWrapperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProxyWrapper.class).to(ProxyWrapperInternal.class);
        bind(ProxyWrapperInternal.class).in(Scopes.SINGLETON);
        bind(ClassCreator.class).to(ClassCreator.FromInjector.class);
    }
}
