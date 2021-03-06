package com.intuso.housemate.platform.pc;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.plugin.host.internal.PluginFileFinder;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by tomc on 21/11/16.
 */
public class PCPluginFileFinder implements PluginFileFinder {

    public final static String PLUGINS_DIR = "plugins.dir";

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(PLUGINS_DIR, "./plugins");
    }

    private final static Logger logger = LoggerFactory.getLogger(PCPluginFileFinder.class);

    private final Map<File, WeakHashMap<Listener, String>> listenerPluginIds = Maps.newHashMap();
    private final ManagedCollection<Listener> listeners;

    @Inject
    public PCPluginFileFinder(ManagedCollectionFactory managedCollectionFactory, PropertyRepository properties) {
        this.listeners = managedCollectionFactory.createSet();
        File pluginDirectory = new File(properties.get(PLUGINS_DIR));
        if(!pluginDirectory.exists())
            pluginDirectory.mkdir();
        if(pluginDirectory.isFile())
            logger.warn("Plugin path is not a directory");
        else {
            logger.debug("Loading plugins from " + pluginDirectory.getAbsolutePath());
            for(File pluginFile : pluginDirectory.listFiles(new PluginFileFilter()))
                listenerPluginIds.put(pluginFile, new WeakHashMap<Listener, String>());
        }
    }

    @Override
    public ManagedCollection.Registration addListener(Listener listener, boolean callForExisting) {
        ManagedCollection.Registration result = listeners.add(listener);
        for(File pluginFile : listenerPluginIds.keySet())
            listenerPluginIds.get(pluginFile).put(listener, listener.fileFound(pluginFile));
        return result;
    }

    private class PluginFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return file.isFile() && file.getName().endsWith(".jar");
        }
    }
}
