package com.intuso.housemate.broker.object.general;

import com.intuso.housemate.api.resources.Resources;
import com.intuso.housemate.broker.AuthenticationController;
import com.intuso.housemate.broker.PluginListener;
import com.intuso.housemate.broker.client.LocalClient;
import com.intuso.housemate.broker.factory.ConditionFactory;
import com.intuso.housemate.broker.factory.ConsequenceFactory;
import com.intuso.housemate.broker.factory.DeviceFactory;
import com.intuso.housemate.broker.object.bridge.BrokerBridgeResources;
import com.intuso.housemate.broker.storage.BrokerObjectStorage;
import com.intuso.housemate.object.broker.LifecycleHandler;
import com.intuso.housemate.object.broker.ServerComms;
import com.intuso.housemate.object.broker.proxy.BrokerProxyFactory;
import com.intuso.housemate.object.broker.proxy.BrokerProxyResources;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.listeners.ListenerRegistration;
import com.intuso.listeners.Listeners;
import com.intuso.utils.log.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/02/13
 * Time: 09:26
 * To change this template use File | Settings | File Templates.
 */
public class BrokerGeneralResources implements Resources {

    private final Log log;
    private final Map<String, String> properties;
    private ServerComms comms;
    private BrokerGeneralRootObject root;
    private BrokerObjectStorage storage;
    private AuthenticationController authenticationController;
    private DeviceFactory deviceFactory;
    private LifecycleHandler lifecycleHandler;
    private ConditionFactory conditionFactory;
    private ConsequenceFactory consequenceFactory;
    private List<PluginDescriptor> plugins;
    private Listeners<PluginListener> pluginListeners;
    private BrokerRealResources realResources;
    private BrokerBridgeResources bridgeResources;
    private BrokerProxyResources<BrokerProxyFactory.All> proxyResources;
    private LocalClient client;
    private RealResources clientResources;

    public BrokerGeneralResources(Log log, Map<String, String> properties) {
        this.log = log;
        this.properties = properties;
        plugins = new ArrayList<PluginDescriptor>();
        pluginListeners = new Listeners<PluginListener>();
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    public ServerComms getComms() {
        return comms;
    }

    public void setComms(ServerComms comms) {
        this.comms = comms;
    }

    public BrokerGeneralRootObject getRoot() {
        return root;
    }

    public void setRoot(BrokerGeneralRootObject root) {
        this.root = root;
    }

    public BrokerObjectStorage getStorage() {
        return storage;
    }

    public void setStorage(BrokerObjectStorage storage) {
        this.storage = storage;
    }

    public AuthenticationController getAuthenticationController() {
        return authenticationController;
    }

    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }

    public LifecycleHandler getLifecycleHandler() {
        return lifecycleHandler;
    }

    public void setLifecycleHandler(LifecycleHandler lifecycleHandler) {
        this.lifecycleHandler = lifecycleHandler;
    }

    public DeviceFactory getDeviceFactory() {
        return deviceFactory;
    }

    public void setDeviceFactory(DeviceFactory deviceFactory) {
        this.deviceFactory = deviceFactory;
    }

    public ConditionFactory getConditionFactory() {
        return conditionFactory;
    }

    public void setConditionFactory(ConditionFactory conditionFactory) {
        this.conditionFactory = conditionFactory;
    }

    public ConsequenceFactory getConsequenceFactory() {
        return consequenceFactory;
    }

    public void setConsequenceFactory(ConsequenceFactory consequenceFactory) {
        this.consequenceFactory = consequenceFactory;
    }

    public void addPlugin(PluginDescriptor plugin) {
        log.d("New Plugin: " + plugin.getClass().getName());
        plugins.add(plugin);
        for(PluginListener listener : pluginListeners)
            listener.pluginAdded(plugin);
    }

    public void removePlugin(PluginDescriptor plugin) {
        plugins.remove(plugin);
        for(PluginListener listener : pluginListeners)
            listener.pluginRemoved(plugin);
    }

    public ListenerRegistration<PluginListener> addPluginListener(PluginListener listener, boolean callForExisting) {
        ListenerRegistration<PluginListener> result = pluginListeners.addListener(listener);
        if(callForExisting)
            for(PluginDescriptor plugin : plugins)
                listener.pluginAdded(plugin);
        return result;
    }

    public BrokerRealResources getRealResources() {
        return realResources;
    }

    public void setRealResources(BrokerRealResources realResources) {
        this.realResources = realResources;
    }

    public BrokerBridgeResources getBridgeResources() {
        return bridgeResources;
    }

    public void setBridgeResources(BrokerBridgeResources bridgeResources) {
        this.bridgeResources = bridgeResources;
    }

    public BrokerProxyResources<BrokerProxyFactory.All> getProxyResources() {
        return proxyResources;
    }

    public void setProxyResources(BrokerProxyResources<BrokerProxyFactory.All> proxyResources) {
        this.proxyResources = proxyResources;
    }

    public LocalClient getClient() {
        return client;
    }

    public void setClient(LocalClient client) {
        this.client = client;
    }

    public RealResources getClientResources() {
        return clientResources;
    }

    public void setClientResources(RealResources clientResources) {
        this.clientResources = clientResources;
    }
}