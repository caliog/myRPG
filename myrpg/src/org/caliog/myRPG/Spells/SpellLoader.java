package org.caliog.myRPG.Spells;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.caliog.myRPG.Manager;
import org.caliog.myRPG.Entities.myClass;
import org.caliog.myRPG.Utils.FilePath;

public class SpellLoader {

    protected static ClassLoader classLoader;

    public static void init() {
	File dir = new File(FilePath.spells);

	List<URL> urls = new ArrayList<URL>();
	for (String file : dir.list()) {
	    if (file.endsWith("Spell.jar")) {
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

    public static Spell load(myClass player, String name) {
	if (!isSpell(name))
	    return null;
	try {
	    String mainC = "src/main/" + name;
	    Class<?> c = Class.forName(mainC, true, classLoader);
	    Class<? extends Spell> spellC = c.asSubclass(Spell.class);
	    Spell spell = spellC.getConstructor(player.getClass()).newInstance(player);
	    return spell;
	} catch (Exception e) {
	    e.printStackTrace();
	    Manager.plugin.getLogger().warning("Failed to load Spell:" + name);
	}
	return null;
    }

    public static boolean isSpell(String name) {
	File file = new File(FilePath.spells + name + "Spell.jar");
	return file.exists();
    }
}
