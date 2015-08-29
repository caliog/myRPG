package org.caliog.Villagers.Quests;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.caliog.myRPG.Manager;
import org.caliog.myRPG.Utils.FilePath;

import org.bukkit.configuration.file.YamlConfiguration;

public class QuestLoader {

    public static YamlConfiguration quests;

    protected static ClassLoader classLoader;

    public static void init() {
	File dir = new File(FilePath.quests);

	List<URL> urls = new ArrayList<URL>();
	for (String file : dir.list()) {
	    if (file.endsWith("Quest.jar")) {
		File f = new File(dir, file);
		try {
		    urls.add(f.toURI().toURL());
		} catch (MalformedURLException e) {
		    e.printStackTrace();
		}
	    }
	}

	classLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), Manager.plugin.getClass()
		.getClassLoader());

    }

    public static Quest load(String name) {
	if (!isJarQuest(name))
	    return null;
	try {
	    String mainC = "src/main/" + name;
	    Class<?> c = Class.forName(mainC, true, classLoader);
	    Class<? extends Quest> questC = c.asSubclass(Quest.class);
	    Quest quest = questC.getConstructor(name.getClass()).newInstance(name);
	    return quest;
	} catch (Exception e) {
	    e.printStackTrace();
	    Manager.plugin.getLogger().warning("Failed to load Quest:" + name);
	}
	return null;
    }

    public static boolean isJarQuest(String name) {
	return new File(FilePath.quests + name + "Quest.jar").exists();
    }

    public static boolean isYmlQuest(String name) {
	return new File(FilePath.quests + name + "Quest.yml").exists();
    }

    public static Quest loadByFile(String name) {
	File file = new File(FilePath.quests + name + ".Quest.yml");
	if (file.exists()) {
	    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
	    return new YmlQuest(name, config);
	}
	return null;
    }
}
