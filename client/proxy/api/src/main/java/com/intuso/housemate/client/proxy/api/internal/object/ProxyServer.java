package com.intuso.housemate.client.proxy.api.internal.object;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Server;
import com.intuso.housemate.client.proxy.api.internal.ChildUtil;
import com.intuso.housemate.client.proxy.api.internal.ProxyRenameable;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.Arrays;

/**
 * @param <COMMAND> the type of the command
 * @param <AUTOMATIONS> the type of the automations list
 * @param <SYSTEMS> the type of the systems list
 * @param <USERS> the type of the users list
 * @param <NODES> the type of the nodes list
 * @param <SERVER> the type of the server
 */
public abstract class ProxyServer<
        COMMAND extends ProxyCommand<?, ?, ?>,
        AUTOMATIONS extends ProxyList<? extends ProxyAutomation<?, ?, ?, ?, ?>, ?>,
        SYSTEMS extends ProxyList<? extends ProxySystem<?, ?, ?, ?>, ?>,
        USERS extends ProxyList<? extends ProxyUser<?, ?, ?>, ?>,
        NODES extends ProxyList<? extends ProxyNode<?, ?, ?, ?>, ?>,
        SERVER extends ProxyServer<COMMAND, AUTOMATIONS, SYSTEMS, USERS, NODES, SERVER>>
        extends ProxyObject<Server.Data, Server.Listener<? super SERVER>>
        implements Server<COMMAND, AUTOMATIONS, SYSTEMS, USERS, NODES, SERVER>,
        ProxyRenameable<COMMAND> {

    private final Connection connection;

    private final COMMAND renameCommand;
    private final AUTOMATIONS automations;
    private final COMMAND addAutomationCommand;
    private final SYSTEMS SYSTEMS;
    private final COMMAND addSystemCommand;
    private final USERS users;
    private final COMMAND addUserCommand;
    private final NODES nodes;

    @Inject
    public ProxyServer(Connection connection,
                       Logger logger,
                       ManagedCollectionFactory managedCollectionFactory,
                       Factory<COMMAND> commandFactory,
                       Factory<AUTOMATIONS> automationsFactory,
                       Factory<SYSTEMS> systemsFactory,
                       Factory<USERS> usersFactory,
                       Factory<NODES> nodesFactory) {
        super(logger, Server.Data.class, managedCollectionFactory);
        this.connection = connection;
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        automations = automationsFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID));
        addAutomationCommand = commandFactory.create(ChildUtil.logger(logger, ADD_AUTOMATION_ID));
        SYSTEMS = systemsFactory.create(ChildUtil.logger(logger, SYSTEMS_ID));
        addSystemCommand = commandFactory.create(ChildUtil.logger(logger, ADD_SYSTEM_ID));
        users = usersFactory.create(ChildUtil.logger(logger, USERS_ID));
        addUserCommand = commandFactory.create(ChildUtil.logger(logger, ADD_USER_ID));
        nodes = nodesFactory.create(ChildUtil.logger(logger, NODES_ID));
    }

    public void start() {
        try {
            init("server", connection);
        } catch(JMSException e) {
            throw new HousemateException("Failed to initalise objects");
        }
    }

    public void stop() {
        uninit();
    }

    @Override
    protected void initChildren(String name, Connection connection) throws JMSException {
        super.initChildren(name, connection);
        renameCommand.init(ChildUtil.name(name, RENAME_ID), connection);
        automations.init(ChildUtil.name(name, AUTOMATIONS_ID), connection);
        addAutomationCommand.init(ChildUtil.name(name, ADD_AUTOMATION_ID), connection);
        SYSTEMS.init(ChildUtil.name(name, SYSTEMS_ID), connection);
        addSystemCommand.init(ChildUtil.name(name, ADD_SYSTEM_ID), connection);
        users.init(ChildUtil.name(name, USERS_ID), connection);
        addUserCommand.init(ChildUtil.name(name, ADD_USER_ID), connection);
        nodes.init(ChildUtil.name(name, NODES_ID), connection);
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        automations.uninit();
        addAutomationCommand.uninit();
        SYSTEMS.uninit();
        addSystemCommand.uninit();
        users.uninit();
        addUserCommand.uninit();
        nodes.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public AUTOMATIONS getAutomations() {
        return automations;
    }

    public COMMAND getAddAutomationCommand() {
        return addAutomationCommand;
    }

    public SYSTEMS getSYSTEMS() {
        return SYSTEMS;
    }

    public COMMAND getAddSystemCommand() {
        return addSystemCommand;
    }

    @Override
    public USERS getUsers() {
        return users;
    }

    public COMMAND getAddUserCommand() {
        return addUserCommand;
    }

    @Override
    public NODES getNodes() {
        return nodes;
    }

    @Override
    public ProxyObject<?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(ADD_AUTOMATION_ID.equals(id))
            return addAutomationCommand;
        else if(ADD_SYSTEM_ID.equals(id))
            return addSystemCommand;
        else if(ADD_USER_ID.equals(id))
            return addUserCommand;
        else if(AUTOMATIONS_ID.equals(id))
            return automations;
        else if(SYSTEMS_ID.equals(id))
            return SYSTEMS;
        else if(NODES_ID.equals(id))
            return nodes;
        else if(USERS_ID.equals(id))
            return users;
        return null;
    }

    public <T extends ProxyObject<?, ?>> T find(String[] path) {
        return find(path, true);
    }

    public <T extends ProxyObject<?, ?>> T find(String[] path, boolean fail) {
        ProxyObject current = this;
        for(int i = 0; i < path.length; i++) {
            current = current.getChild(path[i]);
            if(current == null) {
                if(fail) {
                    if (i == 0)
                        throw new HousemateException("Could not find " + path[i] + " for server");
                    else
                        throw new HousemateException("Could not find " + path[i] + " at " + Joiner.on(".").join(Arrays.copyOfRange(path, 0, i)));
                } else
                    return null;
            }
        }
        return (T) current;
    }

    public static class Service extends AbstractIdleService {

        private final ProxyServer server;

        @Inject
        public Service(ProxyServer server) {
            this.server = server;
        }

        @Override
        protected void startUp() throws Exception {
            server.start();
        }

        @Override
        protected void shutDown() throws Exception {
            server.stop();
        }
    }
}
