package org.caliog.myRPG.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.caliog.Villagers.NPC.Guards.CheckpointPath;
import org.caliog.Villagers.NPC.Guards.GManager;
import org.caliog.Villagers.NPC.Guards.Guard;
import org.caliog.Villagers.NPC.Guards.PathUtil;
import org.caliog.myRPG.Commands.Utils.Command;
import org.caliog.myRPG.Commands.Utils.CommandExecutable;
import org.caliog.myRPG.Commands.Utils.CommandField;
import org.caliog.myRPG.Commands.Utils.CommandField.FieldProperty;
import org.caliog.myRPG.Commands.Utils.Commands;

public class Commandguard extends Commands {

    @Override
    public List<Command> getCommands() {
	/*
	 * Name: guard
	 * SubName: create
	 * 
	 * Permission: myrpg.guard.create
	 * 
	 * Usage: /guard create <name>
	 */
	cmds.add(new Command("guard", "myrpg.guard.create", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {

		GManager.createNewGuard(args[1], player.getLocation());
		player.sendMessage(ChatColor.GOLD + "Created guard at your position!");

	    }
	}, new CommandField("create", FieldProperty.IDENTIFIER), new CommandField("name", FieldProperty.REQUIRED)));

	/*
	 * Name: guard
	 * SubName: path
	 * 
	 * Permission: myrpg.guard.path
	 * 
	 * Usage: /guard path <path>
	 */
	cmds.add(new Command("guard", "myrpg.guard.path", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Guard g = GManager.getClosestGuard(player.getLocation());
		if (args[1].equalsIgnoreCase("remove")) {
		    g.removePath();
		    player.sendMessage(ChatColor.GOLD + "The guard won't walk this path anymore!");
		    return;
		}
		if (new CheckpointPath(args[1]).isLoaded()) {
		    g.setPath(PathUtil.getPath(args[1]));
		    player.sendMessage(ChatColor.GOLD + "Added path!");
		} else
		    player.sendMessage(ChatColor.RED + "This path doesn't exist!");
	    }
	}, new CommandField("path", FieldProperty.IDENTIFIER), new CommandField("pathname", FieldProperty.REQUIRED)));

	/*
	 * Name: guard
	 * SubName: attack
	 * 
	 * Permission: myrpg.guard.attack
	 * 
	 * Usage: /guard attack <attackOptions>
	 */
	cmds.add(new Command("guard", "myrpg.guard.attack", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Guard g = GManager.getClosestGuard(player.getLocation());
		String a = "";
		for (int i = 1; i < args.length; i++)
		    a += args[i];
		g.setAttackings(a);
		player.sendMessage(ChatColor.GOLD + "Changed attacking of this guard!");

	    }
	}, new CommandField("attack", FieldProperty.IDENTIFIER), new CommandField("attackOptions",
		"all|none|monsters|animals|players", FieldProperty.REQUIRED)));

	/*
	 * Name: guard
	 * SubName: equip
	 * 
	 * Permission: myrpg.guard.equip
	 * 
	 * Usage: /guard equip
	 */
	cmds.add(new Command("guard", "myrpg.guard.equip", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Guard g = GManager.getClosestGuard(player.getLocation());
		ItemStack hand = player.getItemInHand();
		ItemStack armor[] = player.getInventory().getArmorContents();
		g.setEquipment(hand, armor);
		player.sendMessage(ChatColor.GOLD + "Changed guard's equipment!");

	    }
	}, new CommandField("equip", FieldProperty.IDENTIFIER)));

	/*
	 * Name: guard
	 * SubName: remove
	 * 
	 * Permission: myrpg.guard.remove
	 * 
	 * Usage: /guard remove
	 */
	cmds.add(new Command("guard", "myrpg.guard.remove", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Guard g = GManager.getClosestGuard(player.getLocation());
		GManager.remove(g);
		player.sendMessage(ChatColor.GOLD + "Removed this guard!");
	    }
	}, new CommandField("remove", FieldProperty.IDENTIFIER)));

	return cmds;
    }
}
