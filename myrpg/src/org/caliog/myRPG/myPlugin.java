package org.caliog.myRPG;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.caliog.Villagers.Listeners.VillagerListener;
import org.caliog.myRPG.Commands.Utils.CommandRegister;
import org.caliog.myRPG.Listeners.myListener;
import org.caliog.myRPG.Resource.FileCreator;
import org.caliog.myRPG.Utils.DataFolder;
import org.caliog.myRPG.Utils.FilePath;
import org.caliog.myRPG.Utils.myUtils;

public class myPlugin extends JavaPlugin {
    public myListener listener;
    public CommandRegister cmdReg;
    private String version;
    private FileCreator fc = new FileCreator();

    public void onEnable() {
	String pN = Bukkit.getServer().getClass().getPackage().getName();
	version = pN.substring(pN.lastIndexOf(".") + 1);
	mkdir();

	Manager.plugin = this;
	cmdReg = new CommandRegister();

	myConfig.config = YamlConfiguration.loadConfiguration(new File(FilePath.config));
	createMIC();
	Manager.load();
	this.listener = new myListener();
	getServer().getPluginManager().registerEvents(this.listener, this);
	getServer().getPluginManager().registerEvents(new VillagerListener(), this);//Villager

	Manager.scheduleRepeatingTask(Manager.getTask(), 20L, 1L);

	getLogger().info(getDescription().getFullName() + " enabled!");
    }

    public void onDisable() {
	Manager.save();

	DataFolder.backup();

	Manager.cancelAllTasks();

	getLogger().info(getDescription().getFullName() + " disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	Player player = null;
	if (sender instanceof Player)
	    player = (Player) sender;
	else {
	    sender.sendMessage(ChatColor.RED + "Only for players, sorry!");//const
	    return false;
	}

	String command = "";
	for (String a : args)
	    command += a + " ";
	command = command.trim();
	int count = command.length() - command.replace("\"", "").length();
	String a[] = new String[args.length];
	if (count < 2) {
	    a = args;
	} else {

	    int counter = 0;
	    for (int i = 0; i < args.length; i++) {

		if ((args[i].startsWith("\""))) {
		    a[counter] = "";
		    while (i < args.length && !args[i].endsWith("\"")) {
			a[counter] += " " + args[i].replace("\"", "");
			i++;
		    }
		    if (i < args.length)
			a[counter] += " " + args[i].replace("\"", "");
		    a[counter] = a[counter].trim();
		    counter++;
		} else if (counter < a.length) {
		    a[counter] = args[i];
		    counter++;
		}

	    }
	}
	return cmdReg.executeCommand(cmd.getName(), myUtils.removeNull(a), player);

    }

    private void mkdir() {
	for (Field f : FilePath.class.getFields()) {
	    try {
		String value = (String) f.get(this);
		String[] split = value.split("/");
		String name = split[split.length - 1];
		File file = new File(value);
		if (!value.equals(FilePath.mic))
		    if (!file.exists()) {
			if (value.endsWith("/")) {
			    file.mkdir();
			} else {
			    file.createNewFile();
			    fc.copyFile(value, name);
			}
		    }
	    } catch (IllegalArgumentException | IllegalAccessException | IOException e) {
		e.printStackTrace();
	    }
	}

    }

    public String getVersion() {
	return version;
    }

    public boolean createMIC() {
	return createMIC(null);
    }

    public boolean createMIC(final Player player) {
	final File micFile = new File(FilePath.mic);
	if (micFile.exists() && micFile.length() != 0)
	    return false;
	if (!myConfig.isMICDisabled())
	    new Thread(new Runnable() {

		@Override
		public void run() {
		    try {
			micFile.createNewFile();
			getLogger().log(Level.INFO, "Starting download MIC.jar...");
			FileCreator.copyURL(micFile, "http://www.caliog.org/MIC.jar");
			getLogger().log(Level.INFO, "Finished download of MIC.jar!");
			if (player != null)
			    player.sendMessage(ChatColor.GREEN + "Finished download of MIC.jar!");
		    } catch (IOException e) {
			getLogger().log(Level.WARNING, "Download of MIC.jar failed!");
			if (player != null)
			    player.sendMessage(ChatColor.GREEN + "Download of MIC.jar failed!");
		    }

		}
	    }).start();

	return true;
    }
}
