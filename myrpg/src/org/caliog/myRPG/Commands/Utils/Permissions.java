package org.caliog.myRPG.Commands.Utils;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class Permissions {
    private static Set<String> permissions = new HashSet<String>();

    public static void declare() {
	permissions.add("myrpg.item");
	permissions.add("myrpg.mic");
	permissions.add("myrpg.reload");

	permissions.add("myrpg.level.set");
	permissions.add("myrpg.level.reset");

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

	permissions.add("myrpg.castle.create");
	permissions.add("myrpg.castle.remove");
	permissions.add("myrpg.castle.edit");
    }

    public static Set<String> getPermissions(Player player) {

	Set<String> list = new HashSet<String>();

	if (player.isOp() || player.hasPermission("myrpg.admin") || player.hasPermission("myrpg.*")
		|| player.hasPermission("*") || player.hasPermission("'*'")) {
	    return permissions;
	} else {

	    for (String p : permissions) {
		String[] split = p.split(".");
		String splitted = "";
		for (int i = 0; i < split.length; i++) {
		    splitted += split[i] + ".";
		    if (player.hasPermission(splitted + "*") || player.hasPermission(splitted + "admin")) {
			for (String perm : permissions) {
			    if (perm.startsWith(splitted))
				list.add(perm);
			}
		    }
		}
	    }

	}

	for (String perm : permissions)
	    if (player.hasPermission(perm))
		list.add(perm);

	return list;
    }
}
