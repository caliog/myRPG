package org.caliog.myRPG.Lib.Barkeeper.CenterBar.v1_8_R3;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSUtil extends org.caliog.myRPG.Lib.Barkeeper.CenterBar.NMSUtil {
    public void sendBar(Player player, String title, String subtitle, int fadein, int active, int fadeout) {
	if (title != null) {
	    PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
		    ChatSerializer.a("{\"text\":\"" + title + "\"}"), fadein, active, fadeout);
	    sendPacket(player, packet);
	}
	if (subtitle != null) {
	    PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
		    ChatSerializer.a("{\"text\":\"" + subtitle + "\"}"), fadein, active, fadeout);
	    sendPacket(player, packet);

	}
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
