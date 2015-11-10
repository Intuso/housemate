package com.intuso.housemate.client.real.impl.internal;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealParameter;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.api.internal.RealUser;
import com.intuso.housemate.client.real.api.internal.type.Email;
import com.intuso.housemate.client.real.impl.internal.type.EmailType;
import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.UserData;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.User;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

public class RealUserImpl
        extends RealObject<UserData, HousemateData<?>, RealObject<?, ? ,?, ?>, User.Listener<? super RealUser>>
        implements RealUser {

    private final RealCommandImpl remove;
    private final RealPropertyImpl<Email> emailProperty;

    /**
     * @param log {@inheritDoc}
     * @param data the object's data
     */
    @Inject
    public RealUserImpl(final Log log,
                        ListenersFactory listenersFactory,
                        @Assisted UserData data,
                        @Assisted final RemoveCallback removeCallback) {
        super(log, listenersFactory, data);
        this.remove = new RealCommandImpl(log, listenersFactory, UserData.REMOVE_ID, UserData.REMOVE_ID, "Remove the user", Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) {
                removeCallback.removeUser(RealUserImpl.this);
            }
        };
        this.emailProperty = new RealPropertyImpl<>(log, listenersFactory, UserData.EMAIL_ID, UserData.EMAIL_ID, "The user's email address", new EmailType(log, listenersFactory), (Email)null);
        addChild(remove);
        addChild(emailProperty);
    }

    @Override
    public RealCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public RealProperty<Email> getEmailProperty() {
        return emailProperty;
    }
}
