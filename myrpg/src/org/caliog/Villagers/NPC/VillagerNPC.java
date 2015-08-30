package org.caliog.Villagers.NPC;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.caliog.Villagers.NPC.Guards.CPMoveable;
import org.caliog.Villagers.nms.NMS;
import org.caliog.myRPG.Manager;

public class VillagerNPC extends CPMoveable {

    private float interactionRadius = 4F;
    private String name;
    protected Profession profession;

    public VillagerNPC(org.bukkit.entity.Villager entity, Location loc, String name) {
	super(loc);
	this.setEntity(entity);
	this.name = name;
	Manager.scheduleTask(new Runnable() {

	    @Override
	    public void run() {
		init();
	    }
	});

    }

    public String getName() {
	return name;
    }

    public UUID getUniqueId() {
	return getVillager().getUniqueId();
    }

    public float getInteractionRadius() {
	return interactionRadius;
    }

    public void setInteractionRadius(float interactionRadius) {
	this.interactionRadius = interactionRadius;
    }

    public org.bukkit.entity.Villager getVillager() {
	return (Villager) getBukkitEntity();
    }

    public void toggleProfession() {
	Profession profession = getProfession();
	switch (profession) {
	case BLACKSMITH:
	    profession = Profession.BUTCHER;
	    break;
	case BUTCHER:
	    profession = Profession.FARMER;
	    break;
	case FARMER:
	    profession = Profession.LIBRARIAN;
	    break;
	case LIBRARIAN:
	    profession = Profession.PRIEST;
	    break;
	case PRIEST:
	    profession = Profession.BLACKSMITH;
	    break;
	}

	setProfession(profession);
    }

    public Profession getProfession() {
	return getVillager().getProfession();
    }

    public void setProfession(Profession prof) {
	getVillager().setProfession(prof);

    }

    protected void init() {
	NMS.getUtil().initVillager(this);
    }

    public void despawn() {
	getVillager().remove();
    }

}
