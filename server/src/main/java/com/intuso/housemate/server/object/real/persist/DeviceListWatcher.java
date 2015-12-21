package com.intuso.housemate.server.object.real.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealDevice;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.persistence.api.internal.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.utilities.listener.ListenerRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:24
* To change this template use File | Settings | File Templates.
*/
public class DeviceListWatcher implements List.Listener<RealDevice<?>> {

    private final static Logger logger = LoggerFactory.getLogger(DeviceListWatcher.class);

    private final Multimap<RealDevice, ListenerRegistration> listeners = HashMultimap.create();
    private final Persistence persistence;
    private final ValueBaseWatcher valueBaseWatcher;
    private final PropertyListWatcher propertyListWatcher;
    private final DeviceListener deviceListener;

    @Inject
    public DeviceListWatcher(Persistence persistence, ValueBaseWatcher valueBaseWatcher, PropertyListWatcher propertyListWatcher, DeviceListener deviceListener) {
        this.persistence = persistence;
        this.valueBaseWatcher = valueBaseWatcher;
        this.propertyListWatcher = propertyListWatcher;
        this.deviceListener = deviceListener;
    }

    @Override
    public void elementAdded(RealDevice<?> device) {

        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.getChildren().put("id", new TypeInstances(new TypeInstance(device.getId())));
        toSave.getChildren().put("name", new TypeInstances(new TypeInstance(device.getName())));
        toSave.getChildren().put("description", new TypeInstances(new TypeInstance(device.getDescription())));
        try {
            persistence.saveValues(device.getPath(), toSave);
        } catch (Throwable t) {
            logger.error("Failed to save new device values", t);
        }
        listeners.put(device, valueBaseWatcher.watch(device.getDriverProperty()));
        listeners.put(device, device.addObjectListener(deviceListener));
        listeners.put(device, valueBaseWatcher.watch(device.getRunningValue()));
        listeners.put(device, device.getProperties().addObjectListener(propertyListWatcher, true));
        try {
            TypeInstances instances = persistence.getTypeInstances(device.getRunningValue().getPath());
            if(instances.getElements().size() > 0 && BooleanType.SERIALISER.deserialise(instances.getElements().get(0)))
                device.getStartCommand().perform(new TypeInstanceMap(),
                        new CommandPerformListener(logger, "Start device \"" + device.getId() + "\""));
        } catch(DetailsNotFoundException e) {
            logger.warn("No details found for whether the device was previously running" + Arrays.toString(device.getPath()));
        } catch(Throwable t) {
            logger.error("Failed to check value for whether the device was previously running", t);
        }
    }

    @Override
    public void elementRemoved(RealDevice<?> device) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(device);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
        try {
            persistence.removeValues(device.getPath());
        } catch(Throwable t) {
            logger.error("Failed to delete device properties", t);
        }
    }
}
