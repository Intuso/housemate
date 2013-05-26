package com.intuso.housemate.web.client.bootstrap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.intuso.housemate.web.client.ClientFactory;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.activity.HousemateActivityMapper;
import com.intuso.housemate.web.client.bootstrap.view.AccountView;
import com.intuso.housemate.web.client.bootstrap.view.ConditionView;
import com.intuso.housemate.web.client.bootstrap.view.DeviceView;
import com.intuso.housemate.web.client.bootstrap.view.HomeView;
import com.intuso.housemate.web.client.bootstrap.view.Page;
import com.intuso.housemate.web.client.bootstrap.view.RuleView;
import com.intuso.housemate.web.client.bootstrap.view.SatisfiedConsequenceView;
import com.intuso.housemate.web.client.bootstrap.view.UnsatisfiedConsequenceView;
import com.intuso.housemate.web.client.bootstrap.view.UserView;
import com.intuso.housemate.web.client.bootstrap.widget.login.LoginPopup;
import com.intuso.housemate.web.client.place.HousematePlaceHistoryMapper;
import com.intuso.housemate.web.client.ui.view.LoginView;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
public class BootstrapClientFactory implements ClientFactory {

    // general things
    private EventBus eventBus = new SimpleEventBus();

    // activity/place stuff
    private HousemateActivityMapper activityMapper = new HousemateActivityMapper();
    private HousematePlaceHistoryMapper placeHistoryMapper = GWT.create(HousematePlaceHistoryMapper.class);
    private PlaceHistoryHandler placeHistoryHandler = new PlaceHistoryHandler(placeHistoryMapper);
    private PlaceController placeController = new PlaceController(eventBus);

    // ui
    private LoginPopup loginPopup;
    private Page page = null;
    private HomeView homeView = null;
    private UserView userView = null;
    private DeviceView deviceView = null;
    private RuleView ruleView = null;
    private ConditionView conditionView = null;
    private SatisfiedConsequenceView satisfiedConsequenceView = null;
    private UnsatisfiedConsequenceView unsatisfiedConsequenceView = null;
    private AccountView accountView = null;

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public HousemateActivityMapper getActivityMapper() {
        return activityMapper;
    }

    @Override
    public HousematePlaceHistoryMapper getPlaceHistoryMapper() {
        return placeHistoryMapper;
    }

    @Override
    public PlaceHistoryHandler getPlaceHistoryHandler() {
        return placeHistoryHandler;
    }

    @Override
    public PlaceController getPlaceController() {
        return placeController;
    }

    @Override
    public LoginView getLoginView() {
        if(loginPopup == null)
            loginPopup = new LoginPopup();
        return loginPopup;
    }

    @Override
    public com.intuso.housemate.web.client.ui.view.Page getPage() {
        if(page == null)
            page = new Page();
        return page;
    }

    @Override
    public com.intuso.housemate.web.client.ui.view.HomeView getHomeView() {
        if(homeView == null)
            homeView = new HomeView(Housemate.ENVIRONMENT.getResources());
        return homeView;
    }

    @Override
    public com.intuso.housemate.web.client.ui.view.UserView getUserView() {
        if(userView == null)
            userView = new UserView(Housemate.ENVIRONMENT.getResources());
        return userView;
    }

    @Override
    public com.intuso.housemate.web.client.ui.view.DeviceView getDeviceView() {
        if(deviceView == null)
            deviceView = new DeviceView(Housemate.ENVIRONMENT.getResources());
        return deviceView;
    }

    @Override
    public com.intuso.housemate.web.client.ui.view.RuleView getRuleView() {
        if(ruleView == null)
            ruleView = new RuleView(Housemate.ENVIRONMENT.getResources());
        return ruleView;
    }

    @Override
    public com.intuso.housemate.web.client.ui.view.ConditionView getConditionView() {
        if(conditionView == null)
            conditionView = new ConditionView(Housemate.ENVIRONMENT.getResources());
        return conditionView;
    }

    @Override
    public com.intuso.housemate.web.client.ui.view.SatisfiedConsequenceView getSatisfiedConsequenceView() {
        if(satisfiedConsequenceView == null)
            satisfiedConsequenceView = new SatisfiedConsequenceView(Housemate.ENVIRONMENT.getResources());
        return satisfiedConsequenceView;
    }

    @Override
    public com.intuso.housemate.web.client.ui.view.UnsatisfiedConsequenceView getUnsatisfiedConsequenceView() {
        if(unsatisfiedConsequenceView == null)
            unsatisfiedConsequenceView = new UnsatisfiedConsequenceView(Housemate.ENVIRONMENT.getResources());
        return unsatisfiedConsequenceView;
    }

    @Override
    public com.intuso.housemate.web.client.ui.view.AccountView getAccountView() {
        if(accountView == null)
            accountView = new AccountView();
        return accountView;
    }
}