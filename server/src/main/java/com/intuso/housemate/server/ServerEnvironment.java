package com.intuso.housemate.server;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.resources.ClientResources;
import com.intuso.housemate.api.resources.RegexMatcher;
import com.intuso.housemate.object.server.proxy.ServerProxyFactory;
import com.intuso.housemate.object.server.proxy.ServerProxyRootObject;
import com.intuso.housemate.object.server.real.ServerRealRootObject;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.object.ServerProxyResourcesImpl;
import com.intuso.housemate.server.object.ServerRealResourcesImpl;
import com.intuso.housemate.server.object.bridge.ServerBridgeResources;
import com.intuso.housemate.server.object.bridge.RootObjectBridge;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.housemate.server.plugin.main.MainPlugin;
import com.intuso.housemate.server.storage.ServerObjectLoader;
import com.intuso.housemate.server.storage.impl.SjoerdDB;
import com.intuso.housemate.server.storage.impl.SjoerdDBModule;
import com.intuso.housemate.comms.transport.socket.server.SocketServer;
import com.intuso.housemate.plugin.api.PluginDescriptor;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.log.LogLevel;
import com.intuso.utilities.log.LogWriter;
import com.intuso.utilities.log.writer.FileWriter;
import com.intuso.utilities.log.writer.StdOutWriter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.FileResource;
import org.eclipse.jetty.util.resource.JarResource;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

/**
 * Platform implementation for a server. Works the same was as the PC implementation in terms of getting properties
 * and overriding based on command line arguments, Main difference is that the Comms implementation is different
 * and some methods are unsupported as they should not be used by the server.
 *
 */
@Singleton
public class ServerEnvironment {

    public final static String HOUSEMATE_CONFIG_DIR = "HOUSEMATE_CONFIG_DIR";
    public final static String HOUSEMATE_LOG_DIR = "HOUSEMATE_LOG_DIR";
    public final static String HOUSEMATE_PROPS_FILE = "housemate.props";
    public final static String PLUGINS_DIR_NAME= "plugins";
    public final static String LOG_LEVEL = "log.level";
    public final static String SERVER_NAME = "server.name";
    public final static String RUN_WEBAPP = "webapp.run";
    public final static String WEBAPP_PORT = "webapp.port";
    private final static String WEBAPP_PATH = "webapp.path";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    private final static String WEBAPP_FOLDER = "webapp";
    private final static String WEBAPP_NAME = "housemate";
    private final static String DEFAULT_WEBAPP_PATH = "/housemate";

    public final File config_dir;

	private final Map<String, String> properties;
    private final Log log;

    private final Injector injector;

