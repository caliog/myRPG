package org.caliog.myRPG.Entities;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.caliog.myRPG.Classes.ClazzLoader;
import org.caliog.myRPG.Group.GroupManager;
import org.caliog.myRPG.Utils.FilePath;

public class PlayerManager {
    private static HashMap<UUID, myClass> players = new HashMap<UUID, myClass>();
    private static File f = new File(FilePath.players);

    public static void save() throws IOException {
	f.mkdir();
	for (UUID id : players.keySet()) {
	    save((myPlayer) players.get(id));
	}
    }

    public static void load() {
	for (Player p : Bukkit.getServer().getOnlinePlayers()) {
	    login(p);
	}
    }

    public static void save(myPlayer p) throws IOException {
	myClass player = (myClass) p;
	File ff = new File(f.getAbsolutePath() + "/" + "players" + ".yml");
	if (!ff.exists()) {
	    ff.createNewFile();
	}
	YamlConfiguration config = YamlConfiguration.loadConfiguration(ff);
	config.set(p.getName(), player.getType());
	config.save(ff);
	player.save();
    }

    public static boolean login(Player player) {
	try {
	    File ff = new File(f.getAbsolutePath() + "/" + "players" + ".yml");
	    if (!ff.exists()) {
		ff.createNewFile();
	    }
	    YamlConfiguration config = YamlConfiguration.loadConfiguration(ff);
	    String type = config.getString(player.getName());
	    myClass p = getPlayer(player, type);
	    register(p);
	    return true;
	} catch (Exception e) {
	}
	return false;
    }

    public static void logout(Player player) {
	f.mkdir();
	myPlayer p = (myPlayer) players.get(player.getUniqueId());
	if (p == null) {
	    return;
	}
	try {
	    save(p);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	players.remove(player.getUniqueId());
    }

    public static myClass getPlayer(Player player, String clazz) {
	myClass p = null;

	p = ClazzLoader.create(player, clazz);

	if (p == null) {
	    return null;
	}
	return p;
    }

    private static void register(myClass p) {
	if (p == null)
	    return;
	players.put(p.getPlayer().getUniqueId(), p);
    }

    public static void register(Player player, String clazz) {
	register(getPlayer(player, clazz));
    }

    public static myClass getPlayer(UUID player) {
	return (myClass) players.get(player);
    }

    public static myClass getPlayer(String string) {
	for (UUID i : players.keySet()) {
	    if (((myClass) players.get(i)).getName().equals(string)) {
		return (myClass) players.get(i);
	    }
	}
	return null;
    }

    public static void task(long seconds) {
	for (myClass clazz : players.values()) {
	    int i = clazz.getIntelligence();
	    int s = 1;
	    if (i < 20) {
		s = 5;
	    } else if (i < 45) {
		s = 4;
	    } else if (i < 60) {
		s = 3;
	    } else if (i <= 80) {
		s = 2;
	    }

	    if (seconds % s == 0.0F) {
		clazz.regainFood();
	    }
	    if (clazz.getHealth() != 0.0D) {
		if (seconds % 5 == 0.0F) {
		    clazz.addHealth(clazz.getMaxHealth() * 0.05D);
		}
		double d = clazz.getHealth() / clazz.getMaxHealth();

		clazz.getPlayer().setHealth(20.0D * (d > 1.0D ? 1.0D : d));
		if (!GroupManager.isInGroup(clazz.getPlayer())) {
		    clazz.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	    }
	}
    }

    public static void changeClass(Player player, String clazz) {
	logout(player);
	register(player, clazz);
	if (player.getLevel() <= 0)
	    player.setLevel(1);
    }

    public static void respawn(Player player) {
	myPlayer p = getPlayer(player.getUniqueId());
	if (p == null) {
	    return;
	}
	Location loc = player.getBedSpawnLocation();
	if (loc != null) {
	    player.teleport(loc);
	} else {
	    player.teleport(player.getWorld().getSpawnLocation());
	}
    }

    public static Collection<myClass> getPlayers() {
	return players.values();
    }
}
