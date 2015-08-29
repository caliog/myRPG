package org.caliog.Villagers.XX;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.caliog.Villagers.Utils.LocationUtil;
import org.caliog.myRPG.Utils.FilePath;
import org.caliog.npclib.NPCManager;

public class GManager {

    private static List<Guard> guards = new ArrayList<Guard>();
    private static File f = new File(FilePath.villagerDataNPCFile);

    public static void load() throws IOException {

	if (!f.exists())
	    f.createNewFile();
	BufferedReader reader = new BufferedReader(new FileReader(f));
	String line = "";
	while ((line = reader.readLine()) != null) {
	    String key = line.split("=")[0], values[] = line.split("=")[1].split("&");
	    String name = values[0];
	    Location loc = LocationUtil.fromString(values[1]);
	    String attacking = values[2];
	    String path = values[3];
	    String eq = values[4];
	    Guard guard = new Guard(name, loc, Integer.parseInt(key), eq);
	    guard.setAttackings(attacking);
	    guard.createPath(path);
	    guards.add(guard);
	}

	reader.close();
    }

    public static void save() throws IOException {
	FileWriter writer = new FileWriter(f);
	String text = "";
	for (Guard g : guards) {
	    text += g.getId() + "=" + g.getName() + "&" + LocationUtil.toString(g.getLocation()) + "&"
		    + g.getAttackings() + "&" + g.getPathName() + "&" + g.getEquipmentString() + "\r";
	}
	writer.write(text);
	writer.close();
	NPCManager.npcManager.despawnAll();
    }

    public static Guard getGuard(UUID entityId) {
	for (Guard guard : guards) {
	    if (guard.getUniqueId() == entityId) {
		return guard;
	    }
	}
	return null;
    }

    public static Guard getClosestGuard(Location location) {
	Guard guard = null;
	double distance = 10000;
	for (Guard g : guards) {
	    double d = g.getNpc().getBukkitEntity().getLocation().distanceSquared(location);
	    if (d < distance) {
		distance = d;
		guard = g;
	    }

	}
	return guard;
    }

    public static void remove(Guard g) {
	if (g != null)
	    NPCManager.npcManager.despawnById(String.valueOf(g.getId()));
	guards.remove(g);

    }

    public static boolean isGuard(Entity e) {
	for (Guard guard : guards) {
	    if (guard.getUniqueId() == e.getUniqueId())
		return true;
	}
	return false;
    }

    public static boolean isUsedId(int id) {
	for (Guard g : guards) {
	    if (g.getId() == id)
		return true;
	}
	return false;
    }

    public static void createNewGuard(String name, Location loc) {
	int id = 1;
	while (isUsedId(id)) {
	    id++;
	}

	Guard g = new Guard(name, loc, id, null);
	guards.add(g);
    }

    public static List<Guard> getGuards() {
	return guards;
    }

    public static void doLogics() {
	GuardWatcher.run();
    }
}
