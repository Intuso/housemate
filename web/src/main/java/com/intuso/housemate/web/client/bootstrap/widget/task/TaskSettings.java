package com.intuso.housemate.web.client.bootstrap.widget.task;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyTask;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class TaskSettings extends Composite {

    interface TaskUiBinder extends UiBinder<Widget, TaskSettings> {}

    private static TaskUiBinder ourUiBinder = GWT.create(TaskUiBinder.class);

    @UiField(provided = true)
    Control control;
    @UiField
    PropertyList propertyList;

    public TaskSettings(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyTask task) {
        control = new Control(types, task);
        initWidget(ourUiBinder.createAndBindUi(this));
        propertyList.setTypes(types);
        propertyList.setList(task.getProperties());
    }
}
