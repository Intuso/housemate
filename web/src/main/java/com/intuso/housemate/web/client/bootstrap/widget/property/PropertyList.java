package com.intuso.housemate.web.client.bootstrap.widget.property;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.object.GWTProxyProperty;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class PropertyList extends NestedList<PropertyData, GWTProxyProperty> {

    @Override
    protected Widget getWidget(ChildOverview childOverview, final GWTProxyProperty property) {
        return new Property(property);
    }
}
