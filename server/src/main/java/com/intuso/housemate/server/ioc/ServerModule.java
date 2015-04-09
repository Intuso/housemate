package com.intuso.housemate.server.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.object.real.ioc.RealObjectModule;
import com.intuso.housemate.plugin.host.ioc.PluginHostModule;
import com.intuso.housemate.server.Server;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.comms.RemoteClientManager;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.object.general.ServerGeneralRoot;
import com.intuso.housemate.server.object.real.FactoryPluginListener;
import com.intuso.housemate.server.object.real.ServerRealRoot;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 */
public class ServerModule extends AbstractModule {

    public ServerModule(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(Server.SERVER_NAME, "My Server");
    }

    @Override
    protected void configure() {

        install(new RealObjectModule());
        install(new PluginHostModule());

        // bind everything as singletons that should be
        // root objects
        bind(ServerRealRoot.class).in(Scopes.SINGLETON);
        bind(ServerGeneralRoot.class).in(Scopes.SINGLETON);
        bind(RootBridge.class).in(Scopes.SINGLETON);
        // other things
        bind(FactoryPluginListener.class).in(Scopes.SINGLETON);
        bind(Server.class).in(Scopes.SINGLETON);
        bind(RemoteClientManager.class).in(Scopes.SINGLETON);
        bind(MainRouter.class).in(Scopes.SINGLETON);

        // bind implementations
        bind(RealRoot.class).to(ServerRealRoot.class);
        bind(Router.class).to(MainRouter.class);
        bind(new TypeLiteral<Root<?>>() {}).to(RootBridge.class);
    }
}
