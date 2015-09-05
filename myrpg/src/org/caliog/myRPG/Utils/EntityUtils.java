package org.caliog.myRPG.Utils;

import org.bukkit.ChatColor;
import org.caliog.myRPG.Mobs.MobInstance;

public class EntityUtils {
    public static String getBar(double h, double mh) {
	double p = h / mh;
	mh = 16.0D;
	String bar = "";
	for (double i = 1.0D; i <= mh; i += 1.0D) {
	    if (i / mh <= p) {
		bar = bar + ChatColor.RED + "♥";
	    } else {
		bar = bar + ChatColor.GOLD + "♥";
	    }
	}
	return bar;
    }

    public static boolean isMobClass(String name) {
	if (new MobInstance(name, null, null).mobConfig != null) {
	    return true;
	}
	return false;
    }
}
