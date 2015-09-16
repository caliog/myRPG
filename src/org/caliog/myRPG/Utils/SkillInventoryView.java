package org.caliog.myRPG.Utils;

import org.caliog.myRPG.Entities.PlayerManager;
import org.caliog.myRPG.Items.Books.DexBook;
import org.caliog.myRPG.Items.Books.IntBook;
import org.caliog.myRPG.Items.Books.StrBook;
import org.caliog.myRPG.Items.Books.VitBook;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class SkillInventoryView extends InventoryView {
    private HumanEntity entity;
    private Inventory inventory;

    public SkillInventoryView(Player player, Inventory inv) {
	this.entity = player;
	this.inventory = inv;
    }

    public Inventory getBottomInventory() {
	return this.inventory;
    }

    public HumanEntity getPlayer() {
	return this.entity;
    }

    public Inventory getTopInventory() {
	Inventory inv = Bukkit.createInventory(null, 9, "Deine Skills");
	inv.addItem(new ItemStack[] { new StrBook(PlayerManager.getPlayer(this.entity.getUniqueId())) });
	inv.addItem(new ItemStack[] { new DexBook(PlayerManager.getPlayer(this.entity.getUniqueId())) });
	inv.addItem(new ItemStack[] { new IntBook(PlayerManager.getPlayer(this.entity.getUniqueId())) });
	inv.addItem(new ItemStack[] { new VitBook(PlayerManager.getPlayer(this.entity.getUniqueId())) });
	return inv;
    }

    public InventoryType getType() {
	return this.inventory.getType();
    }
}