	/**
	 * Create a environment instance
	 * @param args the command line args that the server was run with
	 * @throws HousemateException
	 */
	public ServerEnvironment(String args[]) throws HousemateException {

		// convert the command line args into a map of values that are set
		Map<String, String> overrides = parseArgs(args);

		String dir;
		// get the base housemate config directory. If overridden, use that, else use env var value. If that not set then quit
		if(overrides.get(HOUSEMATE_CONFIG_DIR) != null) {
			System.out.println("Overriding " + HOUSEMATE_CONFIG_DIR + " to " + overrides.get(HOUSEMATE_CONFIG_DIR));
			dir = overrides.get(HOUSEMATE_CONFIG_DIR);
			overrides.remove(HOUSEMATE_CONFIG_DIR);
		} else {
			dir = System.getenv(HOUSEMATE_CONFIG_DIR);
			if(dir == null)
				dir = System.getProperty("user.home") + File.separator + ".housemate";
		}
		config_dir = new File(dir);

        // create the directory if it does not exist
        if(!config_dir.exists())
            config_dir.mkdirs();

		// get the base housemate log directory. If overridden, use that, else use env var value. If that not set then quit
		if(overrides.get(HOUSEMATE_LOG_DIR) != null) {
			System.out.println("Overriding " + HOUSEMATE_LOG_DIR + " to " + overrides.get(HOUSEMATE_LOG_DIR));
			dir = overrides.get(HOUSEMATE_LOG_DIR);
			overrides.remove(HOUSEMATE_LOG_DIR);
		} else {
			dir = System.getenv(HOUSEMATE_LOG_DIR);
			if(dir == null)
				dir = System.getProperty("user.home") + File.separator + ".housemate" + File.separator + "log";
		}
		File top_log_dir = new File(dir);

        // create it if it does not exist
        if(!top_log_dir.exists())
            top_log_dir.mkdirs();

        // for the server we use the server subdir
        File log_dir = new File(top_log_dir, "server");

        // create it if it does not exist
        if(!log_dir.exists())
            log_dir.mkdirs();

		// init the properties
		properties = new HashMap<String, String>();

		// get the props file
		File props_file = new File(config_dir, HOUSEMATE_PROPS_FILE);
		if(!props_file.exists()) {
			System.out.println("Could not find server properties file \"" + props_file.getAbsolutePath() + "\". Creating a new one with default settings");
            createDefaultPropsFile(props_file);
		}

		// load the props from the file
		try {
            Properties fileProps = new Properties();
			fileProps.load(new FileInputStream(props_file));
            for(String key : fileProps.stringPropertyNames())
                properties.put(key, fileProps.getProperty(key));
		} catch (FileNotFoundException e) {
			// Would have logged above!
			System.err.println("Could not find server properties file \"" + props_file.getAbsolutePath() + "\"");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not read server properties from file");
			System.exit(0);
		}

		// override any properties from the props file that are specified on the command line
		for(String prop_name : overrides.keySet()) {
			if(properties.get(prop_name) != null)
				System.out.println("Overriding prop file setting of " + prop_name + " to " + overrides.get(prop_name));
			else
				System.out.println("Setting custom property " + prop_name + " to " + overrides.get(prop_name));
			properties.put(prop_name, overrides.get(prop_name));
		}

        try {
            FileWriter fileWriter = new FileWriter(LogLevel.valueOf(properties.get(LOG_LEVEL)),
                    log_dir.getAbsolutePath() + File.separator + "housemate.log");
            List<LogWriter> logWriters = Arrays.asList(fileWriter, new StdOutWriter(LogLevel.DEBUG));
            log = new Log("Housemate", logWriters);
        } catch(IOException e) {
            throw new HousemateException("Failed to create main app log", e);
        }

        properties.put(SjoerdDB.PATH_PROPERTY_KEY, config_dir.getAbsolutePath() + File.separator + "database");

        injector = Guice.createInjector(
                new PCModule(log, properties), // log and properties provider
                new SjoerdDBModule(), // storage impl
                new ServerModule());

        injector.getInstance(ServerRealResourcesImpl.class).setRoot(injector.getInstance(ServerRealRootObject.class));
        injector.getInstance(new Key<ServerProxyResourcesImpl<ServerProxyFactory.All>>() {}).setRoot(injector.getInstance(ServerProxyRootObject.class));
        injector.getInstance(ServerBridgeResources.class).setRoot(injector.getInstance(RootObjectBridge.class));

        injector.getInstance(MainRouter.class).start();
        loadPlugins();
        injector.getInstance(ServerObjectLoader.class).loadObjects();
        startWebapp();
    }

    public Injector getInjector() {
        return injector;
    }

    /**
	 * Parse the command line arguments into a map of properties that are set and their values
	 * @param args the command line arguments
	 * @return a map of properties that are set and their values
	 * @throws HousemateException
	 */
	private final Map<String, String> parseArgs(String args[]) throws HousemateException {
		if(args.length % 2 == 1)
			throw new HousemateException("Odd number of arguments to parse - must be even");

		Map<String, String> properties = new HashMap<String, String>(args.length / 2);

		for(int i = 0; i < args.length; i+=2) {
			if(!args[i].startsWith("-"))
				throw new HousemateException("Property name must start with \"-\"");
			properties.put(args[i].substring(1), args[i + 1]);
		}

		return properties;
	}

    private void createDefaultPropsFile(File file) throws HousemateException {
        try {
            BufferedWriter out = new BufferedWriter(new java.io.FileWriter(file));
            out.write(LOG_LEVEL + "=DEBUG\n");
            out.write(SERVER_NAME + "=My Server\n");
            out.write(SocketServer.PORT + "=46873\n");
            out.write(RUN_WEBAPP + "=true\n");
            out.write(USERNAME + "=admin\n");
            out.write(PASSWORD + "=admin\n");
            out.close();
        } catch(IOException e) {
            throw new HousemateException("Could not create default props file", e);
        }
    }

