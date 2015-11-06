package org.caliog.myRPG.Lib.Barkeeper.TopBar;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class FakeEntity {

	private final float maxHealth = 200F;
	private String name;
	private final int x, y, z;
	private final byte motX = 0, motY = 0, motZ = 0;
	private float pitch = 0F, yaw = 0F;
	private float health = 0F;
	private World world;
	private int id;
	private Entity entity;

	public FakeEntity(String name, Location loc) {
		this.name = name;
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		this.pitch = loc.getPitch();
		this.yaw = loc.getYaw();
		this.world = loc.getWorld();
	}

	public FakeEntity(String name, Location loc, float p) {
		this(name, loc);
		this.health = p * maxHealth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public float getHealth() {
		return health;
	}

	public void setHealthPercentage(float p) {
		this.health = p * maxHealth;
	}

	public World getWorld() {
		return world;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public byte getMotX() {
		return motX;
	}

	public byte getMotY() {
		return motY;
	}

	public byte getMotZ() {
		return motZ;
	}

	public void setId(int id) {
		this.id = id;

	}

	public int getId() {
		return id;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

	public float getMaxHealth() {
		return maxHealth;
	}
}
