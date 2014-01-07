package com.intuso.housemate.server.object.bridge;

import com.google.common.base.Function;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.user.User;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.api.object.user.UserListener;
import com.intuso.housemate.object.server.proxy.ServerProxyType;
import com.intuso.utilities.log.Log;

/**
 */
public class UserBridge
        extends BridgeObject<UserData, HousemateData<?>, BridgeObject<?, ?, ?, ?, ?>, UserBridge, UserListener>
        implements User<CommandBridge> {

    private final CommandBridge removeCommand;

    public UserBridge(Log log, User user,
                      ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
        super(log, new UserData(user.getId(), user.getName(), user.getDescription()));
        removeCommand = new CommandBridge(log, user.getRemoveCommand(), types);
        addChild(removeCommand);
    }

    @Override
    public CommandBridge getRemoveCommand() {
        return removeCommand;
    }

    public final static class Converter implements Function<User<?>, UserBridge> {

        private final Log log;
        private final ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types;

        public Converter(Log log, ListBridge<TypeData<?>, ServerProxyType, TypeBridge> types) {
            this.log = log;
            this.types = types;
        }

        @Override
        public UserBridge apply(User<?> user) {
            return new UserBridge(log, user, types);
        }
    }
}
