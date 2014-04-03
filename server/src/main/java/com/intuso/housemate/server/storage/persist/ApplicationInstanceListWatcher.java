package com.intuso.housemate.server.storage.persist;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.ApplicationInstanceStatus;
import com.intuso.housemate.api.object.application.instance.ApplicationInstance;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.server.storage.DetailsNotFoundException;
import com.intuso.housemate.server.storage.Storage;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/02/14
 * Time: 19:51
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationInstanceListWatcher implements ListListener<ApplicationInstance<?, ?, ?>> {

    private final Map<ApplicationInstance<?, ?, ?>, ListenerRegistration> listeners = Maps.newHashMap();

    private final Log log;
    private final Storage storage;
    private final ValueWatcher valueWatcher;

    @Inject
    public ApplicationInstanceListWatcher(Log log, Storage storage, ValueWatcher valueWatcher) {
        this.log = log;
        this.storage = storage;
        this.valueWatcher = valueWatcher;
    }

    @Override
    public void elementAdded(ApplicationInstance<?, ?, ?> applicationInstance) {
        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.put("id", new TypeInstances(new TypeInstance(applicationInstance.getId())));
        toSave.put("name", new TypeInstances(new TypeInstance(applicationInstance.getName())));
        toSave.put("description", new TypeInstances(new TypeInstance(applicationInstance.getDescription())));
        try {
            storage.saveValues(applicationInstance.getPath(), toSave);
        } catch (HousemateException e) {
            log.e("Failed to save new application values", e);
        }
        listeners.put(applicationInstance, applicationInstance.getStatusValue().addObjectListener(valueWatcher));
        try {
            TypeInstances instances = storage.getTypeInstances(applicationInstance.getStatusValue().getPath());
            if(instances.getFirstValue() != null) {
                ApplicationInstanceStatus applicationStatus = ApplicationInstanceStatus.Allowed.valueOf(instances.getFirstValue());
                if(applicationStatus == ApplicationInstanceStatus.Allowed)
                    applicationInstance.getAllowCommand().perform(new TypeInstanceMap(),
                            new CommandPerformListener(log, "Allow application instance \"" + applicationInstance.getId() + "\""));
                else if(applicationStatus == ApplicationInstanceStatus.Rejected)
                    applicationInstance.getRejectCommand().perform(new TypeInstanceMap(),
                            new CommandPerformListener(log, "Reject application instance \"" + applicationInstance.getId() + "\""));
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for whether the application was previously accepted" + Arrays.toString(applicationInstance.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to check value for whether the application was previously accepted", e);
        }
    }

    @Override
    public void elementRemoved(ApplicationInstance<?, ?, ?> applicationInstance) {
        ListenerRegistration registration = listeners.remove(applicationInstance);
        if(registration != null)
            registration.removeListener();
        try {
            storage.removeValues(applicationInstance.getPath());
        } catch(HousemateException e) {
            log.e("Failed to delete automation properties", e);
        }
    }
}