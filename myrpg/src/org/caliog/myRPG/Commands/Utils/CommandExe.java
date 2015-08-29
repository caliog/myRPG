package org.caliog.myRPG.Commands.Utils;

import org.caliog.myRPG.Entities.PlayerManager;
import org.caliog.myRPG.Messages.Msg;
import org.caliog.myRPG.Utils.myUtils;

import org.bukkit.entity.Player;

public class CommandExe {

    private Command cmd;
    private String[] args;
    private Player player;

    public CommandExe(Command cmd, String[] args, Player player) {
	this.cmd = cmd;
	this.setArgs(args);
	this.player = player;
	exe();
    }

    public void exe() {
	if (!PlayerManager.getPlayer(player.getUniqueId()).hasPermission(cmd.getPermission())) {
	    Msg.noPermission(player);
	    return;
	}
	if (checkFields(cmd, args, player)) {
	    cmd.execute(args, player);
	}
    }

    public static boolean checkFields(Command cmd, String[] args, Player player) {
	for (int i = 0; i < args.length; i++) {
	    if (i >= cmd.getFields().length)
		break;
	    CommandField field = cmd.getFields()[i];

	    if (field.isIdentifier())
		continue;

	    if (field.getType().contains("integer")) {
		if (!myUtils.isInteger(args[i])) {

		    if (player != null)
			Msg.commandUsageError(field, player);
		    return false;
		}
		if (field.getType().contains("positive") && !myUtils.isPositiveInteger(args[i])) {
		    if (player != null)
			Msg.commandUsageError(field, player);
		    return false;
		}
		if (field.getType().contains("not-negative") && !myUtils.isNotNegativeInteger(args[i])) {
		    if (player != null)
			Msg.commandUsageError(field, player);
		    return false;
		}
	    }

	    if (field.getType().contains("|") && !field.getType().toLowerCase().contains(args[i].toLowerCase())) {
		if (player != null)
		    Msg.commandOptionError(field, player);
		return false;
	    }

	}

	return true;
    }

    public String[] getArgs() {
	return args;
    }

    public void setArgs(String[] args) {
	this.args = args;
    }
}
