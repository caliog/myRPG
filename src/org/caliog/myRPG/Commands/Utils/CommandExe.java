package org.caliog.myRPG.Commands.Utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.Entities.PlayerManager;
import org.caliog.myRPG.Entities.myClass;
import org.caliog.myRPG.Messages.Msg;
import org.caliog.myRPG.Utils.myUtils;

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
		myClass player = PlayerManager.getPlayer(this.player.getUniqueId());
		if (player == null) {
			this.player.sendMessage(ChatColor.RED + "Uups, I don't know you! Maybe try to login again. See console for more information!");
			Manager.plugin.getLogger().warning("Could not identify " + this.player.getName()
					+ ". Check your config and class configuration. Find help at caliog.org.");
			return;
		}
		if (!player.hasPermission(cmd.getPermission())) {
			Msg.noPermission(this.player);
			return;
		}
		if (checkFields(cmd, args, this.player)) {
			cmd.execute(args, this.player);
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
