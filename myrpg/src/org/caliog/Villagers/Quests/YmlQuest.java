package org.caliog.Villagers.Quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.caliog.Villagers.Chat.CMessage;
import org.caliog.Villagers.Chat.ChatTask;
import org.caliog.Villagers.NPC.Villager;
import org.caliog.Villagers.NPC.Util.VManager;
import org.caliog.myRPG.Entities.myClass;
import org.caliog.myRPG.Items.ItemUtils;
import org.caliog.myRPG.Utils.QuestStatus;

public class YmlQuest extends Quest {

    protected YamlConfiguration config;

    public YmlQuest(String name, YamlConfiguration config) {
	super(name);
	this.config = config;
    }

    @Override
    public Location getTargetLocation(myClass player) {
	if (config.isString("target-villager")) {
	    String name = config.getString("target-villager");
	    Villager v = VManager.getVillager(name);
	    if (v != null) {
		return v.getLocation();
	    }
	}

	return null;
    }

    @Override
    public HashMap<Integer, CMessage> getMessages() {
	HashMap<Integer, CMessage> map = new HashMap<Integer, CMessage>();

	for (String id : config.getConfigurationSection("messages").getKeys(false)) {
	    CMessage msg = CMessage.fromString(config.getConfigurationSection("messages").getString(id));
	    if (msg != null) {
		if (id.equals("0"))//id:0 is reserved for the "accept-quest" message; default start with id:1
		    msg.setTask(new ChatTask(this) {

			@Override
			public void execute(myClass player, Villager villager) {
			    player.raiseQuestStatus(this.quest.getName());
			}
		    });

		map.put(Integer.parseInt(id), msg);
	    }
	}

	return map;
    }

    @Override
    public int getMessageStart(myClass p) {
	if (this.couldComplete(p)) {
	    p.completeQuest(getName());
	    return config.getInt("completed-message:");
	} else if (p.getQuestStatus(this.getName()).isLowerThan(QuestStatus.COMPLETED))
	    return config.getInt("not-completed-message:");
	else {
	    return 1;//default start with id 1
	}
    }

    @Override
    public List<ItemStack> getRewards() {
	List<ItemStack> list = new ArrayList<ItemStack>();
	if (config.isList("rewards")) {
	    for (String l : config.getStringList("rewards"))
		list.add(ItemUtils.getItem(l));
	}
	return list;
    }

    @Override
    public List<ItemStack> getCollects() {
	List<ItemStack> list = new ArrayList<ItemStack>();
	if (config.isList("collects")) {
	    for (String l : config.getStringList("collects"))
		list.add(ItemUtils.getItem(l));
	}
	return list;
    }

    @Override
    public ItemStack getReceive() {
	return null;
    }

    @Override
    public HashMap<String, Integer> getMobs() {
	HashMap<String, Integer> map = new HashMap<String, Integer>();
	if (config.isConfigurationSection("mobs")) {
	    ConfigurationSection sec = config.getConfigurationSection("mobs");
	    for (String id : sec.getKeys(false)) {
		if (sec.isInt(id)) {
		    map.put(id, sec.getInt(id));
		}
	    }
	}
	return map;
    }

    @Override
    public int getExp() {
	return config.getInt("exp-reward");
    }

    @Override
    public String getClazz() {
	return config.getString("class");
    }

    @Override
    public int getMinLevel() {
	return config.getInt("min-level");
    }

    @Override
    public String getDescription() {
	String descr = config.getString("description");
	return ChatColor.translateAlternateColorCodes('&', descr);
    }

    @Override
    public QuestStatus hasToReach() {
	return QuestStatus.FIRST;
    }

    @Override
    public String getChainQuest() {
	return config.getString("required-quest");
    }

}
