package org.caliog.Villagers.Chat;

import java.util.HashMap;

import org.caliog.Villagers.NPC.Villager;
import org.caliog.myRPG.Manager;

import org.bukkit.entity.Player;

public class ChatManager {

    private static HashMap<String, Chat> playerChats = new HashMap<String, Chat>();
    private static HashMap<String, Integer> taskIds = new HashMap<String, Integer>();

    public static void clear() {
	playerChats.clear();
    }

    public static void interaction(final Player player, Villager vil, boolean b) {
	final String id = player.getName() + "+" + vil.getName();
	if (playerChats.containsKey(id)) {
	    Chat chat = playerChats.get(id);
	    if (!chat.isEnded()) {
		if (chat.isListening()) {
		    chat.answer(b);
		}
	    } else {
		if (!taskIds.containsKey(id))
		    taskIds.put(id, Manager.scheduleTask(new Runnable() {

			@Override
			public void run() {
			    playerChats.remove(id);
			    taskIds.remove(id);

			}
		    }, 20L * 10));

	    }
	} else {
	    createNewChat(player, vil);
	    Chat chat = playerChats.get(id);
	    if (chat != null && !chat.isEnded())
		chat.chat();
	}

    }

    private static void createNewChat(Player player, Villager vil) {
	Chat c = new Chat(player, vil);
	if (!c.isEnded())
	    playerChats.put(player.getName() + "+" + vil.getName(), c);
    }
}
