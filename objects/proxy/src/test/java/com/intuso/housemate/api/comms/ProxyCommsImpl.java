package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.TestRealRoot;
import com.intuso.housemate.api.resources.Resources;

/**
 */
public class ProxyCommsImpl extends Router {

    private TestRealRoot realRoot;

    public ProxyCommsImpl(Resources resources) {
        super(resources);
        connect();
        setRouterStatus(Status.Connected);
        login(null);
    }

    public void setRealRoot(TestRealRoot realRoot) {
        this.realRoot = realRoot;
    }

    @Override
    public final void connect() {
        // do nothing
    }

    @Override
    public final void disconnect() {
        // do nothing
    }

    @Override
    public void sendMessage(Message message) {
        try {
            if(realRoot != null)
                realRoot.distributeMessage(message);
        } catch(HousemateException e) {
            getLog().e("Could not send message to real root");
            getLog().st(e);
        }
    }
}