package org.caliog.myRPG.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.caliog.myRPG.Commands.Utils.Command;
import org.caliog.myRPG.Commands.Utils.CommandExecutable;
import org.caliog.myRPG.Commands.Utils.CommandField;
import org.caliog.myRPG.Commands.Utils.CommandField.FieldProperty;
import org.caliog.myRPG.Commands.Utils.Commands;
import org.caliog.myRPG.Mobs.MobInstance;
import org.caliog.myRPG.Mobs.Pet;
import org.caliog.myRPG.Utils.EntityUtils;

public class Commandpet extends Commands {

	@Override
	public List<Command> getCommands() {
		/*
		 * Name: pet
		 * 
		 * SubName: create
		 * 
		 * Permission: myrpg.pet.create
		 * 
		 * Usage: /pet create <mob> <name>
		 */
		cmds.add(new Command("pet", "myrpg.pet.create", new CommandExecutable() {

			@Override
			public void execute(String[] args, Player player) {
				if (EntityUtils.isMobClass(args[1])) {
					MobInstance mob = new MobInstance(args[1], null, null);
					if (mob.isPet()) {
						if (args[2].length() >= 17)
							args[2] = args[2].substring(0, 17);
						String name = args[2];
						Pet.givePetEgg(player, args[1], name);
					} else
						player.sendMessage(ChatColor.GOLD + args[1] + "is not a pet!");
				} else
					player.sendMessage(ChatColor.GOLD + args[1] + " is not a mob!");
			}
		}, new CommandField("create", FieldProperty.IDENTIFIER), new CommandField("mob", FieldProperty.REQUIRED),
				new CommandField("name", FieldProperty.REQUIRED)));

		return cmds;
	}

}
