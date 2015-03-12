package com.intuso.housemate.web.client.bootstrap.widget.automation;

import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.automation.AutomationData;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.LazyLoadedWidgetCallback;
import com.intuso.housemate.web.client.bootstrap.widget.list.MainList;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class AutomationList extends MainList<AutomationData, GWTProxyAutomation> {
    
    public AutomationList(String title, List<String> filteredIds, boolean includeFiltered) {
        super(Housemate.INJECTOR.getProxyRoot().getAutomations(), title, filteredIds, includeFiltered);
    }

    @Override
    protected void getWidget(ChildOverview childOverview, LazyLoadedWidgetCallback callback) {
        callback.widgetReady(new Automation(childOverview));
    }
}
