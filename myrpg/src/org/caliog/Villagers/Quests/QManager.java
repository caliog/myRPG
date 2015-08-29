package org.caliog.Villagers.Quests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.caliog.Villagers.NPC.Villager;
import org.caliog.myRPG.Entities.Playerface;
import org.caliog.myRPG.Entities.myClass;
import org.caliog.myRPG.Utils.FilePath;

import org.bukkit.inventory.ItemStack;

public class QManager {

    private static List<Quest> quests = new ArrayList<Quest>();

    public static void init() {
	quests.clear();
	File dir = new File(FilePath.quests);
	for (String n : dir.list())
	    if (QuestLoader.isJarQuest(n))
		quests.add(QuestLoader.load(n));
	while (quests.contains(null))
	    quests.remove(null);
    }

    public static Quest getQuest(String id) {
	for (Quest quest : quests)
	    if (quest.getName().equals(id))
		return quest;
	return null;
    }

    public static Quest searchFittingQuest(myClass player, Villager villager) {
	if (villager == null || player == null)
	    return null;

	for (String q : player.getUnCompletedQuests()) {
	    Quest quest = getQuest(q);
	    if (quest != null && quest.getTargetLocation(player) != null)
		if (quest.getTargetLocation(player).distance(villager.getLocation()) < 3.2)
		    return quest;
	}

	for (String id : villager.getQuests()) {
	    if (player.isCompleted(id))
		continue;
	    Quest q = getQuest(id);
	    if (q == null)
		continue;
	    if (q.hasClazz() && !player.getType().equals(q.getClazz()))
		continue;
	    if (q.hasMinLevel() && player.getLevel() < q.getMinLevel())
		continue;
	    if (player.getUnCompletedQuests().contains(q.getName()) && !q.couldComplete(player))
		continue;
	    if (q.isChainQuest() && !player.isCompleted(q.getChainQuest()))
		continue;
	    return q;
	}

	return null;
    }

    public static List<Quest> getQuests() {
	return quests;
    }

    public static void updateQuestBook(myClass player) {
	for (ItemStack stack : player.getPlayer().getInventory()) {
	    if (stack != null)
		if (QuestBook.isQuestBook(stack)) {
		    stack.setItemMeta(new QuestBook(player).cloneBookMeta());
		    return;
		}
	}
	Playerface.giveItem(player.getPlayer(), new QuestBook(player));
    }
}
