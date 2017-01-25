package com.intuso.housemate.client.proxy.internal.simple.ioc;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyObject;
import com.intuso.housemate.client.proxy.api.internal.object.ProxyServer;
import com.intuso.housemate.client.proxy.internal.simple.SimpleProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/01/14
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class SimpleProxyServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new SimpleProxyModule());
        bind(SimpleProxyServer.class).in(Scopes.SINGLETON);
        bind(ProxyServer.class).to(SimpleProxyServer.class);
        Multibinder.newSetBinder(binder(), Service.class).addBinding().to(ProxyServer.Service.class);
    }

    @Provides
    @Server
    public Logger getRootLogger() {
        return ChildUtil.logger(LoggerFactory.getLogger(ProxyObject.PROXY), Object.VERSION);
    }
}
