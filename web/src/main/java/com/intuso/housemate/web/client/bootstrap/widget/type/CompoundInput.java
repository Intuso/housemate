package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.type.CompoundTypeWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.web.client.event.TypeInputEditedEvent;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class CompoundInput
        extends SubTypeInputList
        implements TypeInput {

    private final GWTProxyType type;
    private TypeInstance typeInstance;

    public CompoundInput(CompoundTypeWrappable typeWrappable, GWTProxyType type) {
        this.type = type;
    }

    @Override
    public void setTypeInstance(TypeInstance typeInstance) {
        this.typeInstance = typeInstance != null ? typeInstance : new TypeInstance();
        setTypeInstances(this.typeInstance.getChildValues());
        List<GWTProxySubType> list = (List<GWTProxySubType>) type.getWrapper("sub-types");
        if(list != null)
            setList(list);
    }

    @Override
    public void onTypeInputEdited(TypeInputEditedEvent event) {
        fireEvent(new TypeInputEditedEvent(typeInstance));
    }
}