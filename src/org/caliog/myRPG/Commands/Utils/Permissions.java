package org.caliog.myRPG.Commands.Utils;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class Permissions {
	private static Set<String> permissions;

	public static void declare() {
		permissions = new HashSet<String>();
		permissions.add("myrpg.item");
		permissions.add("myrpg.mic");
		permissions.add("myrpg.reload");
		permissions.add("myrpg.backup");

		permissions.add("myrpg.level");

		permissions.add("myrpg.group.create");
		permissions.add("myrpg.group.join");
		permissions.add("myrpg.group.invite");

		permissions.add("myrpg.guard.create");
		permissions.add("myrpg.guard.remove");
		permissions.add("myrpg.guard.attack");
		permissions.add("myrpg.guard.path");
		permissions.add("myrpg.guard.equip");

		permissions.add("myrpg.path.create");
		permissions.add("myrpg.path.set");
		permissions.add("myrpg.path.delete");

		permissions.add("myrpg.msz.create");
		permissions.add("myrpg.msz.remove");
		permissions.add("myrpg.msz.info");
		permissions.add("myrpg.msz.reset");

		permissions.add("myrpg.villager.create");
		permissions.add("myrpg.villager.remove");
		permissions.add("myrpg.villager.talk");
		permissions.add("myrpg.villager.quest");
		permissions.add("myrpg.villager.toggle");
		permissions.add("myrpg.villager.path");

		permissions.add("myrpg.trader.create");
		permissions.add("myrpg.trader.recipe");

		permissions.add("myrpg.priest.create");

		permissions.add("myrpg.pet.create");

	}

	public static Set<String> getPermissions(Player player) {
		Set<String> list = new HashSet<String>();

		if (player.isOp() || player.hasPermission("myrpg.admin") || player.hasPermission("myrpg.*") || player.hasPermission("*")
				|| player.hasPermission("'*'")) {
			return permissions;
		} else {
			for (String p : permissions) {
				String[] split = p.split("\\.");
				String splitted = "";
				for (int i = 0; i < split.length; i++) {
					if (i < split.length - 1)
						splitted += split[i] + ".";
					else
						splitted += split[i];
					if (player.hasPermission(splitted + "*") || player.hasPermission(splitted + "admin")
							|| player.hasPermission(splitted)) {
						list.add(p);
					}
				}
			}
		}

		return list;
	}
}
