package org.caliog.myRPG.Utils;

import java.util.HashMap;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.myConfig;

public class GroupManager {

    private static Permission permission;

    public static boolean init() {
	RegisteredServiceProvider<Permission> permissionProvider = Manager.plugin.getServer().getServicesManager()
		.getRegistration(net.milkbowl.vault.permission.Permission.class);
	if (permissionProvider != null) {
	    permission = permissionProvider.getProvider();
	}
	return (permission != null);
    }

    public static void updateGroup(Player player, int level) {
	if (permission == null || player == null)
	    return;
	String[] ignore = myConfig.getIgnoredPlayers();
	if (ignore != null)
	    for (String n : ignore)
		if (n.equals(player.getName()))
		    return;

	HashMap<String, Integer> map = myConfig.getGroupMap();
	int max = -1;
	for (int i : map.values())
	    if (i > max && level >= i)
		max = i;
	if (max == -1)
	    return;

	for (String g : permission.getGroups()) {
	    if (map.keySet().contains(g)) {
		int lvl = map.get(g);

		if (lvl <= level)
		    permission.playerAddGroup(player, g);
		else
		    permission.playerRemoveGroup(player, g);

	    }
	}

    }
}
