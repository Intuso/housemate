package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.web.client.handler.HasTypeInputEditedHandlers;

/**
 */
public interface TypeInput extends HasTypeInputEditedHandlers, IsWidget {
    public void setTypeInstance(TypeInstance typeInstance);
}