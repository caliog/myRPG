package org.caliog.myRPG.Items.Custom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.caliog.myRPG.Manager;

public class HealthPotion {

    public static ItemStack getHP1(int amount) {
	return getItemStack(amount, "Health Potion I", 1);
    }

    public static ItemStack getHP2(int amount) {
	return getItemStack(amount, "Health Potion II", 2);
    }

    public static ItemStack getHP3(int amount) {
	return getItemStack(amount, "Health Potion III", 4);
    }

    public static ItemStack getItemStack(int amount, String name, int heart) {
	Potion potion = new Potion(PotionType.INSTANT_HEAL);
	ItemStack stack = potion.toItemStack(amount);
	ItemMeta meta = stack.getItemMeta();
	if (stack.hasItemMeta()) {
	    meta = stack.getItemMeta();
	} else
	    meta = Bukkit.getItemFactory().getItemMeta(Material.POTION);
	if (Manager.plugin.getVersion().equals("v1_8_R3"))
	    meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
	meta.setDisplayName(ChatColor.DARK_GRAY + name);
	List<String> lore = new ArrayList<String>();
	lore.add(" ");
	lore.add(ChatColor.GOLD + "This potion gives you " + heart + " heart!");
	lore.add(" ");
	meta.setLore(lore);
	stack.setItemMeta(meta);
	return stack;
    }
}
