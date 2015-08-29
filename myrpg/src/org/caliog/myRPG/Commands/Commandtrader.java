package org.caliog.myRPG.Commands;

import java.util.List;

import org.caliog.Villagers.NPC.Trader;
import org.caliog.Villagers.NPC.Util.VManager;
import org.caliog.myRPG.Commands.Utils.Command;
import org.caliog.myRPG.Commands.Utils.CommandExecutable;
import org.caliog.myRPG.Commands.Utils.CommandField;
import org.caliog.myRPG.Commands.Utils.CommandField.FieldProperty;
import org.caliog.myRPG.Commands.Utils.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
		    int price = Integer.parseInt(args[1]);
		    trader.addRecipe(player.getItemInHand(), price);
		    player.sendMessage(ChatColor.GRAY + "The trader sells now: "
			    + player.getItemInHand().getType().name().toLowerCase().replace("_", " ") + "!");
		} else {
		    trader.addRecipe(player.getInventory().getItem(0), player.getInventory().getItem(1), player
			    .getInventory().getItem(2));
		    player.sendMessage(ChatColor.GRAY + "The trader sells now: "
			    + player.getInventory().getItem(2).getType().name().toLowerCase().replace("_", " ") + "!");
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

		trader.delRecipe(player.getItemInHand());
		player.sendMessage(ChatColor.GRAY + "Deleted this recipe!");

	    }
	}, new CommandField("del", FieldProperty.IDENTIFIER)));

	return cmds;
    }
}
