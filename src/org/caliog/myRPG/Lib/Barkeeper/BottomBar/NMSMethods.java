package org.caliog.myRPG.Lib.Barkeeper.BottomBar;

import org.bukkit.entity.Player;
import org.caliog.myRPG.NMS.NMS;

public class NMSMethods {

	public static void sendHotBar(Player player, String msg) {
		try {
			Class<?> chatSerializer = NMS.getNMSClass("IChatBaseComponent$ChatSerializer");
			Class<?> packetPlayOutChat = NMS.getNMSClass("PacketPlayOutChat");

			// TODO method name "b" is variable
			Object bc = chatSerializer.getMethod("b", String.class).invoke(null, "{'text': '" + msg + "'}");
			Object packet = packetPlayOutChat.getConstructor(NMS.getNMSClass("IChatBaseComponent"), byte.class).newInstance(bc, (byte) 2);
			NMS.sendPacket(player, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
