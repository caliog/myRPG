package org.caliog.myRPG;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class myConfig {

    public static YamlConfiguration config;

    public static Material getCurrency() {
	try {
	    return Material.valueOf(config.getString("currency"));
	} catch (Exception e) {
	    Manager.plugin.getLogger().warning("Could not load currency in config.yml!");
	    Manager.plugin.getLogger().warning("Using default currency: Emeralds!");
	    return Material.EMERALD;
	}
    }

    public static String getStandardClass() {
	return config.getString("standard-class");
    }

    public static String getWorld() {
	return config.getString("world");
    }

    public static boolean isLevelLinear() {
	return config.getBoolean("linear-experience");
    }

    public static int getRemoveItemTime() {
	return config.getInt("remove-item-on-ground");
    }
}
