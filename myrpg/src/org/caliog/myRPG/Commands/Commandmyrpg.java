package org.caliog.myRPG.Commands;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.myConfig;
import org.caliog.myRPG.Commands.Utils.Command;
import org.caliog.myRPG.Commands.Utils.CommandExecutable;
import org.caliog.myRPG.Commands.Utils.CommandField;
import org.caliog.myRPG.Commands.Utils.CommandField.FieldProperty;
import org.caliog.myRPG.Commands.Utils.Commands;
import org.caliog.myRPG.Utils.FilePath;

public class Commandmyrpg extends Commands {

    @Override
    public List<Command> getCommands() {
	/*
	 * Name: myrpg
	 * 
	 * Permission: null
	 * 
	 * Usage: /myrpg
	 */
	cmds.add(new Command("myrpg", null, new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		player.sendMessage(Manager.plugin.getDescription().getFullName());
		player.sendMessage("Type /myrpg help [page] for commands!");
	    }
	}));

	/*
	 * Name: myrpg
	 * SubName: reload
	 * 
	 * Permission: myrpg.reload
	 * 
	 * Usage: /myrpg
	 */
	cmds.add(new Command("myrpg", "myrpg.reload", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		myConfig.config = YamlConfiguration.loadConfiguration(new File(FilePath.config));
		player.sendMessage(ChatColor.GREEN + "Reloaded " + Manager.plugin.getDescription().getFullName());
	    }
	}, new CommandField("reload", FieldProperty.IDENTIFIER)));

	/*
	 * Name: myrpg
	 * SubName: help
	 * 
	 * Permission: myrpg.help
	 * 
	 * Usage: /myrpg help [page]
	 */
	cmds.add(new Command("myrpg", null, new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		int page = 1;
		if (args.length == 2)
		    page = Integer.parseInt(args[1]);
		player.sendMessage(ChatColor.BLUE + "All permitted myRPG commands: (Page " + page + ")");
		int counter = 0;
		int limit = 9 * page;
		if (Manager.plugin.cmdReg.getPermittedCommands(player).size() >= limit - 9)
		    for (Command cmd : Manager.plugin.cmdReg.getPermittedCommands(player)) {
			if (counter >= limit - 9 && counter < limit)
			    player.sendMessage(ChatColor.GREEN + cmd.getUsage());
			counter++;
		    }
	    }
	}, new CommandField("help", FieldProperty.IDENTIFIER), new CommandField("page", "positve integer",
		FieldProperty.OPTIONAL)));
	/*
	 * Name: myrpg
	 * 
	 * Permission: myrpg.mic
	 * 
	 * Usage: /myrpg
	 */
	cmds.add(new Command("myrpg", "myrpg.mic", new CommandExecutable() {

	    @Override
	    public void execute(String[] args, Player player) {
		if (myConfig.isMICDisabled())
		    player.sendMessage(ChatColor.RED + "MIC is disabled in config!");
		else {
		    if (Manager.plugin.createMIC())
			player.sendMessage(ChatColor.GRAY + "Created MIC.jar in your config folder.. Use it wisely!");
		    else
			player.sendMessage(ChatColor.RED + "Couldn't create MIC.jar, is it already existing?");
		}
	    }
	}, new CommandField("mic", FieldProperty.IDENTIFIER)));

	return cmds;
    }
}
