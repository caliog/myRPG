package org.caliog.npclib.v1_8_R2;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.SocketAddress;

import net.minecraft.server.v1_8_R2.EnumProtocolDirection;
import net.minecraft.server.v1_8_R2.NetworkManager;

public class NPCNetworkManager extends NetworkManager {

	public NPCNetworkManager() throws IOException {

		super(EnumProtocolDirection.CLIENTBOUND);

		try {
			Field channel = getField("k");
			Field address = getField("l");

			if (channel == null || address == null)
				return;
			channel.set(this, new NPCChannel());
			address.set(this, new SocketAddress() {
				private static final long serialVersionUID = 2173638219433070267L;

			});
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

	}

	private Field getField(String string) throws NoSuchFieldException, SecurityException {
		Field f = NetworkManager.class.getDeclaredField(string);
		f.setAccessible(true);
		return f;
	}
}
