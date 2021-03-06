package org.caliog.npclib;

import java.util.ArrayList;

import org.bukkit.Location;

public class NPCPath {

	private final ArrayList<Node> path;
	private final NPCPathFinder pathFinder;
	private final Location end;

	public NPCPath(NPCPathFinder npcPathFinder, ArrayList<Node> path, Location end) {
		this.path = path;
		this.end = end;
		pathFinder = npcPathFinder;
	}

	public Location getEnd() {
		return end;
	}

	public ArrayList<Node> getPath() {
		return path;
	}

	public boolean checkPath(Node node, Node parent, boolean update) {
		return pathFinder.checkPath(node, parent, update);
	}

}
