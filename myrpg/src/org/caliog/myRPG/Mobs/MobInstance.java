package org.caliog.myRPG.Mobs;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.caliog.myRPG.Entities.Playerface;
import org.caliog.myRPG.Items.ItemUtils;
import org.caliog.myRPG.Utils.FilePath;
import org.caliog.myRPG.Utils.Vector;

public class MobInstance extends Mob {
    public YamlConfiguration mobConfig = null;

    public MobInstance(String name, UUID id, Vector m) {
	super(name, id, m);
	File f = new File(FilePath.mobs + name + ".yml");
	if (f.exists()) {
	    this.mobConfig = YamlConfiguration.loadConfiguration(f);
	    setHealth(getHP());
	}
    }

    public int getLevel() {
	return this.mobConfig.getInt("level");
    }

    public EntityType getType() {
	return EntityType.valueOf(this.mobConfig.getString("entity-type"));
    }

    public HashMap<String, ItemStack> eq() {
	this.eq.put("HAND", ItemUtils.getItem(this.mobConfig.getString("equipment.hand")));
	this.eq.put("HELMET", ItemUtils.getItem(this.mobConfig.getString("equipment.helmet")));
	this.eq.put("CHESTPLATE", ItemUtils.getItem(this.mobConfig.getString("equipment.chestplate")));
	this.eq.put("LEGGINGS", ItemUtils.getItem(this.mobConfig.getString("equipment.leggings")));
	this.eq.put("BOOTS", ItemUtils.getItem(this.mobConfig.getString("equipment.boots")));
	return this.eq;
    }

    public double getHP() {
	return this.mobConfig.getInt("hitpoints");
    }

    public boolean isAgressive() {
	return this.mobConfig.getBoolean("agressive");
    }

    public int getExp() {
	String s = this.mobConfig.getString("experience");
	int e = 0;
	if (s.contains("%")) {
	    e = (int) (Playerface.getExpDifference(Integer.parseInt(s.split("%")[1].split("-")[0]),
		    Integer.parseInt(s.split("%")[1].split("-")[1])) * (Integer.parseInt(s.split("%")[0]) / 100.0F));
	} else {
	    e = Integer.parseInt(s);
	}
	return e;
    }

    public HashMap<ItemStack, Float> drops() {
	List<String> list = this.mobConfig.getStringList("drops");
	for (String l : list) {
	    if (l.contains("%")) {
		this.drops.put(ItemUtils.getItem(l.split("%")[1]),
			Float.valueOf(Integer.parseInt(l.split("%")[0]) / 100.0F));
	    }
	}
	return this.drops;
    }

    public int getExtraTime() {
	return this.mobConfig.getInt("extra-spawn-time");
    }

    public int getDefense() {
	return this.mobConfig.getInt("defense");
    }

    public int getDamage(boolean b) {
	return this.mobConfig.getInt("damage");
    }

    public boolean isBoss() {
	return this.mobConfig.getBoolean("boss");
    }
}
