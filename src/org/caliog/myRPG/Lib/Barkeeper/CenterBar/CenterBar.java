package org.caliog.myRPG.Lib.Barkeeper.CenterBar;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CenterBar {
    public static void display(Player player, String title, String subtitle, int time, boolean t) {
	NMSUtil util = NMS.getUtil();
	if (title != null) {
	    title = ChatColor.translateAlternateColorCodes('&', title);
	}
	if (subtitle != null) {
	    subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
	}
	if (util != null) {
	    util.sendBar(player, title, subtitle, t ? 15 : 0, time, t ? 15 : 0);
	}
    }

    public static void display(Player player, String title, int time) {
	display(player, title, null, time, false);
    }

    public static void display(Player player, String title) {
	display(player, title, 60);
    }

    public static void display(Player player, String title, String subtitle, int time) {
	display(player, title, subtitle, time, false);
    }

    public static void display(Player player, String title, String subtitle) {
	display(player, title, subtitle, 60, false);
    }

    public static void broadcast(String title, String subtitle, World world, int time, boolean t) {
	Collection<? extends Player> players = new ArrayList<Player>();
	players = Bukkit.getOnlinePlayers();
	if (world != null)
	    players = world.getPlayers();
	for (Player player : players)
	    display(player, title, subtitle, time, t);

    }

    public static void broadcast(String title, String subtitle, World world) {
	broadcast(title, subtitle, world, 60, false);
    }

    public static void broadcast(String title, String subtitle) {
	broadcast(title, subtitle, null);
    }
}
