package org.caliog.myRPG.Spells;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.Entities.myClass;

public class InvisibleSpell extends Spell {
    public InvisibleSpell(myClass player) {
	super(player, "Tarnung");
    }

    public boolean execute() {
	if (!super.execute()) {
	    return false;
	}
	if (getPlayer().isFighting()) {
	    return false;
	}
	activate(getPower() * 20);
	Manager.scheduleRepeatingTask(new Runnable() {
	    public void run() {
		InvisibleSpell.this.getPlayer().getPlayer()
			.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1200, 2));
	    }
	}, 0L, 200L, getPower() * 20L);
	Manager.scheduleTask(new Runnable() {
	    public void run() {
		InvisibleSpell.this.getPlayer().getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
	    }
	}, getPower() * 20L);

	return true;
    }

    public int getMinLevel() {
	return 5;
    }

    public int getFood() {
	return Math.round(getPower() / 90.0F * 12.0F);
    }

    public int getPower() {
	int level = getPlayer().getLevel();
	if (level <= 10) {
	    return level * 5;
	}
	if (level <= 20) {
	    return level * 2 + 10;
	}
	return 90;
    }

    public int getDamage() {
	return 0;
    }

    public int getDefense() {
	return 0;
    }
}
