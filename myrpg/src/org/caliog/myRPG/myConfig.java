package org.caliog.myRPG;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.caliog.myRPG.Classes.ClazzLoader;

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

    public static String getDefaultClass() {
	String str = config.getString("default-class");
	if (ClazzLoader.isClass(str))
	    return str;
	else if (ClazzLoader.isClass("Warrior"))
	    return "Warrior";
	else {
	    Manager.plugin.getLogger().log(Level.WARNING,
		    "Could not find default class (config.yml) in your class.yml! Disabling myRPG...");
	    Manager.plugin.getServer().getPluginManager().disablePlugin(Manager.plugin);
	}
	return null;
    }

    public static List<String> getDisabledWorlds() {
	List<String> def = new ArrayList<String>();
	def.add("world_nether");
	def.add("world_the_end");
	if (!config.isSet("disable-worlds"))
	    return def;
	return config.getStringList("disable-worlds");
    }

    public static boolean isLevelLinear() {
	return config.getBoolean("linear-experience", false);
    }

    public static int getRemoveItemTime() {
	return config.getInt("remove-item-delay", 120);
    }

    public static boolean isFireworkEnabled() {
	return config.getBoolean("firework", true);
    }

    public static float getExpLoseRate() {
	int r = config.getInt("exp-loss-on-death", 0);
	return r / 100F;
    }

    public static boolean spellsEnabled() {
	return config.getBoolean("enable-spells", true);
    }

    public static boolean keepInventory() {
	return config.getBoolean("keep-inventory", true);
    }

    public static int getDefaultSpawnTime() {
	return config.getInt("mob-spawn-time", 15);
    }
}
