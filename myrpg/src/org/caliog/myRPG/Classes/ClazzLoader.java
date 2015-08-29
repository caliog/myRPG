package org.caliog.myRPG.Classes;

import org.caliog.myRPG.Entities.myClass;
import org.caliog.myRPG.Spells.SpellLoader;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ClazzLoader {

    public static YamlConfiguration classes;

    public static myClass create(Player player, String c) {
	if (isClass(c)) {
	    myClass clazz = new myClass(player, c);
	    if (!clazz.isLoaded()) {
		ConfigurationSection config = classes.getConfigurationSection(c);
		clazz.setIntelligence(config.getInt("int"));
		clazz.setVitality(config.getInt("vit"));
		clazz.setDexterity(config.getInt("dex"));
		clazz.setStrength(config.getInt("str"));
		for (int i1 = 0; i1 <= 1; i1++) {
		    for (int i2 = 0; i2 <= 1; i2++) {
			for (int i3 = 0; i3 <= 1; i3++) {
			    String id = String.valueOf(i1) + String.valueOf(i2) + String.valueOf(i3);
			    clazz.addSpell(id, SpellLoader.load(clazz, config.getString("spells." + id)));
			}
		    }
		}
	    }
	    return clazz;
	}
	return null;
    }

    public static boolean isClass(String name) {
	return classes.isConfigurationSection(name);
    }
}
