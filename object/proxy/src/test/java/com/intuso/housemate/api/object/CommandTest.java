package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.TestEnvironment;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 14/07/12
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */
public class CommandTest {

    public final static String COMMANDS = "commands";

    private static CommandListener<SimpleProxyObject.Command> EMPTY_FUNCTION_LISTENER = new CommandListener<SimpleProxyObject.Command>() {
        @Override
        public void commandStarted(SimpleProxyObject.Command function) {}

        @Override
        public void commandFinished(SimpleProxyObject.Command function) {}

        @Override
        public void commandFailed(SimpleProxyObject.Command function, String error) {}
    };

    private SimpleProxyObject.List<CommandWrappable, SimpleProxyObject.Command> proxyList
            = new SimpleProxyObject.List<CommandWrappable, SimpleProxyObject.Command>(
            SimpleProxyFactory.changeFactoryType(TestEnvironment.TEST_INSTANCE.getProxyResources(), new SimpleProxyFactory.Command()),
            TestEnvironment.TEST_INSTANCE.getProxyResources(),
            new ListWrappable(COMMANDS, COMMANDS, COMMANDS));
    private RealList<CommandWrappable, RealCommand> realList = new RealList<CommandWrappable, RealCommand>(TestEnvironment.TEST_INSTANCE.getRealResources(), COMMANDS, COMMANDS, COMMANDS, new ArrayList<RealCommand>());
    private RealCommand realCommand;
    private SimpleProxyObject.Command proxyCommand;

    public CommandTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addWrapper(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realCommand = new RealCommand(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-command", "My Command", "description", new ArrayList<RealParameter<?>>()) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        realList.add(realCommand);
        proxyCommand = proxyList.get("my-command");
    }

    @Test
    public void testCreateProxyCommand() throws HousemateException {
        assertNotNull(proxyCommand);
    }

    @Test
    public void testPerformProxyFunction() throws HousemateException {
        final AtomicBoolean called = new AtomicBoolean(false);
        RealCommand realCommand = new RealCommand(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-other-command", "My Other Command", "description", new ArrayList<RealParameter<?>>()) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                called.set(true);
            }
        };
        realList.add(realCommand);
        SimpleProxyObject.Command proxyCommand = proxyList.get("my-other-command");
        proxyCommand.perform(new TypeInstances(), EMPTY_FUNCTION_LISTENER);
        assertEquals(true, called.get());
    }

    @Test
    public void testParameter() throws HousemateException {
        final AtomicBoolean correctParam = new AtomicBoolean(false);
        RealCommand realCommand = new RealCommand(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-other-command", "My Other Command", "description",
                Arrays.<RealParameter<?>>asList(IntegerType.createParameter(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-parameter", "My Parameter", "description"))) {
            @Override
            public void perform(TypeInstances values) throws HousemateException {
                correctParam.set(values.get("my-parameter") != null
                        && values.get("my-parameter").getValue() != null
                        && values.get("my-parameter").getValue().equals("1234"));
            }
        };
        realList.add(realCommand);
        SimpleProxyObject.Command proxyCommand = proxyList.get("my-other-command");
        proxyCommand.perform(new TypeInstances() {
            {
                put("my-parameter", new TypeInstance("1234"));
            }
        }, EMPTY_FUNCTION_LISTENER);
        assertEquals(true, correctParam.get());
    }

    @Test
    public void testPerformListenerCalled() throws HousemateException {
        final AtomicBoolean functionStartedCalled = new AtomicBoolean(false);
        final AtomicBoolean functionFinishedCalled = new AtomicBoolean(false);
        proxyCommand.perform(new TypeInstances(), new CommandListener<SimpleProxyObject.Command>() {
            @Override
            public void commandStarted(SimpleProxyObject.Command function) {
                functionStartedCalled.set(true);
            }

            @Override
            public void commandFinished(SimpleProxyObject.Command function) {
                functionFinishedCalled.set(true);
            }

            @Override
            public void commandFailed(SimpleProxyObject.Command function, String error) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        assertEquals(true, functionStartedCalled.get());
        assertEquals(true, functionFinishedCalled.get());
    }
}
