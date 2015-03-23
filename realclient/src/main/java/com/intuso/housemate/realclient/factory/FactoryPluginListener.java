package com.intuso.housemate.realclient.factory;

import com.google.common.collect.Sets;
import com.google.inject.*;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.object.real.RealRoot;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.factory.condition.ConditionFactoryType;
import com.intuso.housemate.object.real.factory.condition.RealConditionFactory;
import com.intuso.housemate.object.real.factory.device.DeviceFactoryType;
import com.intuso.housemate.object.real.factory.device.RealDeviceFactory;
import com.intuso.housemate.object.real.factory.hardware.HardwareFactoryType;
import com.intuso.housemate.object.real.factory.hardware.RealHardwareFactory;
import com.intuso.housemate.object.real.factory.task.RealTaskFactory;
import com.intuso.housemate.object.real.factory.task.TaskFactoryType;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.housemate.plugin.host.PluginListener;
import com.intuso.housemate.plugin.host.PluginManager;
import com.intuso.utilities.log.Log;

import java.util.Map;
import java.util.Set;

/**
 * Created by tomc on 19/03/15.
 */
public class FactoryPluginListener implements PluginListener {

    private final Log log;
    private final RealRoot root;
    private final HardwareFactoryType hardwareFactoryType;
    private final DeviceFactoryType deviceFactoryType;
    private final ConditionFactoryType conditionFactoryType;
    private final TaskFactoryType taskFactoryType;

    @Inject
    public FactoryPluginListener(Log log, HardwareFactoryType hardwareFactoryType, DeviceFactoryType deviceFactoryType, ConditionFactoryType conditionFactoryType, TaskFactoryType taskFactoryType, PluginManager pluginManager, RealRoot root) {
        this.log = log;
        this.hardwareFactoryType = hardwareFactoryType;
        this.deviceFactoryType = deviceFactoryType;
        this.conditionFactoryType = conditionFactoryType;
        this.taskFactoryType = taskFactoryType;
        this.root = root;
        pluginManager.addPluginListener(this, true);
    }

    @Override
    public void pluginAdded(Injector pluginInjector) {
        addTypes(pluginInjector);
        addHardwareFactories(pluginInjector);
        addDeviceFactories(pluginInjector);
        addConditionFactories(pluginInjector);
        addTaskFactories(pluginInjector);
    }

    @Override
    public void pluginRemoved(Injector pluginInjector) {
        removeTypes(pluginInjector);
        removeHardwareFactories(pluginInjector);
        removeDeviceFactories(pluginInjector);
        removeConditionFactories(pluginInjector);
        removeTaskFactories(pluginInjector);
    }

    private void addTypes(Injector pluginInjector) {
        for(RealType<?, ?, ?> type : pluginInjector.getInstance(new Key<Set<RealType<?, ?, ?>>>() {})) {
            log.d("Adding type " + type.getId());
            root.addType(type);
        }
    }

    private void removeTypes(Injector pluginInjector) {
        for(RealType<?, ?, ?> type : pluginInjector.getInstance(new Key<Set<RealType<?, ?, ?>>>() {})) {
            log.d("Adding type " + type.getId());
            root.removeType(type.getId());
        }
    }

    private void addHardwareFactories(Injector pluginInjector) {
        Set<Entry<RealHardwareFactory<?>>> factoryEntries = getEntries(pluginInjector, RealHardwareFactory.class);
        for(Entry<RealHardwareFactory<?>> factoryEntry : factoryEntries) {
            log.d("Adding new hardware factory for type " + factoryEntry.getTypeInfo().id());
            hardwareFactoryType.addFactory(factoryEntry.getTypeInfo().id(),
                    factoryEntry.getTypeInfo().name(), factoryEntry.getTypeInfo().description(),
                    factoryEntry.getInjector().getInstance(factoryEntry.getFactoryKey()));
        }
    }
    
    private void removeHardwareFactories(Injector pluginInjector) {
        Set<Entry<RealHardwareFactory<?>>> factoryEntries = getEntries(pluginInjector, RealHardwareFactory.class);
        for(Entry<RealHardwareFactory<?>> factoryEntry : factoryEntries)
            hardwareFactoryType.removeFactory(factoryEntry.getTypeInfo().id());
    }

    private void addDeviceFactories(Injector pluginInjector) {
        Set<Entry<RealDeviceFactory<?>>> factoryEntries = getEntries(pluginInjector, RealDeviceFactory.class);
        for(Entry<RealDeviceFactory<?>> factoryEntry : factoryEntries) {
            log.d("Adding new device factory for type " + factoryEntry.getTypeInfo().id());
            deviceFactoryType.addFactory(factoryEntry.getTypeInfo().id(),
                    factoryEntry.getTypeInfo().name(), factoryEntry.getTypeInfo().description(),
                    factoryEntry.getInjector().getInstance(factoryEntry.getFactoryKey()));
        }
    }