    private void loadPlugins() {

        loadSharedJNILibs();
        
        PluginManager pluginManager = injector.getInstance(PluginManager.class);

        // add the default plugin
        pluginManager.addPlugin(new MainPlugin(injector));

        // discover plugins from local dir
        File pluginDirectory = new File(this.config_dir, PLUGINS_DIR_NAME);
        if(!pluginDirectory.exists())
            pluginDirectory.mkdir();
        if(pluginDirectory.isFile())
            log.w("Plugin path is not a directory");
        else {
            log.d("Loading plugins from " + pluginDirectory.getAbsolutePath());
            for(File pluginFile : pluginDirectory.listFiles(new PluginFileFilter())) {
                for(PluginDescriptor plugin : loadPlugin(pluginFile))
                    pluginManager.addPlugin(plugin);
            }
        }
    }

    private void loadSharedJNILibs() {
        //CommPortIdentifier.getPortIdentifiers();
    }

    private List<PluginDescriptor> loadPlugin(File file) {
        log.d("Loading plugins from " + file.getAbsolutePath());
        try {
            ClassLoader cl = new URLClassLoader(new URL[] {file.toURI().toURL()}, PluginDescriptor.class.getClassLoader());
            Enumeration<URL> urls = cl.getResources("META-INF/MANIFEST.MF");
            List<PluginDescriptor> result = new ArrayList<PluginDescriptor>();
            while(urls.hasMoreElements()) {
                URL url = urls.nextElement();
                Manifest mf = new Manifest(url.openStream());
                for(Map.Entry<Object, Object> attrEntry : mf.getMainAttributes().entrySet()) {
                    try {
                        result.addAll(processManifestAttribute(url, attrEntry.getKey(), attrEntry.getValue(), cl));
                    } catch(HousemateException e) {
                        log.e("Failed to load plugin descriptor");
                        log.st(e);
                    }
                }
                for(Map.Entry<String, Attributes> mfEntry : mf.getEntries().entrySet()) {
                    Attributes attrs = mfEntry.getValue();
                    for(Map.Entry<Object, Object> attrEntry : attrs.entrySet()) {
                        try {
                            result.addAll(processManifestAttribute(url, attrEntry.getKey(), attrEntry.getValue(), cl));
                        } catch(HousemateException e) {
                            log.e("Failed to load plugin descriptor");
                            log.st(e);
                        }
                    }
                }
            }
            return result;
        } catch(MalformedURLException e) {
            log.e("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not get URL for file");
            return Lists.newArrayList();
        } catch(IOException e) {
            log.e("Failed to load  plugin from " + file.getAbsolutePath() + ". Could not load mainifest file");
            return Lists.newArrayList();
        }
    }

    private List<PluginDescriptor> processManifestAttribute(URL url, Object key, Object value, ClassLoader cl) throws HousemateException {
        List<PluginDescriptor> result = Lists.newArrayList();
        if(key != null && key.toString().equals(PluginDescriptor.MANIFEST_ATTRIBUTE)
                && value instanceof String) {
            log.d("Found " + PluginDescriptor.MANIFEST_ATTRIBUTE + " attribute in "+ url.toExternalForm());
            String[] classNames = ((String)value).split(",");
            for(String className : classNames) {
                PluginDescriptor descriptor = null;
                try {
                    log.d("Loading plugin class " + className);
                    Class<?> clazz = Class.forName(className, true, cl);
                    if(PluginDescriptor.class.isAssignableFrom(clazz)) {
                        descriptor = (PluginDescriptor) clazz.getConstructor().newInstance();
                        log.d("Successfully loaded plugin class " + className);
                    } else
                        throw new HousemateException(clazz.getName() + " is not assignable to " + PluginDescriptor.class.getName());
                } catch(ClassNotFoundException e) {
                    throw new HousemateException("Could not load plugin descriptor " + className + " from " + url.toExternalForm(), e);
                } catch(InvocationTargetException e) {
                    throw new HousemateException("Could not load plugin descriptor " + className + " from " + url.toExternalForm(), e);
                } catch(NoSuchMethodException e) {
                    throw new HousemateException("Could not load plugin descriptor " + className + " from " + url.toExternalForm(), e);
                } catch(InstantiationException e) {
                    throw new HousemateException("Could not load plugin descriptor " + className + " from " + url.toExternalForm(), e);
                } catch(IllegalAccessException e) {
                    throw new HousemateException("Could not load plugin descriptor " + className + " from " + url.toExternalForm(), e);
                }
                if(descriptor != null) {
                    try {
                        result.add(descriptor);
                    } catch(Throwable t) {
                        throw new HousemateException("Failed to initialise plugin descriptor " + className + " from " + url.toExternalForm(), t);
                    }
                }
            }
        }
        return result;
    }

