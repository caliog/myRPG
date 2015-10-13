package org.caliog.myRPG.Lib.Barkeeper.TopBar;

import java.lang.reflect.Method;

import org.bukkit.entity.Player;

public abstract class NMSUtil {

    public abstract void sendSpawnPacket(Player player, FakeEntity entity);

    public abstract void sendDestroyPacket(Player player, FakeEntity entity);

    public abstract void sendMetaData(Player player, FakeEntity entity);

    public abstract void sendTeleport(Player player, FakeEntity entity);

    public static Method getMethod(Class<?> cl, String method, Class<?>[] args) {
	for (Method m : cl.getMethods()) {
	    if ((m.getName().equals(method)) && (matching(args, m.getParameterTypes()))) {
		return m;
	    }
	}
	return null;
    }

    private static boolean matching(Class<?>[] args, Class<?>[] parameterTypes) {
	boolean equal = true;
	if (args.length != parameterTypes.length) {
	    return false;
	}
	for (int i = 0; i < args.length; i++) {
	    if (args[i] != parameterTypes[i]) {
		equal = false;
		break;
	    }
	}
	return equal;
    }

}
