package org.caliog.myRPG.Spells;

import org.caliog.myRPG.BarAPI.BarAPI;
import org.caliog.myRPG.Entities.myClass;
import org.caliog.myRPG.Messages.Msg;

public abstract class Spell {
    private final myClass player;
    private boolean active = false;
    private String name;

    public Spell(myClass player, String name) {
	this.player = player;
	this.name = name;
    }

    public abstract int getMinLevel();

    public abstract int getFood();

    public abstract int getPower();

    public abstract int getDamage();

    public abstract int getDefense();

    public boolean isActive() {
	return this.active;
    }

    public boolean activate(long time) {
	if (this.active) {
	    return false;
	}
	this.active = true;
	if (!this.player.isBossFight()) {
	    BarAPI.timerBar(this.player.getPlayer(), this.name, (int) Math.round(time / 20L));
	}
	return true;
    }

    public boolean execute() {
	if (this.player.getLevel() < getMinLevel()) {
	    Msg.sendMessage(this.player.getPlayer(), "need-level-skill");
	    return false;
	}
	if (this.player.getPlayer().getFoodLevel() - getFood() >= 0) {
	    this.player.getPlayer().setFoodLevel(this.player.getPlayer().getFoodLevel() - getFood());
	} else {
	    Msg.sendMessage(this.player.getPlayer(), "need-mana-skill");
	    return false;
	}
	if (isActive()) {
	    Msg.sendMessage(this.player.getPlayer(), "skill-active");
	    return false;
	}
	return true;
    }

    public myClass getPlayer() {
	return this.player;
    }
}