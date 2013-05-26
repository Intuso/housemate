package com.intuso.housemate.web.client.handler;

import com.google.gwt.event.shared.EventHandler;
import com.intuso.housemate.web.client.event.CredentialsSubmittedEvent;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 28/03/12
 * Time: 00:00
 * To change this template use File | Settings | File Templates.
 */
public interface CredentialsSubmittedHandler extends EventHandler {
    public void onCredentialsSubmitted(CredentialsSubmittedEvent event);
}