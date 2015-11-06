package org.caliog.myRPG.Mobs;

import org.bukkit.Location;
import org.caliog.myRPG.Utils.Vector;

public class MobSpawnPoint extends MobSpawnZone {

	private String castle;

	public MobSpawnPoint(Location loc, String m, String castle) {
		super(loc, 1, 1, m);
		this.castle = castle;
	}

	public MobSpawnPoint(Vector m, String mob, String castle) {
		super(m, mob, 1, 1);
		this.castle = castle;
	}

	public String getCastle() {
		return castle;
	}

	public void setCastle(String castle) {
		this.castle = castle;
	}

	public void askCastleSpawn() {
		super.askForSpawn();
	}

	@Override
	public void askForSpawn() {

	}

	@Override
	public void askForSpawn(int t) {

	}

	public static MobSpawnPoint fromString(String str) {
		String[] split = str.split("&");
		if (split.length != 3)
			return null;
		Vector v = Vector.fromString(split[0]);
		if (v == null)
			return null;
		return new MobSpawnPoint(v, split[1], split[2]);
	}

	public String toString() {
		return this.getM().toString() + "&" + this.getMob() + "&" + castle;
	}

	public boolean isDead() {
		return countMobs() == 0;
	}
}
