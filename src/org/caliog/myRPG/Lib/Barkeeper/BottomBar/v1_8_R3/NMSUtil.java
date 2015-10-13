package org.caliog.myRPG.Lib.Barkeeper.BottomBar.v1_8_R3;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSUtil extends org.caliog.myRPG.Lib.Barkeeper.BottomBar.NMSUtil {
    public void sendHotBar(Player player, String msg) {
	IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{'text': '" + msg + "'}");
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
