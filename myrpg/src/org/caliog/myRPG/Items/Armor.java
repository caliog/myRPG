package org.caliog.myRPG.Items;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.caliog.myRPG.Utils.FilePath;
import org.caliog.myRPG.Utils.Utils;

public class Armor extends CustomItemInstance {
    private int level;

    public Armor(Material type, String name, int level, boolean tradeable, YamlConfiguration config) {
	super(type, name, tradeable, config);
	this.level = level;
	syncItemStack();
    }

    public int getDefense() {
	return this.config.getInt("defense");
    }

    public int getLevel() {
	return this.level;
    }

    public void syncItemStack() {
	ItemMeta meta = getItemMeta();
	meta.setDisplayName(ChatColor.DARK_GRAY + getName() + ChatColor.GRAY + " Lv. " + getLevel());
	List<String> lore = new ArrayList<String>();
	lore.add(ChatColor.ITALIC + "" + ChatColor.BLUE + "Def: " + getDefense());
	if (!getEffects().isEmpty()) {
	    lore.add(" ");
	}
	for (ItemEffect effect : getEffects()) {
	    lore.add(ChatColor.ITALIC + "" + ChatColor.GOLD + effect.getString());
	}
	if ((hasClass()) || (hasMinLevel()) || (!isTradeable())) {
	    lore.add(" ");
	}
	if (hasMinLevel()) {
	    lore.add(ChatColor.RED + "MinLv: " + getMinLevel());
	}
	if (hasClass()) {
	    lore.add(ChatColor.RED + "Class: " + this.getClazz());
	}
	if (!isTradeable()) {
	    lore.add(ChatColor.RED + "soulbound!");
	}
	meta.setLore(lore);
	setItemMeta(meta);
    }

    public static Armor getInstance(ItemStack item) {
	if ((item == null) || (!item.hasItemMeta())) {
	    return null;
	}
	String name = null;
	String level = null;
	boolean soulbound = false;

	String dn = item.getItemMeta().getDisplayName();
	if ((dn == null) || (!dn.contains("" + ChatColor.GRAY))) {
	    return null;
	}
	name = dn.substring(dn.indexOf("" + ChatColor.DARK_GRAY) + 2, dn.indexOf("" + ChatColor.GRAY));

	name = Utils.cleanString(name);

	level = dn.substring(dn.indexOf(" Lv. ") + 5);
	for (String l : item.getItemMeta().getLore()) {
	    if (l.contains("soulbound! ")) {
		soulbound = true;
		break;
	    }
	}
	if ((name == null) || (level == null)) {
	    return null;
	}
	Armor ci = getInstance(name, Integer.parseInt(level), !soulbound);
	return ci;
    }

    public static Armor getInstance(String name, int level, boolean tradeable) {
	File f = new File(FilePath.armor + name + ".yml");
	if (!f.exists()) {
	    return null;
	}
	YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

	Material mat = Material.matchMaterial(config.getString("material", "none"));
	if (mat == null) {
	    return null;
	}
	Armor instance = new Armor(mat, name, level, tradeable, config);

	return instance;
    }

    public static boolean isArmor(ItemStack item) {
	return getInstance(item) != null;
    }
}
