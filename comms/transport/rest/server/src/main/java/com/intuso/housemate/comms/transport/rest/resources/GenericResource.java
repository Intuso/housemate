package com.intuso.housemate.comms.transport.rest.resources;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.comms.Receiver;
import com.intuso.housemate.api.comms.Router;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/10/13
 * Time: 09:23
 * To change this template use File | Settings | File Templates.
 */
@Path("/generic")
public class GenericResource {

    private final Router router;

    public GenericResource(Router router) {
        this.router = router;
    }

    @Path("/sendMessage")
    @POST
    public void sendMessage(@Context HttpServletRequest request,
                            @QueryParam("path") String path,
                            @QueryParam("type") String type,
                            Message.Payload payload) {
        getRouterRegistration(request).sendMessage(new Message(path.split("/"), type, payload));
    }

    @Path("/getMessage")
    @GET
    public Message<Message.Payload> getMessage(@Context HttpServletRequest request) {
        MessageCache cache = getMessageCache(request);
        return cache != null ? cache.getMessage() : null;
    }

    private Router.Registration getRouterRegistration(HttpServletRequest request) {
        if(request.getAttribute("registration") == null) {
            MessageCache cache = new MessageCache();
            request.setAttribute("cache", cache);
            request.setAttribute("registration", router.registerReceiver(cache));
        }
        return (Router.Registration) request.getAttribute("registration");
    }

    private MessageCache getMessageCache(HttpServletRequest request) {
        return (MessageCache)request.getAttribute("cache");
    }

    private class MessageCache implements Receiver<Message.Payload> {

        private LinkedBlockingQueue<Message<Message.Payload>> cache = new LinkedBlockingQueue<Message<Message.Payload>>();

        @Override
        public synchronized void messageReceived(Message<Message.Payload> message) throws HousemateException {
            try {
                cache.put(message);
            } catch (InterruptedException e) {
                throw new HousemateException("Failed to cache message for client");
            }
        }

        private Message<Message.Payload> getMessage() {
            try {
                return cache.poll(50, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                return null;
            }
        }
    }
}