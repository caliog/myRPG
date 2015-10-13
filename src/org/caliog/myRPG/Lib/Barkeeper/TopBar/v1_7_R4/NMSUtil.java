package org.caliog.myRPG.Lib.Barkeeper.TopBar.v1_7_R4;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.EntityEnderDragon;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.caliog.myRPG.Lib.Barkeeper.TopBar.FakeEntity;
import org.caliog.myRPG.Lib.Barkeeper.TopBar.TopBar;

public class NMSUtil extends org.caliog.myRPG.Lib.Barkeeper.TopBar.NMSUtil {

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
	watcher.a(0, (byte) 0x20);
	watcher.a(6, entity.getHealth());
	watcher.a(7, Integer.valueOf(0));
	watcher.a(8, (Byte) (byte) 0);
	watcher.a(10, entity.getName());
	watcher.a(11, (Byte) (byte) 1);

	PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entity.getId(), watcher, true);
	sendPacket(player, packet);
    }

    @Override
    public void sendTeleport(Player player, FakeEntity entity) {
	Location loc = TopBar.transform(player.getLocation());
	PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity.getId(), loc.getBlockX() * 32,
		loc.getBlockY() * 32, loc.getBlockZ() * 32, Byte.valueOf((byte) ((int) loc.getYaw() * 256 / 360)),
		Byte.valueOf((byte) ((int) loc.getPitch() * 256 / 360)));
	sendPacket(player, packet);

    }

    private void sendPacket(Player p, Packet packet) {
	net.minecraft.server.v1_7_R4.EntityPlayer player = getHandle(p);
	if (player.playerConnection != null)
	    player.playerConnection.sendPacket(packet);
    }

    private net.minecraft.server.v1_7_R4.World getHandle(World world) {
	return ((CraftWorld) world).getHandle();
    }

    private EntityPlayer getHandle(Player player) {
	return ((CraftPlayer) player).getHandle();
    }

    private net.minecraft.server.v1_7_R4.Entity getHandle(org.bukkit.entity.Entity entity) {
	return ((CraftEntity) entity).getHandle();
    }

}