    private class PluginFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.isFile() && pathname.getName().endsWith(".jar");
        }
    }

    private void startWebapp() throws HousemateException {
        if(properties.get(RUN_WEBAPP) != null
                && properties.get(RUN_WEBAPP).equalsIgnoreCase("false")) {
            log.d("Not starting webapp");
            return;
        }
        File webappDirectory = new File(config_dir, WEBAPP_FOLDER);
        if(!webappDirectory.exists())
            webappDirectory.mkdir();
        File webappFile = new File(webappDirectory, WEBAPP_NAME + ".war");
        if(webappFile.isDirectory())
            webappFile.delete();
        if(!webappFile.exists()) {
            URL url = getClass().getResource("/" + webappFile.getName());
            if(url == null) {
                log.e("Could not find existing webapp and could not find it in jar. Cannot start web interface");
                return;
            }
            copyWebapp(url, webappFile);
        }
        File webappDir = new File(webappDirectory, WEBAPP_NAME);
        if(webappDir.isFile())
            webappDir.delete();
        if(!webappDir.exists()) {
            webappDir.mkdir();
            unpackWar(webappFile, webappDir);
        }
        int port = 46874;
        try {
            if(properties.containsKey(WEBAPP_PORT))
                port = Integer.parseInt(properties.get(WEBAPP_PORT));
        } catch(Throwable t) {
            log.w("Failed to parse property " + WEBAPP_PORT + ". Using default of " + port + " instead");
        }
        startJetty(port, webappDir);
    }

    private void copyWebapp(URL fromUrl, File toFile) throws HousemateException {
        InputStream is = null;
        OutputStream os = null;
        try {
            try {
                is = fromUrl.openStream();
            } catch(IOException e) {
                throw new HousemateException("Failed to open stream to copy webapp from");
            }
            try {
                os = new FileOutputStream(toFile);
            } catch(IOException e) {
                throw new HousemateException("Failed to open stream to write webapp to");
            }
            byte[] buffer = new byte[1024];
            int read;
            try {
                while((read = is.read(buffer)) > 0)
                    os.write(buffer, 0, read);
            } catch (IOException e) {
                throw new HousemateException("Failed to copy webapp", e);
            }
        } finally {
            try {
                if(is != null)
                    is.close();
            } catch(IOException e) {
                log.e("Failed to close input stream when copying webapp");
                log.st(e);
            }
            try {
                if(os != null)
                    os.close();
            } catch(IOException e) {
                log.e("Failed to close input stream when copying webapp");
                log.st(e);
            }
        }
    }

    private void unpackWar(File webappFile, File webappDir) throws HousemateException {
        try {
            Resource jarWebApp = JarResource.newJarResource(FileResource.newResource(webappFile));
            jarWebApp.copyTo(webappDir);
        } catch(IOException e) {
            throw new HousemateException("Error unpacking webapp", e);
        }
    }

    private void startJetty(int port, File warFile) throws HousemateException {
        Server server = new Server(port);

        // Configure webapp provided as external WAR
        WebAppContext webapp = new WebAppContext();
        webapp.getServletContext().setAttribute("RESOURCES", injector.getInstance(ClientResources.class));
        webapp.setContextPath(properties.get(WEBAPP_PATH) != null ? properties.get(WEBAPP_PATH) : DEFAULT_WEBAPP_PATH);
        webapp.setWar(warFile.getAbsolutePath());
        server.setHandler(webapp);

        // Start the server
        try {
            server.start();
        } catch(Exception e) {
            throw new HousemateException("Failed to start internal webserver", e);
        }
    }

    public static class RM implements RegexMatcher {

        Pattern pattern;

        public RM(String regexPattern) {
            pattern = Pattern.compile(regexPattern);
        }

        @Override
        public boolean matches(String value) {
            return pattern.matcher(value).matches();
        }
    }
}