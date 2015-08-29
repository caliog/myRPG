package org.caliog.myRPG.Spells;

import org.caliog.myRPG.Manager;
import org.caliog.myRPG.Entities.myClass;

public class SpeedSpell extends Spell {
    public SpeedSpell(myClass player) {
	super(player, "Speed");
    }

    public boolean execute() {
	if (!super.execute()) {
	    return false;
	}
	float p = getPower() / 1000.0F;
	if (p > 5.0F) {
	    p = 5.0F;
	}
	getPlayer().getPlayer().setWalkSpeed(p);

	Manager.scheduleTask(new Runnable() {
	    public void run() {
		SpeedSpell.this.getPlayer().getPlayer().setWalkSpeed(0.2F);
	    }
	}, 600L);
	activate(600L);
	return false;
    }

    public int getMinLevel() {
	return 1;
    }

    public int getFood() {
	return (int) (10.0F * (getPower() / 500.0F)) - 1;
    }

    public int getPower() {
	int p = Math.round(getPlayer().getLevel() / 40.0F * 200.0F);
	return p + 300;
    }

    public int getDamage() {
	return 0;
    }

    public int getDefense() {
	return 0;
    }
}
