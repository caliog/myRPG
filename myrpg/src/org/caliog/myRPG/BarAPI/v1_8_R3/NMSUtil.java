package org.caliog.myRPG.BarAPI.v1_8_R3;

import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.caliog.myRPG.BarAPI.BarAPI;
import org.caliog.myRPG.BarAPI.FakeEntity;

public class NMSUtil extends org.caliog.myRPG.BarAPI.NMSUtil {

    @Override
    public void sendSpawnPacket(Player player, FakeEntity fakeEntity) {
	EntityEnderDragon dragon = new EntityEnderDragon(getHandle(fakeEntity.getWorld()));
	dragon.setLocation(fakeEntity.getX(), fakeEntity.getY(), fakeEntity.getZ(), fakeEntity.getPitch(),
		fakeEntity.getYaw());
	dragon.setInvisible(true);
	dragon.setCustomName(fakeEntity.getName());
	dragon.setHealth(fakeEntity.getHealth());
	dragon.motX = fakeEntity.getMotX();
	dragon.motY = fakeEntity.getMotY();
	dragon.motZ = fakeEntity.getMotZ();
	fakeEntity.setId(dragon.getId());
	fakeEntity.setEntity(dragon.getBukkitEntity());
	PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(dragon);
	sendPacket(player, packet);
	return;
    }

    @Override
    public void sendDestroyPacket(Player player, FakeEntity entity) {
	PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy();
	try {
	    Field a = packet.getClass().getDeclaredField("a");
	    a.setAccessible(true);

	    a.set(packet, new int[] { entity.getId() });
	    sendPacket(player, packet);
	} catch (Exception e) {
	}
    }

    @Override
    public void sendMetaData(Player player, FakeEntity entity) {
	DataWatcher watcher = new DataWatcher(getHandle(entity.getEntity()));
	watcher.a(5, 32);
	watcher.a(6, entity.getHealth());
	watcher.a(7, Integer.valueOf(0));
	watcher.a(8, (byte) 0);
	watcher.a(10, entity.getName());
	watcher.a(11, (byte) 1);

	PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entity.getId(), watcher, true);
	sendPacket(player, packet);
    }

    @Override
    public void sendTeleport(Player player, FakeEntity entity) {
	Location loc = BarAPI.transform(player.getLocation());
	PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity.getId(), loc.getBlockX() * 32,
		loc.getBlockY() * 32, loc.getBlockZ() * 32, Byte.valueOf((byte) ((int) loc.getYaw() * 256 / 360)),
		Byte.valueOf((byte) ((int) loc.getPitch() * 256 / 360)), false);
	sendPacket(player, packet);

    }

    private void sendPacket(Player p, Packet<?> packet) {
	net.minecraft.server.v1_8_R3.EntityPlayer player = getHandle(p);
	if (player.playerConnection != null)
	    player.playerConnection.sendPacket(packet);
    }

    private net.minecraft.server.v1_8_R3.World getHandle(World world) {
	return ((CraftWorld) world).getHandle();
    }

    private EntityPlayer getHandle(Player player) {
	return ((CraftPlayer) player).getHandle();
    }

    private net.minecraft.server.v1_8_R3.Entity getHandle(org.bukkit.entity.Entity entity) {
	return ((CraftEntity) entity).getHandle();
    }

}
