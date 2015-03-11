package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.object.GWTProxyValue;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class ValueList extends NestedList<ValueData, GWTProxyValue> {

    @Override
    protected IsWidget getWidget(ChildOverview childOverview, GWTProxyValue value) {
        return ValueDisplay.FACTORY.create(value);
    }
}
