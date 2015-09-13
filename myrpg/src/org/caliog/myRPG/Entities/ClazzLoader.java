package org.caliog.myRPG.Entities;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.caliog.myRPG.Spells.Spell;
import org.caliog.myRPG.Spells.SpellLoader;

public class ClazzLoader {

    public static YamlConfiguration classes;

    public static myClass create(Player player, String c) {
	if (isClass(c)) {
	    myClass clazz = new myClass(player, c);

	    ConfigurationSection config = classes.getConfigurationSection(c);
	    if (!clazz.isLoaded()) {
		clazz.setLevel(1);
		clazz.getPlayer().setExp(0F);
		clazz.setIntelligence(config.getInt("int"));
		clazz.setVitality(config.getInt("vit"));
		clazz.setDexterity(config.getInt("dex"));
		clazz.setStrength(config.getInt("str"));
	    }

	    String[] ids = { "xxx", "xxo", "xox", "oxx", "xoo", "oxo", "oox", "ooo" };
	    for (String id : ids) {
		if (config.isSet("spells." + id)) {
		    Spell spell = SpellLoader.load(clazz, config.getString("spells." + id));
		    if (spell != null)
			clazz.addSpell(id.replaceAll("x", "1").replaceAll("o", "0"), spell);

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
