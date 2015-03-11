package com.intuso.housemate.web.client.bootstrap.view;

import com.google.common.collect.Lists;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import com.intuso.housemate.web.client.bootstrap.widget.automation.AutomationList;
import com.intuso.housemate.web.client.bootstrap.widget.list.AddButton;
import com.intuso.housemate.web.client.handler.MultiListSelectedIdsChangedHandler;
import com.intuso.housemate.web.client.handler.SelectedIdsChangedHandler;
import com.intuso.housemate.web.client.object.GWTProxyRoot;
import com.intuso.housemate.web.client.place.AutomationsPlace;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/09/13
 * Time: 00:15
 * To change this template use File | Settings | File Templates.
 */
public class AutomationsView extends FlowPanel
        implements com.intuso.housemate.web.client.ui.view.AutomationsView, SelectedIdsChangedHandler {

    private final PlaceHistoryMapper placeHistoryMapper;
    private final AutomationList favouritesList;
    private final AutomationList allList;
    private final MultiListSelectedIdsChangedHandler selectedIdsChangedHandler;

    @Inject
    public AutomationsView(PlaceHistoryMapper placeHistoryMapper, GWTProxyRoot root) {

        this.placeHistoryMapper = placeHistoryMapper;

        selectedIdsChangedHandler = new MultiListSelectedIdsChangedHandler(this);

        List<String> favourites = Lists.newArrayList();
        favouritesList = new AutomationList("favourites", favourites, true);
        favouritesList.addSelectedIdsChangedHandler(selectedIdsChangedHandler);
        allList = new AutomationList(favourites.size() > 0 ? "all" : "", favourites, false);
        allList.addSelectedIdsChangedHandler(selectedIdsChangedHandler);
        
        add(favouritesList);
        add(allList);
        add(new AddButton(root.getAddAutomationCommand()));
    }

    @Override
    public void selectedIdsChanged(Set<String> ids) {
        History.newItem(placeHistoryMapper.getToken(new AutomationsPlace(ids)), false);
    }

    @Override
    public void newPlace(AutomationsPlace place) {
        favouritesList.setSelected(place.getAutomationIds());
        allList.setSelected(place.getAutomationIds());
    }
}
