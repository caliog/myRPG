package org.caliog.myRPG.Entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.caliog.myRPG.myConfig;
import org.caliog.myRPG.Mobs.Mob;
import org.caliog.myRPG.Mobs.MobInstance;
import org.caliog.myRPG.Mobs.MobSpawner;
import org.caliog.myRPG.Utils.EntityUtils;
import org.caliog.myRPG.Utils.FilePath;
import org.caliog.myRPG.Utils.Vector;

public class VolatileEntities {
    private static List<Mob> mobs = new ArrayList<Mob>();
    private static Set<UUID> register = new HashSet<UUID>();

    public static void save() throws IOException {
	File f = new File(FilePath.mobsFile);
	FileWriter writer = new FileWriter(f);
	String text = "";
	World w = Bukkit.getWorld(myConfig.getWorld());
	if (w != null)
	    for (Entity e : w.getEntities()) {
		Mob m = getMob(e.getUniqueId());
		if (m != null) {
		    text = text + m.getName() + "=" + m.getId().toString() + "=" + m.getSpawnZone().toString() + "\r";
		}
	    }

	writer.write(text);
	writer.close();
	register.clear();
    }

    public static void load() throws Exception {
	File f = new File(FilePath.mobsFile);
	if (!f.exists()) {
	    return;
	}
	BufferedReader reader = new BufferedReader(new FileReader(f));
	String line = "";
	while ((line = reader.readLine()) != null) {
	    String m = line.split("=")[0];
	    if (EntityUtils.isMobClass(m)) {
		UUID uuid = UUID.fromString(line.split("=")[1]);

		getMobs().add(new MobInstance(m, uuid, Vector.fromString(line.split("=")[2])));
		register(uuid);
		World w = Bukkit.getWorld(myConfig.getWorld());
		if (w != null)
		    for (Entity entity : w.getEntities()) {

			if ((entity.getUniqueId().equals(uuid)) && (!MobSpawner.isNearSpawnZone(entity))) {
			    remove(uuid);
			    entity.remove();
			}

		    }
	    }
	}
	reader.close();
	f.delete();
    }

    public static boolean remove(UUID entityId) {
	for (Mob m : mobs) {
	    if (m.getId().equals(entityId)) {
		m.delete();
		mobs.remove(m);
		unregister(m.getId());
		return true;
	    }
	}
	return false;
    }

    public static void register(Mob mob) {
	remove(mob.getId());
	register(mob.getId());
	mobs.add(mob);
    }

    public static Mob getMob(UUID entityId) {
	for (Mob m : mobs) {
	    if (m.getId().equals(entityId)) {
		return m;
	    }
	}
	return null;
    }

    public static void register(UUID id) {
	register.add(id);
    }

    public static void unregister(UUID id) {
	register.remove(id);
    }

    public static boolean isRegistered(UUID uuid) {
	return register.contains(uuid);
    }

    public static List<Mob> getMobs() {
	return mobs;
    }

    public static void setMobs(List<Mob> mobs) {
	VolatileEntities.mobs = mobs;
    }

    public static void killAllMobs() {
	mobs.clear();
    }
}
