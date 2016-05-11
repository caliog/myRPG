package org.caliog.myRPG.Utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.caliog.Villagers.NPC.Guards.GManager;
import org.caliog.Villagers.NPC.Guards.Guard;

import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;;

public class PlayerList {

	public static void refreshList() {
		ArrayList<EntityPlayer> hide = new ArrayList<EntityPlayer>();
		for (Guard guard : GManager.getGuards()) {
			EntityPlayer npc = (EntityPlayer) ((CraftPlayer) guard.getBukkitEntity()).getHandle();
			hide.add(npc);
		}

		if (hide.isEmpty())
			return;

		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, hide.toArray(new EntityPlayer[1]));
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (hide.contains((EntityPlayer) ((CraftPlayer) p).getHandle()))
				continue;
			try {
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			} catch (Exception e) {
				// TODO
			}
		}
	}

	public static void refreshList(Player p) {
		ArrayList<EntityPlayer> hide = new ArrayList<EntityPlayer>();
		for (Guard guard : GManager.getGuards()) {
			EntityPlayer npc = (EntityPlayer) ((CraftPlayer) guard.getBukkitEntity()).getHandle();
			hide.add(npc);
		}

		if (hide.isEmpty())
			return;

		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, hide.toArray(new EntityPlayer[1]));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);

	}

}
