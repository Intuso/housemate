package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.TestRealRoot;
import com.intuso.housemate.api.resources.Resources;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 05/03/12
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
public class ProxyCommsImpl extends Comms {

    private TestRealRoot realRoot;

    public ProxyCommsImpl(Resources resources) {
        super(resources);
        connect(null, null);
    }

    public void setRealRoot(TestRealRoot realRoot) {
        this.realRoot = realRoot;
    }

    @Override
    public void disconnect() {
        // do nothing
    }

    @Override
    public void sendMessage(Message message) {
        try {
            if(realRoot != null)
                realRoot.distributeMessage(message);
        } catch(HousemateException e) {
            log.e("Could not send message to real root");
            log.st(e);
        }
    }
}