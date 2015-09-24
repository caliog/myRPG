package org.caliog.myRPG.Mobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.myConfig;
import org.caliog.myRPG.Entities.PlayerManager;
import org.caliog.myRPG.Entities.VolatileEntities;
import org.caliog.myRPG.Utils.EntityUtils;
import org.caliog.myRPG.Utils.FilePath;
import org.caliog.myRPG.Utils.Vector;

public class MobSpawner {
    public static Set<MobSpawnZone> zones = new HashSet<MobSpawnZone>();

    public static void loadZones() throws IOException {
	File f = new File(FilePath.szFile);
	if (!f.exists()) {
	    return;
	}
	BufferedReader reader = new BufferedReader(new FileReader(f));
	String line = "";
	while ((line = reader.readLine()) != null) {
	    String[] a = line.split("/");
	    if (a.length == 4) {
		Vector m;

		m = Vector.fromString(a[0]);
		if (m == null || m.isNull())
		    continue;
		String mob = null;
		if (EntityUtils.isMobClass(a[1])) {
		    mob = a[1];
		}
		int radius = Integer.parseInt(a[2]);
		int amount = Integer.parseInt(a[3]);
		if ((mob != null) && (m != null)) {
		    zones.add(new MobSpawnZone(m, mob, radius, amount));
		}

	    }
	}
	reader.close();
    }

    public static void saveZones() throws IOException {
	File f = new File(FilePath.szFile);
	f.getParentFile().mkdir();
	f.createNewFile();
	FileWriter writer = new FileWriter(f);
	String text = "";
	for (MobSpawnZone z : zones) {
	    text = text + z.getM().toString() + "/" + z.getMob() + "/" + z.getRadius() + "/" + z.getAmount() + "\r";
	}
	writer.write(text);
	writer.close();
    }

    public static boolean isNearSpawnZone(Entity e) {
	Mob m = VolatileEntities.getMob(e.getUniqueId());
	if (m != null) {
	    for (MobSpawnZone zone : zones) {
		if (zone.getM().equals(m.getSpawnZone())) {
		    if (m.getSpawnZone().distanceSquared(e.getLocation()) <= 3.5D * zone.getRadius() * zone.getRadius()) {
			return true;
		    }
		    return false;
		}
	    }
	    return false;
	}
	return true;
    }

    public static Runnable getTask() {
	return new Runnable() {
	    public void run() {
		while (MobSpawner.zones.contains(null)) {
		    MobSpawner.zones.remove(null);
		}

		Set<UUID> remove = new HashSet<UUID>();
		Set<UUID> ids = new HashSet<UUID>();
		for (World w : Manager.getWorlds()) {
		    if (w == null)
			continue;
		    for (MobSpawnZone z : MobSpawner.zones) {
			if (z.getWorld().equals(w.getName()))
			    if (Math.random() < 0.6D) {
				z.askForSpawn();
			    }
		    }
		    Mob m;
		    for (Entity e : w.getEntities()) {
			if (((e instanceof Creature)) || ((e instanceof Slime)) || ((e instanceof Ghast))) {
			    m = VolatileEntities.getMob(e.getUniqueId());
			    if (!VolatileEntities.isRegistered(e.getUniqueId())) {
				if (myConfig.isNaturalSpawnDisabled())
				    e.remove();
			    } else if (m != null) {
				ids.add(e.getUniqueId());
				for (Entity p : e.getNearbyEntities(7.0D, 5.0D, 7.0D)) {
				    if (((p instanceof Player)) && (PlayerManager.getPlayer(p.getUniqueId()) != null)
					    && (m.isAgressive()) && ((e instanceof Creature))) {
					((Creature) e).setTarget((Player) p);
				    }
				}
			    }
			}
		    }

		}
		for (Mob mob : VolatileEntities.getMobs()) {
		    if (!ids.contains(mob.getId())) {
			remove.add(mob.getId());
		    }
		}
		for (UUID id : remove) {
		    VolatileEntities.remove(id);
		}
	    }
	};
    }
}
