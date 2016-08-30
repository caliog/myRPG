package org.caliog.myRPG.Lib.Barkeeper.CenterBar;

import org.bukkit.entity.Player;
import org.caliog.myRPG.NMS.NMS;

public class NMSMethods {

	// TODO method name "b" is variable
	public static void sendBar(Player player, String title, String subtitle, int fadein, int active, int fadeout) {
		try {
			Class<?> iChatBaseComponent = NMS.getNMSClass("IChatBaseComponent");
			Class<?> chatSerializer = NMS.getNMSClass("IChatBaseComponent$ChatSerializer");
			Class<?> packetPlayOutTitle = NMS.getNMSClass("PacketPlayOutTitle");
			Class<?> enumTitleAction = NMS.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
			if (title != null) {
				Object bc = chatSerializer.getMethod("b", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
				Object packet = packetPlayOutTitle.getConstructor(enumTitleAction, iChatBaseComponent, int.class, int.class, int.class)
						.newInstance(enumTitleAction.getField("TITLE").get(null), bc, fadein, active, fadeout);
				NMS.sendPacket(player, packet);
			}
			if (subtitle != null) {
				Object bc = chatSerializer.getMethod("b", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
				Object packet = packetPlayOutTitle.getConstructor(enumTitleAction, iChatBaseComponent, int.class, int.class, int.class)
						.newInstance(enumTitleAction.getField("SUBTITLE").get(null), bc, fadein, active, fadeout);
				NMS.sendPacket(player, packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
