package com.intuso.housemate.object.proxy.simple;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.intuso.housemate.api.comms.*;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.ProxyRoot;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/01/14
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
public class ProxyClientHelper<ROOT extends ProxyRoot<?, ?, ?>> {

    private final Log log;
    private final ROOT proxyRoot;
    private final Router router;

    private ApplicationDetails applicationDetails;
    private String component;
    private List<HousemateObject.TreeLoadInfo> toLoad = Lists.newArrayList();
    private LoadManager.Callback callback;

    private boolean shouldClearRoot = true;
    private boolean shouldLoad = true;
    private ListenerRegistration proxyListenerRegistration;
    private ListenerRegistration routerListenerRegistration;

    private ProxyClientHelper(Log log, ROOT proxyRoot, Router router) {
        this.log = log;
        this.proxyRoot = proxyRoot;
        this.router = router;
    }

    public static <ROOT extends ProxyRoot<?, ?, ?>> ProxyClientHelper<ROOT>
                newClientHelper(Log log, ROOT proxyRoot, Router router) {
        return new ProxyClientHelper<>(log, proxyRoot, router);
    }

    public static <ROOT extends ProxyRoot<?, ?, ?>> ProxyClientHelper<ROOT>
                newClientHelper(Injector injector) {
        return new ProxyClientHelper(
                injector.getInstance(Log.class),
                injector.getInstance(SimpleProxyRoot.class),
                injector.getInstance(Router.class));
    }

    public static <ROOT extends ProxyRoot<?, ?, ?>> ProxyClientHelper<ROOT>
                newClientHelper(Module... modules) {
        return ProxyClientHelper.newClientHelper(Guice.createInjector(modules));
    }

    public ROOT getRoot() {
        return proxyRoot;
    }

    public ProxyClientHelper<ROOT> applicationDetails(ApplicationDetails applicationDetails) {
        this.applicationDetails = applicationDetails;
        return this;
    }

    public ProxyClientHelper<ROOT> component(String component) {
        this.component = component;
        return this;
    }

    public ProxyClientHelper<ROOT> load(HousemateObject.TreeLoadInfo treeLoadInfo) {
        toLoad.add(treeLoadInfo);
        return this;
    }

    private ProxyClientHelper<ROOT> load(String[] path, String ending) {
        return load(HousemateObject.TreeLoadInfo.create(path, ending));
    }

    public ProxyClientHelper<ROOT> load(String ... path) {
        return load(path, null);
    }

    public ProxyClientHelper<ROOT> loadAllChildren(String ... path) {
        return load(path, HousemateObject.EVERYTHING);
    }

    public ProxyClientHelper<ROOT> loadAllDescendants(String... path) {
        return load(path, HousemateObject.EVERYTHING_RECURSIVE);
    }

    public ProxyClientHelper<ROOT> callback(LoadManager.Callback callback) {
        this.callback = callback;
        return this;
    }

    public ProxyClientHelper<ROOT> start() {
        proxyListenerRegistration = proxyRoot.addObjectListener(new ProxyRootListener());
        RouterListener routerListener = new RouterListener();
        routerListenerRegistration = router.addObjectListener(routerListener);
        routerListener.serverConnectionStatusChanged(null, ServerConnectionStatus.DisconnectedPermanently);
        return this;
    }

    public void unregister() {
        if(proxyRoot.getApplicationInstanceStatus() != ApplicationInstanceStatus.Unregistered)
            proxyRoot.unregister();
        if(router.getApplicationInstanceStatus() != ApplicationInstanceStatus.Unregistered)
            router.unregister();
    }

    public void stop() {
        router.disconnect();
        if(proxyListenerRegistration != null)
            proxyListenerRegistration.removeListener();
        if(routerListenerRegistration != null)
            routerListenerRegistration.removeListener();
    }

    private class RouterListener implements RootListener<RouterRoot> {

        private boolean needsRegistering = true;

        @Override
        public void serverConnectionStatusChanged(RouterRoot root, ServerConnectionStatus serverConnectionStatus) {
            log.d("Router serverConnectionStatus = " + serverConnectionStatus);
            if(serverConnectionStatus == ServerConnectionStatus.DisconnectedPermanently) {
                needsRegistering = true;
                router.connect();
            } else if((serverConnectionStatus == ServerConnectionStatus.ConnectedToServer || serverConnectionStatus == ServerConnectionStatus.DisconnectedTemporarily) && needsRegistering) {
                needsRegistering = false;
                router.register(applicationDetails, component);
            }
        }

        @Override
        public void applicationStatusChanged(RouterRoot root, ApplicationStatus applicationStatus) {
            log.d("Router applicationStatus = " + applicationStatus);
        }

        @Override
        public void applicationInstanceStatusChanged(RouterRoot root, ApplicationInstanceStatus applicationInstanceStatus) {
            log.d("Router applicationInstanceStatus = " + applicationInstanceStatus);
        }

        @Override
        public void newApplicationInstance(RouterRoot root, String instanceId) {
            // connection manager saves this in the properties for us
        }

        @Override
        public void newServerInstance(RouterRoot root, String serverId) {
            // connection manager will re-register us
        }
    }

    private class ProxyRootListener implements RootListener<ProxyRoot<?, ?, ?>> {

        private boolean needsRegistering = true;

        @Override
        public void serverConnectionStatusChanged(ProxyRoot<?, ?, ?> root, ServerConnectionStatus serverConnectionStatus) {
            log.d("Root serverConnectionStatus = " + serverConnectionStatus);
            if(serverConnectionStatus == ServerConnectionStatus.DisconnectedPermanently)
                needsRegistering = true;
            else if((serverConnectionStatus == ServerConnectionStatus.ConnectedToServer || serverConnectionStatus == ServerConnectionStatus.DisconnectedTemporarily) && needsRegistering) {
                needsRegistering = false;
                proxyRoot.register(applicationDetails, component);
            }
        }

        @Override
        public void applicationStatusChanged(ProxyRoot<?, ?, ?> root, ApplicationStatus applicationStatus) {
            log.d("Root applicationStatus = " + applicationStatus);
        }

        @Override
        public void applicationInstanceStatusChanged(ProxyRoot<?, ?, ?> root, ApplicationInstanceStatus applicationInstanceStatus) {
            log.d("Root applicationInstanceStatus = " + applicationInstanceStatus);
            if(applicationInstanceStatus == ApplicationInstanceStatus.Allowed) {
                if(shouldClearRoot) {
                    proxyRoot.clearLoadedObjects();
                    shouldClearRoot = false;
                }
                if(shouldLoad) {
                    proxyRoot.load(new LoadManager(callback, toLoad));
                    shouldLoad = false;
                }
            }
        }

        @Override
        public void newApplicationInstance(ProxyRoot<?, ?, ?> root, String instanceId) {
            // do nothing, saved in router listener
        }

        @Override
        public void newServerInstance(ProxyRoot<?, ?, ?> root, String serverId) {
            shouldClearRoot = true;
            shouldLoad = true;
        }
    }
}
