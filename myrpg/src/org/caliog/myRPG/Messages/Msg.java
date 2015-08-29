package org.caliog.myRPG.Messages;

import java.io.File;

import org.caliog.myRPG.Manager;
import org.caliog.myRPG.Commands.Utils.CommandField;
import org.caliog.myRPG.Utils.FilePath;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Msg {

    public static final String WEAPON = "%WEAPON%";
    public static final String LEVEL = "%LEVEL%";
    public static final String CLASS = "%CLASS%";
    public static final String PLAYER = "%PLAYER%";
    private static YamlConfiguration file = YamlConfiguration.loadConfiguration(new File(FilePath.messages));

    private static boolean sendMessageTo(Player player, String msg) {
	if (msg == null || msg.length() == 0) {
	    Manager.plugin.getLogger().warning("Message error! Look over your messages file!");
	    return false;
	} else
	    player.sendMessage(msg);
	return true;
    }

    public static void sendMessage(Player player, String msgKey, String[] key, String[] replace) {
	if (key != null)
	    if (key.length != replace.length) {
		Manager.plugin.getLogger().warning("Parameter Error in Msg.java");
		return;
	    }

	String msg = file.getString(msgKey);
	if (msg == null)
	    return;
	if (key != null)
	    for (int i = 0; i < key.length; i++)
		msg = msg.replace(key[i], replace[i]);
	msg = ChatColor.translateAlternateColorCodes('&', msg);

	sendMessageTo(player, msg);
    }

    public static void sendMessage(Player player, String msgKey, String key, String replace) {
	String[] a = { key }, b = { replace };
	sendMessage(player, msgKey, a, b);
    }

    public static void sendMessage(Player player, String msgKey) {
	String[] a = null, b = null;
	sendMessage(player, msgKey, a, b);
    }

    public static void commandUsageError(CommandField field, Player player) {
	String type = "";
	if (field.getType().contains("positive"))
	    type += "positive integer";
	else if (field.getType().contains("not-negative"))
	    type += "not-negative integer";
	else if (field.getType().contains("integer"))
	    type += "integer";
	String message = field.getName() + " has to be a " + type + "!";
	sendMessageTo(player, ChatColor.RED + message);
    }

    public static void commandOptionError(CommandField field, Player player) {
	sendMessageTo(player, ChatColor.RED + field.getName() + " has to be on of these options: " + field.getType());
    }

    public static void noPermission(Player player) {
	sendMessageTo(player, ChatColor.RED + "You do not have the permission to do this!");
    }

}
