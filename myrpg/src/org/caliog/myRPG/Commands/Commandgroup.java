package org.caliog.myRPG.Commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.Commands.Utils.Command;
import org.caliog.myRPG.Commands.Utils.CommandExecutable;
import org.caliog.myRPG.Commands.Utils.CommandField;
import org.caliog.myRPG.Commands.Utils.CommandField.FieldProperty;
import org.caliog.myRPG.Commands.Utils.Commands;
import org.caliog.myRPG.Group.GroupManager;
import org.caliog.myRPG.Messages.Msg;
import org.caliog.myRPG.Utils.Utils;

public class Commandgroup extends Commands {

    @Override
    public List<Command> getCommands() {

	/*
	 * Name: group
	 * SubName: create
	 * 
	 * Permission: myrpg.group.create
	 * 
	 * Usage: /group create
	 */
	cmds.add(new Command("group", "myrpg.group.create", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		if (!GroupManager.isInGroup(player)) {
		    GroupManager.createGroup(player);
		    Msg.sendMessage(player, "group-created");
		} else {
		    Msg.sendMessage(player, "group-leave-create");
		}

	    }
	}, new CommandField("create", FieldProperty.IDENTIFIER)));

	/*
	 * Name: group
	 * SubName: invite
	 * 
	 * Permission: myrpg.group.invite
	 * 
	 * Usage: /group invite <player>
	 */
	cmds.add(new Command("group", "myrpg.group.invite", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		final String name = args[1];
		if (Bukkit.getPlayer(name) != null) {
		    GroupManager.invitation.put(Bukkit.getPlayer(name).getUniqueId(), player.getUniqueId());
		    Manager.scheduleTask(new Runnable() {
			public void run() {
			    GroupManager.invitation.remove(Bukkit.getPlayer(name).getUniqueId());
			}
		    }, 1800L);
		    Msg.sendMessage(Bukkit.getPlayer(name), "group-invited", Msg.PLAYER, player.getName());
		} else {
		    player.sendMessage(ChatColor.RED + "This player is offline!");
		}
	    }
	}, new CommandField("invite", FieldProperty.IDENTIFIER), new CommandField("player", FieldProperty.REQUIRED)));

	/*
	 * Name: group
	 * SubName: join
	 * 
	 * Permission: myrpg.group.join
	 * 
	 * Usage: /group join
	 */
	cmds.add(new Command("group", "myrpg.group.join", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		if (!GroupManager.isInGroup(player)) {
		    if (GroupManager.invitation.containsKey(player.getUniqueId())) {
			if (!GroupManager.addMemeber(
				Utils.getPlayer((UUID) GroupManager.invitation.get(player.getUniqueId())), player)) {
			    Msg.sendMessage(player, "group-full");
			}
			GroupManager.invitation.remove(player.getUniqueId());
		    }
		} else {
		    Msg.sendMessage(player, "group-leave-join");
		}

	    }
	}, new CommandField("join", FieldProperty.IDENTIFIER)));

	/*
	 * Name: group
	 * SubName: leave
	 * 
	 * Permission: null
	 * 
	 * Usage: /group leave [player]
	 */
	cmds.add(new Command("group", null, new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		if (args.length >= 2) {
		    String name = args[1];
		    if (Bukkit.getPlayer(name) != null) {
			if (!GroupManager.removeMemeber(player, name)) {
			    Msg.sendMessage(player, "group-cannot-leave-player");
			}
		    } else {
			player.sendMessage(ChatColor.RED + "This player is offline!");
		    }
		} else {
		    if (GroupManager.isInGroup(player)) {
			GroupManager.leaveGroup(player);
			Msg.sendMessage(player, "group-left");
		    } else {
			Msg.sendMessage(player, "group-no");
		    }
		}
	    }
	}, new CommandField("leave", FieldProperty.IDENTIFIER), new CommandField("player", FieldProperty.OPTIONAL)));

	return cmds;
    }
}
