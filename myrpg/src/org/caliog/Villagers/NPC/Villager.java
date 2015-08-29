package org.caliog.Villagers.NPC;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.caliog.Villagers.Chat.CMessage;
import org.caliog.Villagers.Quests.QManager;
import org.caliog.Villagers.Utils.DataSaver;
import org.caliog.Villagers.Utils.LocationUtil;

public class Villager extends VillagerNPC {

    private VillagerType type;

    private HashMap<Integer, CMessage> texts = new HashMap<Integer, CMessage>();
    private List<String> quests = new ArrayList<String>();

    public enum VillagerType {
	PRIEST, TRADER, VILLAGER;
    }

    public HashMap<Integer, CMessage> getMessages() {
	return texts;
    }

    public Villager(org.bukkit.entity.Villager entity, VillagerType type, Location location, String name) {
	super(entity, location, name);
	entity.setCustomName(name);
	entity.setCustomNameVisible(true);
	this.type = type;
	this.setInteractionRadius(10F);
    }

    public VillagerType getType() {
	return type;
    }

    public void setType(VillagerType type) {
	this.type = type;
    }

    public FileWriter save(FileWriter writer) throws IOException {
	try {
	    List<String> messages = new ArrayList<String>();
	    for (int i : texts.keySet())
		messages.add(i + ":" + texts.get(i).toString());

	    writer.append(getName() + "&" + LocationUtil.toString(getLocation()) + "&" + type.name() + "&"
		    + String.valueOf(isStrolling()) + "&" + DataSaver.save(messages) + "&" + DataSaver.save(quests)
		    + "&" + getProfession().name() + ((type.equals(VillagerType.TRADER)) ? "&" : "\r"));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return writer;
    }

    public void addText(int i, String text) {
	this.texts.put(i, CMessage.fromString(text));
    }

    public void removeText(int id) {
	texts.remove(id);
    }

    public boolean addQuest(String q) {

	if (QManager.getQuest(q) != null) {
	    quests.add(q);
	    return true;
	}
	return false;

    }

    public void removeQuest(String q) {

	quests.remove(q);

    }

    public List<String> getQuests() {
	return quests;
    }

    public void copy(Villager v) {
	this.texts = v.getMessages();
	this.quests = v.getQuests();
	this.setProfession(v.getProfession());
    }

    public void clearText() {
	texts.clear();
    }

}
