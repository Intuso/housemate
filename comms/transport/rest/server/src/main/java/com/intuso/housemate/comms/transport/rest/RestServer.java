package com.intuso.housemate.comms.transport.rest;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Router;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.comms.transport.rest.resources.ContextualResource;
import com.intuso.housemate.comms.transport.rest.resources.GenericResource;
import com.intuso.housemate.plugin.api.ExternalClientRouter;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.PropertyContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/10/13
 * Time: 09:22
 * To change this template use File | Settings | File Templates.
 */
public class RestServer extends ExternalClientRouter {

    public final static String PORT = "rest.server.port";

    private final Injector injector;
    private final Router.Registration routerRegistration;
    private final Server server;

    @Inject
    public RestServer(Log log, PropertyContainer properties, Injector injector, Router router) {

        super(log);

        this.injector = injector;
        this.routerRegistration = router.registerReceiver(this);

        setRouterStatus(Status.Connected);

        String port = properties.get(PORT);
        if(port == null) {
            log.d("Rest server port not set, using default");
            port = "46872";
        }

        log.d("Creating REST server on port " + port);
        server = new Server(Integer.parseInt(port));

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("");
        // adds Jersey Servlet with a customized ResourceConfig
        handler.addServlet(new ServletHolder(new ServletContainer(resourceConfig())), "/*");
        server.setHandler(handler);
    }

    @Override
    public void connect() {
        // do nothing
    }

    @Override
    public void disconnect() {
        // do nothing
    }

    @Override
    public void sendMessage(Message<?> message) {
        routerRegistration.sendMessage(message);
    }

    public void start() throws HousemateException {
        try {
            server.start();
        } catch (Exception e) {
            getLog().e("Could not start the server", e);
            throw new HousemateException("Could not start the REST server", e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch(Exception e) {
            getLog().e("Could not stop the REST server", e);
        }
    }

    private ResourceConfig resourceConfig() {
        return new ResourceConfig()
                .register(new GenericResource(this))
                .register(new ContextualResource(injector));
    }

    public static class RM implements RegexMatcher {

        private final Pattern pattern;

        public RM(String regexPattern) {
            pattern = Pattern.compile(regexPattern);
        }

        @Override
        public boolean matches(String value) {
            return pattern.matcher(value).matches();
        }
    }
}
