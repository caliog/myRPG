package org.caliog.Villagers.Chat;

import java.util.HashMap;

import org.caliog.Villagers.Chat.CMessage.MessageType;
import org.caliog.Villagers.NPC.Villager;
import org.caliog.Villagers.Quests.QManager;
import org.caliog.Villagers.Quests.Quest;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.Entities.PlayerManager;
import org.caliog.myRPG.Entities.myClass;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chat {

    private final myClass player;
    private final Villager villager;

    private int current = 0;
    private final HashMap<Integer, CMessage> messages;
    private final Quest q;
    private boolean ended = false;
    private int taskId;

    public Chat(Player p, Villager v) {
	player = PlayerManager.getPlayer(p.getUniqueId());
	this.villager = v;
	this.q = QManager.searchFittingQuest(player, villager);
	if (q != null) {
	    messages = q.getMessages();

	    current = q.getMessageStart(player) - 1;

	} else
	    messages = villager.getMessages();
	if (messages.isEmpty())
	    ended = true;
    }

    public void chat() {
	current++;
	if (getCurrent() == null) {
	    ended = true;
	    return;
	}
	String name = villager.getName();
	if (name == null || name.equals("null")) {
	    name = "Villager";
	}

	player.getPlayer().sendMessage(
		ChatColor.GRAY + name + ChatColor.WHITE + ": " + ChatColor.BOLD + "" + ChatColor.GOLD + '"'
			+ getCurrent().getMessage() + '"');

	Manager.scheduleTask(new Runnable() {

	    @Override
	    public void run() {
		getCurrent().execute(player, villager);

	    }
	});

	if (getCurrent().getType().equals(MessageType.END))
	    ended = true;
	else if (getCurrent().getType().equals(MessageType.TEXT)) {
	    Manager.scheduleTask(new Runnable() {

		@Override
		public void run() {
		    chat();

		}
	    }, getCurrent().getTime());

	}

	if (!ended) {
	    Manager.cancelTask(taskId);
	    taskId = Manager.scheduleTask(new Runnable() {

		@Override
		public void run() {
		    ended = true;
		}
	    }, 20L * 10);
	}

    }

    public void answer(boolean t) {
	if (!t && getCurrent().getType().equals(MessageType.QUESTION))
	    current = getCurrent().getTarget() - 1;
	chat();

    }

    public boolean isListening() {
	return getCurrent().getType().equals(MessageType.QUESTION);
    }

    public CMessage getCurrent() {
	return messages.get(current);
    }

    public boolean isEnded() {
	return ended;
    }

}
