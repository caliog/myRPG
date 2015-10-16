package org.caliog.myRPG.Lib.Barkeeper.BottomBar.v1_7_R4;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSUtil extends org.caliog.myRPG.Lib.Barkeeper.BottomBar.NMSUtil {
    public void sendHotBar(Player player, String msg) {
	IChatBaseComponent bc = ChatSerializer.a("{'text': '" + msg + "'}");
	PacketPlayOutChat packet = new PacketPlayOutChat(bc, true);
	sendPacket(player, packet);
    }

    private void sendPacket(Player p, Packet packet) {
	EntityPlayer player = getHandle(p);
	if (player.playerConnection != null) {
	    player.playerConnection.sendPacket(packet);
	}
    }

    private EntityPlayer getHandle(Player player) {
	return ((CraftPlayer) player).getHandle();
    }
}
