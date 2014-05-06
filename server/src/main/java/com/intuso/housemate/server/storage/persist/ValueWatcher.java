package com.intuso.housemate.server.storage.persist;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.utilities.log.Log;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:25
* To change this template use File | Settings | File Templates.
*/
public class ValueWatcher implements ValueListener<Value<?, ?>> {

    private final Log log;
    private final Persistence persistence;

    @Inject
    public ValueWatcher(Log log, Persistence persistence) {
        this.log = log;
        this.persistence = persistence;
    }


    @Override
    public void valueChanging(Value<?, ?> value) {
        // do nothing
    }

    @Override
    public void valueChanged(Value<?, ?> value) {
        try {
            persistence.saveTypeInstances(value.getPath(), value.getTypeInstances());
        } catch(HousemateException e) {
            log.e("Failed to save property value", e);
        }
    }
}
