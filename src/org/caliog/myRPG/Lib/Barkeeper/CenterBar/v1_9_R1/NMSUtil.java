package org.caliog.myRPG.Lib.Barkeeper.CenterBar.v1_9_R1;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;

public class NMSUtil extends org.caliog.myRPG.Lib.Barkeeper.CenterBar.NMSUtil {
	public void sendBar(Player player, String title, String subtitle, int fadein, int active, int fadeout) {
		if (title != null) {
			PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
					ChatSerializer.b("{\"text\":\"" + title + "\"}"), fadein, active, fadeout);
			sendPacket(player, packet);
		}
		if (subtitle != null) {
			PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
					ChatSerializer.b("{\"text\":\"" + subtitle + "\"}"), fadein, active, fadeout);
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
