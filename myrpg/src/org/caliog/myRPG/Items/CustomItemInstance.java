package org.caliog.myRPG.Items;

import java.io.File;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.caliog.myRPG.Utils.FilePath;

public class CustomItemInstance extends CustomItem {
    protected final YamlConfiguration config;

    public CustomItemInstance(Material type, String name, boolean tradeable, YamlConfiguration config) {
	super(type, name, tradeable);
	this.config = config;
	syncItemStack();
    }

    public List<ItemEffect> getEffects() {
	List<String> list = this.config.getStringList("item-effects");
	this.effects.clear();
	for (String l : list) {
	    if (l.contains(":")) {
		this.effects.add(new ItemEffect(ItemEffect.ItemEffectType.valueOf(l.split(":")[0]), Integer.parseInt(l
			.split(":")[1])));
	    }
	}
	return this.effects;
    }

    public int getMinLevel() {
	return this.config.getInt("min-level");
    }

    public String getClazz() {
	try {
	    return this.config.getString("class-type");
	} catch (Exception e) {
	}
	return null;
    }

    public static CustomItemInstance getInstance(String name, int i, boolean tradeable) {
	File f = new File(FilePath.items + name + ".yml");
	if (!f.exists()) {
	    return null;
	}
	YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

	Material mat = Material.matchMaterial(config.getString("material", "none"));
	if (mat == null) {
	    return null;
	}
	CustomItemInstance instance = new CustomItemInstance(mat, name, tradeable, config);
	instance.setAmount(i);

	return instance;
    }
}
