package org.caliog.Villagers.NPC;

import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.caliog.Villagers.NPC.Util.Recipe;
import org.caliog.Villagers.nms.NMS;

public class Trader extends Villager {

    private Recipe recipe = new Recipe();

    public Trader(org.bukkit.entity.Villager v, Location location, String name) {
	super(v, VillagerType.TRADER, location, name);
	this.setInteractionRadius(4F);
    }

    public boolean openInventory(Player player) {
	return NMS.getUtil().openInventory(this, player);
    }

    public void addRecipe(ItemStack s1, ItemStack s2, ItemStack s3) {
	this.recipe.add(s1, s2, s3);
    }

    public void addRecipe(ItemStack s, int i) {
	this.recipe.add(s, i);
    }

    public void setRecipe(Recipe recipe) {
	if (recipe == null)
	    return;
	this.recipe = recipe;

    }

    @Override
    public FileWriter save(FileWriter writer) throws IOException {
	super.save(writer);
	writer.append(recipe.asString() + "\r");
	return writer;
    }

    public void delRecipe(ItemStack itemInHand) {
	recipe.del(itemInHand);

    }

    public Recipe getRecipe() {
	return recipe;
    }

}
