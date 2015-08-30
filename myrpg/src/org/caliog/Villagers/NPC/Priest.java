package org.caliog.Villagers.NPC;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.Classes.ClazzLoader;
import org.caliog.myRPG.Entities.PlayerManager;
import org.caliog.myRPG.Messages.Msg;

public class Priest extends Villager {

    Set<UUID> players = new HashSet<UUID>();

    public Priest(org.bukkit.entity.Villager entity, Location location, String name) {
	super(entity, VillagerType.PRIEST, location, name);
    }

    private String getClassType() {
	String n = this.getBukkitEntity().getName();
	if (ClazzLoader.isClass(ChatColor.stripColor(n)))
	    return n;
	else
	    return null;

    }

    public void onInteract(Player player) {
	if (getClassType() == null)
	    return;
	if (players.contains(player.getUniqueId()))
	    changeClass(player);
	else
	    offerChange(player);
    }

    private void offerChange(final Player player) {
	Msg.sendMessage(player, "class-change-offer", Msg.CLASS, getClassType());
	players.add(player.getUniqueId());
	Manager.scheduleTask(new Runnable() {

	    @Override
	    public void run() {
		players.remove(player.getUniqueId());
	    }
	});
    }

    private void changeClass(Player player) {
	PlayerManager.changeClass(player, ChatColor.stripColor(getClassType()));
	Msg.sendMessage(player, "class-changed", Msg.CLASS, getClassType());
    }

}
