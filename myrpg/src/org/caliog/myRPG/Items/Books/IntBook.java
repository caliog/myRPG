package org.caliog.myRPG.Items.Books;

import java.util.ArrayList;
import java.util.List;

import org.caliog.myRPG.Entities.myClass;
import org.caliog.myRPG.Items.ItemEffect;

import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

public class IntBook extends Book {
    public IntBook(myClass clazz) {
	super("Intelligence", clazz);
    }

    public void syncItemStack() {
	ItemMeta meta = getItemMeta();
	meta.setDisplayName(ChatColor.DARK_GRAY + getName());
	List<String> lore = new ArrayList<String>();
	lore.add(ChatColor.BLUE + " + " + this.player.getIntelligence());

	lore.add(" ");
	lore.add(ChatColor.GOLD + "Drag a skillstar at this book");
	lore.add(ChatColor.GOLD + "to increase your intelligence!");
	lore.add(" ");

	lore.add(ChatColor.RED + "soulbound!");
	meta.setLore(lore);
	setItemMeta(meta);
    }

    public List<ItemEffect> getEffects() {
	return this.effects;
    }

    public int getMinLevel() {
	return -1;
    }

    public String getClazz() {
	return null;
    }
}
