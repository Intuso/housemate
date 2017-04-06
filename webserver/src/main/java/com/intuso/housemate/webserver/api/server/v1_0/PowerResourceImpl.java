package com.intuso.housemate.webserver.api.server.v1_0;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.api.type.serialiser.BooleanSerialiser;
import com.intuso.housemate.client.v1_0.proxy.object.*;
import com.intuso.housemate.client.v1_0.rest.PowerResource;
import com.intuso.housemate.client.v1_0.rest.model.Page;
import com.intuso.housemate.webserver.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tomc on 23/01/17.
 */
public class PowerResourceImpl implements PowerResource {

    private final static Logger logger = LoggerFactory.getLogger(PowerResourceImpl.class);

    private final Command.PerformListener<ProxyCommand.Simple> loggerListener = new Command.PerformListener<ProxyCommand.Simple>() {
        @Override
        public void commandStarted(ProxyCommand.Simple command) {
            logger.debug("Started perform of {}", command);
        }

        @Override
        public void commandFinished(ProxyCommand.Simple command) {
            logger.debug("Finished perform of {}", command);
        }

        @Override
        public void commandFailed(ProxyCommand.Simple command, String error) {
            logger.debug("Failed perform of {} because {}", command, error);
        }
    };

    @Context private HttpServletRequest request;

    @Override
    public Page<Device.Data> list(int offset, int limit) {
        logger.debug("Listing devices {} to {}", offset, limit);
        List<Device.Data> devices = Lists.newArrayList();
        for(ProxyNode.Simple node : SessionUtils.getServer(request.getSession(false)).getNodes())
            for(ProxyHardware.Simple hardware : node.getHardwares())
                for(ProxyDevice.Simple device : hardware.getDevices())
                  devices.add(new Device.Data(device.getId(), device.getName(), device.getDescription()));

        Stream<Device.Data> stream  = devices.stream();
        if(offset > 0)
            stream = stream.skip(offset);
        if(limit >= 0)
            stream = stream.limit(limit);
        return new Page<>(offset, devices.size(), stream.collect(Collectors.toList()));
    }

    @Override
    public boolean isOn(String id) {
        logger.debug("Is on {}", id);
        for(ProxyNode.Simple node : SessionUtils.getServer(request.getSession(false)).getNodes())
            for(ProxyHardware.Simple hardware : node.getHardwares())
                for(ProxyDevice.Simple device : hardware.getDevices())
                    if(device.getId().equals(id))
                        return BooleanSerialiser.INSTANCE.deserialise(device.getValues().get("on").getValue().getElements().get(0));
        return false;
    }

    @Override
    public void turnOn(String id) {
        logger.debug("Turning on {}", id);
        for(ProxyNode.Simple node : SessionUtils.getServer(request.getSession(false)).getNodes()) {
            for(ProxyHardware.Simple hardware : node.getHardwares()) {
                for(ProxyDevice.Simple device : hardware.getDevices()) {
                    if (device.getId().equals(id)) {
                        device.getCommands().get("on").perform(loggerListener);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void turnOff(String id) {
        logger.debug("Turning off {}", id);
        for(ProxyNode.Simple node : SessionUtils.getServer(request.getSession(false)).getNodes()) {
            for(ProxyHardware.Simple hardware : node.getHardwares()) {
                for(ProxyDevice.Simple device : hardware.getDevices()) {
                    if (device.getId().equals(id)) {
                        device.getCommands().get("off").perform(loggerListener);
                        return;
                    }
                }
            }
        }
    }
}
