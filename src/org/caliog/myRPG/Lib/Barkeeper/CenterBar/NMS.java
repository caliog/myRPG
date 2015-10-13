package org.caliog.myRPG.Lib.Barkeeper.CenterBar;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import org.caliog.myRPG.Manager;

public class NMS {
    public static NMSUtil getUtil() {
	String version = Manager.plugin.getVersion();
	try {
	    Class<?> raw = Class.forName("org.caliog.myRPG.Lib.Barkeeper.CenterBar." + version + ".NMSUtil");
	    Class<? extends NMSUtil> util = raw.asSubclass(NMSUtil.class);
	    Constructor<? extends NMSUtil> constructor = util.getConstructor(new Class[0]);
	    return (NMSUtil) constructor.newInstance(new Object[0]);
	} catch (ClassNotFoundException ex) {
	    Manager.plugin.getLogger().log(Level.WARNING, "Unsupported bukkit version! (" + version + ")");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }
}