    private void removeDeviceFactories(Injector pluginInjector) {
        Set<Entry<DeviceFactory<?>>> factoryEntries = getEntries(pluginInjector, RealDeviceFactory.class);
        for(Entry<DeviceFactory<?>> factoryEntry : factoryEntries)
            deviceFactoryType.removeFactory(factoryEntry.getTypeInfo().id());
    }

    private void addConditionFactories(Injector pluginInjector) {
        Set<Entry<RealConditionFactory<?>>> factoryEntries = getEntries(pluginInjector, RealConditionFactory.class);
        for(Entry<RealConditionFactory<?>> factoryEntry : factoryEntries) {
            log.d("Adding new condition factory for type " + factoryEntry.getTypeInfo().id());
            conditionFactoryType.addFactory(factoryEntry.getTypeInfo().id(),
                    factoryEntry.getTypeInfo().name(), factoryEntry.getTypeInfo().description(),
                    factoryEntry.getInjector().getInstance(factoryEntry.getFactoryKey()));
        }
    }

    private void removeConditionFactories(Injector pluginInjector) {
        Set<Entry<RealConditionFactory<?>>> factoryEntries = getEntries(pluginInjector, RealConditionFactory.class);
        for(Entry<RealConditionFactory<?>> factoryEntry : factoryEntries)
            conditionFactoryType.removeFactory(factoryEntry.getTypeInfo().id());
    }

    private void addTaskFactories(Injector pluginInjector) {
        Set<Entry<RealTaskFactory<?>>> factoryEntries = getEntries(pluginInjector, RealTaskFactory.class);
        for(Entry<RealTaskFactory<?>> factoryEntry : factoryEntries) {
            log.d("Adding new task factory for type " + factoryEntry.getTypeInfo().id());
            taskFactoryType.addFactory(factoryEntry.getTypeInfo().id(),
                    factoryEntry.getTypeInfo().name(), factoryEntry.getTypeInfo().description(),
                    factoryEntry.getInjector().getInstance(factoryEntry.getFactoryKey()));
        }
    }

    private void removeTaskFactories(Injector pluginInjector) {
        Set<Entry<RealTaskFactory<?>>> factoryEntries = getEntries(pluginInjector, RealTaskFactory.class);
        for(Entry<RealTaskFactory<?>> factoryEntry : factoryEntries)
            taskFactoryType.removeFactory(factoryEntry.getTypeInfo().id());
    }
    
    private <T, C> Set<Entry<T>> getEntries(Injector injector, Class<C> factoryClass) {
        Set<Entry<T>> result = Sets.newHashSet();
        for(Map.Entry<Key<?>, Binding<?>> entry : injector.getAllBindings().entrySet()) {
            TypeLiteral<?> typeLiteral = entry.getKey().getTypeLiteral();
            if(factoryClass.isAssignableFrom(typeLiteral.getRawType())) {
                String typeClassName = typeLiteral.toString().substring(typeLiteral.toString().indexOf("<") + 1, typeLiteral.toString().length() - 1);
                if(typeClassName.startsWith("?"))
                    continue;
                Class<?> typeClass;
                try {
                    typeClass = Class.forName(typeClassName, true, injector.getInstance(ClassLoader.class));
                } catch (ClassNotFoundException e) {
                    log.e("Could not find type class " + typeClassName);
                    continue;
                }
                TypeInfo typeInfo = typeClass.getAnnotation(TypeInfo.class);
                if(typeInfo != null)
                    result.add(new Entry<T>(injector, typeInfo, (Key<T>) entry.getKey()));
                else
                    log.e("Factory class " + typeClass.getName() + " has no " + TypeInfo.class.getName() + " annotation");
            }
        }
        return result;
    }

    private class Entry<T> {

        private final Injector injector;
        private final TypeInfo typeInfo;
        private final Key<T> factoryKey;

        public Entry(Injector injector, TypeInfo typeInfo, Key<T> factoryKey) {
            this.injector = injector;
            this.typeInfo = typeInfo;
            this.factoryKey = factoryKey;
        }

        public Injector getInjector() {
            return injector;
        }

        public TypeInfo getTypeInfo() {
            return typeInfo;
        }

        public Key<T> getFactoryKey() {
            return factoryKey;
        }

        @Override
        public final int hashCode() {
            return typeInfo.id().hashCode();
        }
    }
}
