package org.caliog.myRPG.Lib.Barkeeper.BottomBar.v1_9_R1;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;

public class NMSUtil extends org.caliog.myRPG.Lib.Barkeeper.BottomBar.NMSUtil {
	public void sendHotBar(Player player, String msg) {
		IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.b("{'text': '" + msg + "'}");
		PacketPlayOutChat packet = new PacketPlayOutChat(bc, (byte) 2);
		sendPacket(player, packet);
	}

	private void sendPacket(Player p, Packet<?> packet) {
		EntityPlayer player = getHandle(p);
		if (player.playerConnection != null) {
			player.playerConnection.sendPacket(packet);
		}
	}

	private EntityPlayer getHandle(Player player) {
		return ((CraftPlayer) player).getHandle();
	}
}
