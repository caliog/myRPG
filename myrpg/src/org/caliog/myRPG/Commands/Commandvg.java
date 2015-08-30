package org.caliog.myRPG.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.caliog.Villagers.NPC.Villager;
import org.caliog.Villagers.NPC.Util.VManager;
import org.caliog.myRPG.Commands.Utils.Command;
import org.caliog.myRPG.Commands.Utils.CommandExecutable;
import org.caliog.myRPG.Commands.Utils.CommandField;
import org.caliog.myRPG.Commands.Utils.CommandField.FieldProperty;
import org.caliog.myRPG.Commands.Utils.Commands;
import org.caliog.myRPG.Messages.CmdMessage;

public class Commandvg extends Commands {

    @Override
    public List<Command> getCommands() {
	/*
	 * Name: vg
	 * SubName: create
	 * 
	 * Permission: myrpg.villager.create
	 * 
	 * Usage: /vg create <name..>
	 */
	cmds.add(new Command("vg", "myrpg.villager.create", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		String name = args[1];
		VManager.spawnVillager(player.getLocation(), name.trim());
	    }
	}, new CommandField("create", FieldProperty.IDENTIFIER), new CommandField("name", FieldProperty.REQUIRED)));

	/*
	 * Name: vg
	 * SubName: remove
	 * 
	 * Permission: myrpg.villager.remove
	 * 
	 * Usage: /vg remove
	 */
	cmds.add(new Command("vg", "myrpg.villager.remove", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Villager v = VManager.getClosestVillager(player.getLocation());
		if (v == null) {
		    player.sendMessage(CmdMessage.noVillager);
		    return;
		}
		VManager.remove(v.getUniqueId());
		player.sendMessage(ChatColor.GRAY + "Removed this villager!");
	    }
	}, new CommandField("remove", FieldProperty.IDENTIFIER)));

	/*
	 * Name: vg
	 * SubName: talk
	 * 
	 * Permission: myrpg.villager.talk
	 * 
	 * Usage: /vg talk <id> <message> <type> <target>
	 */
	cmds.add(new Command("vg", "myrpg.villager.talk", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Villager v = VManager.getClosestVillager(player.getLocation());

		if (v == null) {
		    player.sendMessage(CmdMessage.noVillager);
		    return;
		}
		String text = args[2];
		try {
		    text += "#" + args[3];
		    text += "#" + (args.length >= 5 ? args[4] : Integer.parseInt(args[1]) + 1);

		    v.addText(Integer.parseInt(args[1]), text);
		    player.sendMessage(ChatColor.GRAY + "The villager talks!");
		} catch (NumberFormatException e) {
		    player.sendMessage(ChatColor.RED + "/vg talk <id> <message> <type> [target]");
		    player.sendMessage(ChatColor.RED + "Visit myRPG wiki to get some information about this command!");
		}
	    }
	}, new CommandField("talk", FieldProperty.IDENTIFIER), new CommandField("id", "not-negative integer",
		FieldProperty.REQUIRED), new CommandField("message", FieldProperty.REQUIRED), new CommandField("type",
		"END|QUESTION|TEXT", FieldProperty.REQUIRED), new CommandField("target", "not-negative integer",
		FieldProperty.OPTIONAL)));

	/*
	 * Name: vg
	 * SubName: deltalk
	 * 
	 * Permission: myrpg.villager.talk
	 * 
	 * Usage: /vg deltalk [id]
	 */
	cmds.add(new Command("vg", "myrpg.villager.talk", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Villager v = VManager.getClosestVillager(player.getLocation());
		if (v == null) {
		    player.sendMessage(CmdMessage.noVillager);
		    return;
		}
		if (args.length == 2)
		    v.removeText(Integer.parseInt(args[1]));
		else
		    v.clearText();
		player.sendMessage(ChatColor.GRAY + "Removed message!");
	    }
	}, new CommandField("deltalk", FieldProperty.IDENTIFIER), new CommandField("id", "not-negative integer",
		FieldProperty.OPTIONAL)));

	/*
	 * Name: vg
	 * SubName: quest
	 * 
	 * Permission: myrpg.villager.quest
	 * 
	 * Usage: /vg quest <name>
	 */
	cmds.add(new Command("vg", "myrpg.villager.quest", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Villager v = VManager.getClosestVillager(player.getLocation());
		if (v == null) {
		    player.sendMessage(CmdMessage.noVillager);
		    return;
		}
		if (v.addQuest(args[1]))
		    player.sendMessage(ChatColor.GRAY + "Added this quest!");
		else
		    player.sendMessage(ChatColor.GRAY + "Quest does not exist.");
	    }
	}, new CommandField("quest", FieldProperty.IDENTIFIER), new CommandField("name", FieldProperty.REQUIRED)));

	/*
	 * Name: vg
	 * SubName: delquest
	 * 
	 * Permission: myrpg.villager.quest
	 * 
	 * Usage: /vg delquest <name>
	 */
	cmds.add(new Command("vg", "myrpg.villager.quest", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Villager v = VManager.getClosestVillager(player.getLocation());
		if (v == null) {
		    player.sendMessage(CmdMessage.noVillager);
		    return;
		}
		v.removeQuest(args[1]);
		player.sendMessage(ChatColor.GRAY + "Removed this quest!");
	    }
	}, new CommandField("delquest", FieldProperty.IDENTIFIER), new CommandField("name", FieldProperty.REQUIRED)));

	/*
	 * Name: vg
	 * SubName: toggle
	 * 
	 * Permission: myrpg.villager.toggle
	 * 
	 * Usage: /vg toggle
	 */
	cmds.add(new Command("vg", "myrpg.villager.toggle", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		Villager v = VManager.getClosestVillager(player.getLocation());
		if (v == null) {
		    player.sendMessage(CmdMessage.noVillager);
		    return;
		}
		v.toggleProfession();
	    }
	}, new CommandField("toggle", FieldProperty.IDENTIFIER)));

	//TODO add paths

	return cmds;
    }
}
