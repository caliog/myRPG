package org.caliog.myRPG.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.caliog.Villagers.NPC.Trader;
import org.caliog.Villagers.NPC.Util.VManager;
import org.caliog.myRPG.Commands.Utils.Command;
import org.caliog.myRPG.Commands.Utils.CommandExecutable;
import org.caliog.myRPG.Commands.Utils.CommandField;
import org.caliog.myRPG.Commands.Utils.CommandField.FieldProperty;
import org.caliog.myRPG.Commands.Utils.Commands;

public class Commandtrader extends Commands {

    @Override
    public List<Command> getCommands() {
	/*
	 * Name: trader
	 * SubName: create
	 * 
	 * Permission: myrpg.trader.create
	 * 
	 * Usage: /trader create <name..>
	 */
	cmds.add(new Command("trader", "myrpg.trader.create", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		VManager.spawnTrader(player.getLocation(), args[1], null);
		player.sendMessage(ChatColor.GOLD + "Spawned the trader next to you!");
	    }
	}, new CommandField("create", FieldProperty.IDENTIFIER), new CommandField("name", FieldProperty.REQUIRED)));

	/*
	 * Name: trader
	 * SubName: add
	 * 
	 * Permission: myrpg.trader.recipe
	 * 
	 * Usage: /trader add [price]
	 */
	cmds.add(new Command("trader", "myrpg.trader.recipe", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Trader trader = VManager.getClosestTrader(player.getLocation());
		if (trader == null) {
		    player.sendMessage(ChatColor.RED + "There is no trader around you!");
		    return;
		}
		if (args.length == 2) {
		    if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) {
			player.sendMessage(ChatColor.RED + "You have to take the item you want to sell in your hand!");
			return;
		    }
		    int price = Integer.parseInt(args[1]);
		    trader.addRecipe(player.getItemInHand(), price);
		    player.sendMessage(ChatColor.GOLD + "The trader sells now: "
			    + player.getItemInHand().getType().name().toLowerCase().replace("_", " ") + "!");
		} else {
		    ItemStack[] items = { player.getInventory().getItem(0), player.getInventory().getItem(1),
			    player.getInventory().getItem(2) };
		    if (items[2] == null) {
			player.sendMessage(ChatColor.RED
				+ "You have to put the item you want to sell in the third slot!");
			return;
		    } else if (items[0] == null) {
			player.sendMessage("You have to put something you want to have for your sell in the first slot!");
			return;
		    }
		    trader.addRecipe(items[0], items[1], items[2]);
		    player.sendMessage(ChatColor.GOLD + "The trader sells now: "
			    + items[2].getType().name().toLowerCase().replace("_", " ") + "!");
		}

	    }
	}, new CommandField("add", FieldProperty.IDENTIFIER), new CommandField("price", "not-negative integer",
		FieldProperty.OPTIONAL)));

	/*
	 * Name: trader
	 * SubName: del
	 * 
	 * Permission: myrpg.trader.recipe
	 * 
	 * Usage: /trader del
	 */
	cmds.add(new Command("trader", "myrpg.trader.recipe", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Trader trader = VManager.getClosestTrader(player.getLocation());
		if (trader == null) {
		    player.sendMessage(ChatColor.RED + "There is no trader around you!");
		    return;
		}
		if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) {
		    player.sendMessage(ChatColor.RED + "You have to take the item the trader sells in your hand!");
		    return;
		}
		if (trader.delRecipe(player.getItemInHand()))
		    player.sendMessage(ChatColor.GOLD + "Deleted this recipe!");
		else
		    player.sendMessage(ChatColor.GOLD
			    + "Sorry, I could not find this recipe. Take the item you do not want to sell anymore in your hand (amount is important)!");

	    }
	}, new CommandField("del", FieldProperty.IDENTIFIER)));

	return cmds;
    }
}
