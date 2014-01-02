package com.intuso.housemate.web.client.bootstrap.view;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.automation.AutomationList;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.handler.MultiListSelectedIdsChangedHandler;
import com.intuso.housemate.web.client.handler.SelectedIdsChangedHandler;
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
    
    private final AutomationList favouritesList;
    private final AutomationList allList;
    private final MultiListSelectedIdsChangedHandler selectedIdsChangedHandler;
    
    public AutomationsView() {

        selectedIdsChangedHandler = new MultiListSelectedIdsChangedHandler(this);

        List<String> favourites = Lists.newArrayList();
        favouritesList = new AutomationList("favourites", favourites, true);
        favouritesList.addSelectedIdsChangedHandler(selectedIdsChangedHandler);
        allList = new AutomationList(favourites.size() > 0 ? "all" : "", favourites, false);
        allList.addSelectedIdsChangedHandler(selectedIdsChangedHandler);
        
        add(favouritesList);
        add(allList);
        Button addButton = new PerformButton(Housemate.ENVIRONMENT.getResources().getRoot().getAddAutomationCommand(), IconType.PLUS);
        addButton.setSize(ButtonSize.SMALL);
        add(addButton);
    }

    @Override
    public void selectedIdsChanged(Set<String> ids) {
        History.newItem(Housemate.FACTORY.getPlaceHistoryMapper().getToken(new AutomationsPlace(ids)), false);
    }

    @Override
    public void newPlace(AutomationsPlace place) {
        favouritesList.setSelected(place.getAutomationIds());
        allList.setSelected(place.getAutomationIds());
    }
}