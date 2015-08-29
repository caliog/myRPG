package org.caliog.myRPG;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.caliog.Villagers.Chat.ChatManager;
import org.caliog.Villagers.NPC.Util.VManager;
import org.caliog.Villagers.Quests.QManager;
import org.caliog.Villagers.Quests.QuestKill;
import org.caliog.Villagers.Quests.QuestLoader;
import org.caliog.Villagers.Utils.DataSaver;
import org.caliog.Villagers.XX.GManager;
import org.caliog.myRPG.Classes.ClazzLoader;
import org.caliog.myRPG.Commands.Utils.Permissions;
import org.caliog.myRPG.Entities.PlayerManager;
import org.caliog.myRPG.Entities.Playerface;
import org.caliog.myRPG.Entities.VolatileEntities;
import org.caliog.myRPG.Entities.myClass;
import org.caliog.myRPG.Mobs.MobSpawner;
import org.caliog.myRPG.Utils.FilePath;
import org.caliog.npclib.NMS;
import org.caliog.npclib.NPCManager;

public class Manager {
    public static myPlugin plugin;
    private static long timer = 0L;
    public static float seconds;

    public static myClass getPlayer(UUID id) {
	return PlayerManager.getPlayer(id);
    }

    public static Runnable getTask() {
	return new Runnable() {
	    public void run() {
		Manager.timer += 1L;
		Manager.seconds = (float) Manager.timer / 20.0F;
		if (Manager.timer % 5L == 0L) {
		    MobSpawner.getTask().run();
		}
		PlayerManager.task(Manager.seconds);
		VManager.doLogics();
		GManager.doLogics();
	    }
	};
    }

    public static void save() {

	try {
	    MobSpawner.saveZones();
	    VolatileEntities.save();
	    PlayerManager.save();
	    Playerface.clear();

	    //Villager stuff
	    VManager.save();
	    GManager.save();
	    QuestKill.save();
	    ChatManager.clear();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void load() {
	myConfig.config = YamlConfiguration.loadConfiguration(new File(FilePath.config));
	ClazzLoader.classes = YamlConfiguration.loadConfiguration(new File(FilePath.classes));
	QuestLoader.quests = YamlConfiguration.loadConfiguration(new File(FilePath.quests));
	Permissions.declare();

	//Villager
	NPCManager.npcManager = NMS.getNPCManager();
	QManager.init();
	try {
	    VManager.load();
	    GManager.load();

	    QuestKill.load();

	    //Villager END

	    MobSpawner.loadZones();
	    VolatileEntities.load();
	    PlayerManager.load();

	    DataSaver.clean();//this has to be the last thing to do
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static int scheduleRepeatingTask(Runnable r, long d, long p) {
	return Bukkit.getScheduler().scheduleSyncRepeatingTask(Manager.plugin, r, d, p);
    }

    public static int scheduleTask(Runnable r, long d) {
	return Bukkit.getScheduler().scheduleSyncDelayedTask(Manager.plugin, r, d);
    }

    public static int scheduleTask(Runnable r) {
	return Bukkit.getScheduler().scheduleSyncDelayedTask(Manager.plugin, r);
    }

    public static void cancelTask(Integer id) {
	Bukkit.getScheduler().cancelTask(id.intValue());
    }

    public static void cancelAllTasks() {
	Bukkit.getScheduler().cancelTasks(Manager.plugin);
    }

    public static void scheduleRepeatingTask(Runnable r, long i, long j, long l) {
	final int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Manager.plugin, r, i, j);
	Bukkit.getScheduler().scheduleSyncDelayedTask(Manager.plugin, new Runnable() {
	    public void run() {
		Bukkit.getScheduler().cancelTask(taskId);
	    }
	}, l);
    }

    public static void broadcast(String string) {
	for (myClass p : PlayerManager.getPlayers())
	    p.getPlayer().sendMessage(string);

    }
}
