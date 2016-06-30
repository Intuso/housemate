package com.intuso.housemate.plugin.api.bridge.v1_0.driver;

import com.google.common.base.Function;
import com.intuso.housemate.plugin.v1_0.api.driver.PluginResource;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 05/11/15.
 */
public class PluginResourceBridgeReverse<FROM, TO> implements PluginResource<TO> {

    private final com.intuso.housemate.plugin.api.internal.driver.PluginResource<FROM> pluginResource;
    private final Function<? super FROM, ? extends TO> convertFrom;

    public PluginResourceBridgeReverse(com.intuso.housemate.plugin.api.internal.driver.PluginResource<FROM> pluginResource, Function<? super FROM, ? extends TO> convertFrom) {
        this.pluginResource = pluginResource;
        this.convertFrom = convertFrom;
    }

    public com.intuso.housemate.plugin.api.internal.driver.PluginResource<FROM> getPluginResource() {
        return pluginResource;
    }

    @Override
    public TO getResource() {
        return convertFrom.apply(pluginResource.getResource());
    }

    @Override
    public ListenerRegistration addListener(Listener<TO> listener) {
        return pluginResource.addListener(new ListenerBridge(listener));
    }

    private class ListenerBridge implements com.intuso.housemate.plugin.api.internal.driver.PluginResource.Listener<FROM> {

        private final Listener<TO> listener;

        private ListenerBridge(Listener<TO> listener) {
            this.listener = listener;
        }

        @Override
        public void resourceAvailable(FROM resource) {
            listener.resourceAvailable(convertFrom.apply(resource));
        }

        @Override
        public void resourceUnavailable() {
            listener.resourceUnavailable();
        }
    }
}