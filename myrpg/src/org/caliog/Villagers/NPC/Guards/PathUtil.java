package org.caliog.Villagers.NPC.Guards;

import java.util.HashMap;

import org.bukkit.Location;

public class PathUtil {

    private static HashMap<String, CheckpointPath> paths = new HashMap<String, CheckpointPath>();

    public static void setPath(String name, int cp, int delay, Location loc) {
	CheckpointPath path = null;
	if (paths.containsKey(name)) {
	    path = paths.get(name);
	    path.setCheckpoint(loc, cp);
	} else {
	    path = new CheckpointPath(name);
	    if (!path.isLoaded())
		path = new CheckpointPath(name, cp, delay, loc);
	    paths.put(name, path);
	}

    }

    public static CheckpointPath getPath(String string) {
	CheckpointPath path = paths.get(string);
	paths.remove(string);
	return path;
    }
}
