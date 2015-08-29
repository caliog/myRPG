package org.caliog.myRPG.Items.Custom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.caliog.myRPG.Items.CustomItem;
import org.caliog.myRPG.Items.ItemEffect;

public class Apple_2 extends CustomItem {
    public Apple_2(int amount) {
	super(Material.APPLE, "Apple II", true);
	setAmount(amount);
	syncItemStack();
    }

    public void syncItemStack() {
	ItemMeta meta = getItemMeta();
	meta.setDisplayName(ChatColor.DARK_GRAY + getName());
	List<String> lore = new ArrayList<String>();

	lore.add(" ");
	lore.add(ChatColor.GOLD + "This apple gives you 10 lifepoints!");
	lore.add(" ");
	if (!isTradeable()) {
	    lore.add(ChatColor.RED + "soulbound!");
	}
	meta.setLore(lore);
	setItemMeta(meta);
    }

    public List<ItemEffect> getEffects() {
	return this.effects;
    }

    public int getMinLevel() {
	return 0;
    }

    @Override
    public String getClazz() {
	return null;
    }

}
