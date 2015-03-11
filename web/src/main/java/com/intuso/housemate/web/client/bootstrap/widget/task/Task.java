package com.intuso.housemate.web.client.bootstrap.widget.task;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.object.SettingsModal;
import com.intuso.housemate.web.client.bootstrap.widget.value.BooleanValueDisplay;
import com.intuso.housemate.web.client.object.GWTProxyTask;

/**
 */
public class Task extends Composite {

    interface TaskUiBinder extends UiBinder<Widget, Task> {}

    private static TaskUiBinder ourUiBinder = GWT.create(TaskUiBinder.class);

    @UiField
    BooleanValueDisplay value;

    private final GWTProxyTask task;

    public Task(GWTProxyTask task) {
        this.task = task;
        initWidget(ourUiBinder.createAndBindUi(this));
        value.setValue(task.getExecutingValue());
    }

    @UiHandler("settings")
    public void onEdit(ClickEvent event) {
        new SettingsModal(task.getName(), new TaskSettings(task));
    }
}
