package org.caliog.myRPG.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.Commands.Utils.Command;
import org.caliog.myRPG.Commands.Utils.CommandExecutable;
import org.caliog.myRPG.Commands.Utils.CommandField;
import org.caliog.myRPG.Commands.Utils.CommandField.FieldProperty;
import org.caliog.myRPG.Commands.Utils.Commands;
import org.caliog.myRPG.Entities.VolatileEntities;
import org.caliog.myRPG.Messages.CmdMessage;
import org.caliog.myRPG.Mobs.MobSpawnZone;
import org.caliog.myRPG.Mobs.MobSpawner;
import org.caliog.myRPG.Utils.EntityUtils;

public class Commandmsz extends Commands {

    @Override
    public List<Command> getCommands() {
	/*
	 * Name: msz
	 * SubName: create
	 * 
	 * Permission: myrpg.msz.create
	 * 
	 * Usage: /msz create <mob> <radius> <amount>
	 */
	cmds.add(new Command("msz", "myrpg.msz.create", new CommandExecutable() {

	    @Override
	    public void execute(final String[] args, final Player player) {
		final String c = args[1];
		if (!EntityUtils.isMobClass(c)) {
		    player.sendMessage(ChatColor.GRAY + args[1] + " is not a mob!");
		    return;
		}
		Manager.scheduleTask(new Runnable() {
		    public void run() {
			MobSpawner.zones.add(new MobSpawnZone(player.getLocation(), Integer.parseInt(args[2]), Integer
				.parseInt(args[3]), c));
		    }
		});
		player.sendMessage(CmdMessage.createdMSZ);
	    }
	}, new CommandField("create", FieldProperty.IDENTIFIER), new CommandField("mob", FieldProperty.REQUIRED),
		new CommandField("amount", "positive integer", FieldProperty.REQUIRED)));

	/*
	 * Name: msz
	 * SubName: remove
	 * 
	 * Permission: myrpg.msz.remove
	 * 
	 * Usage: /msz remove
	 */
	cmds.add(new Command("msz", "myrpg.msz.remove", new CommandExecutable() {

	    @Override
	    public void execute(final String[] args, final Player player) {
		for (final MobSpawnZone z : MobSpawner.zones) {
		    if (z.isInside(player.getLocation())) {
			Manager.scheduleTask(new Runnable() {
			    public void run() {
				MobSpawner.zones.remove(z);
			    }
			});
			player.sendMessage(CmdMessage.removedMSZ);
			return;
		    }
		}
		player.sendMessage(CmdMessage.hereIsNoMSZ);
		return;
	    }
	}, new CommandField("remove", FieldProperty.IDENTIFIER)));

	/*
	 * Name: msz
	 * SubName: reset
	 * 
	 * Permission: myrpg.msz.reset
	 * 
	 * Usage: /msz reset
	 */
	cmds.add(new Command("msz", "myrpg.msz.reset", new CommandExecutable() {

	    @Override
	    public void execute(final String[] args, final Player player) {
		VolatileEntities.killAllMobs();
		player.sendMessage(ChatColor.GRAY + "Reset all mob spawn zones!");
	    }
	}, new CommandField("reset", FieldProperty.IDENTIFIER)));

	/*
	 * Name: msz
	 * SubName: info
	 * 
	 * Permission: myrpg.msz.info
	 * 
	 * Usage: /msz info
	 */
	cmds.add(new Command("msz", "myrpg.msz.info", new CommandExecutable() {

	    @Override
	    public void execute(final String[] args, final Player player) {
		for (MobSpawnZone z : MobSpawner.zones) {
		    if (z.isInside(player.getLocation())) {
			player.sendMessage(ChatColor.GRAY + "" + z.getAmount() + " of " + z.getMob()
				+ " in a radius of " + z.getRadius() + "! ");
			return;
		    }
		}
		player.sendMessage(CmdMessage.hereIsNoMSZ);
		return;
	    }
	}, new CommandField("info", FieldProperty.IDENTIFIER)));

	return cmds;
    }
}
