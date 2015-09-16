package org.caliog.myRPG.Mobs;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.BarAPI.BarAPI;
import org.caliog.myRPG.Entities.Fighter;
import org.caliog.myRPG.Entities.PlayerManager;
import org.caliog.myRPG.Entities.VolatileEntities;
import org.caliog.myRPG.Utils.ParticleEffect;
import org.caliog.myRPG.Utils.Utils;
import org.caliog.myRPG.Utils.Vector;

public abstract class Mob extends Fighter {
    private final String name;
    private final UUID id;
    protected HashMap<ItemStack, Float> drops = new HashMap<ItemStack, Float>();
    protected HashMap<String, ItemStack> eq = new HashMap<String, ItemStack>();
    private final Vector spawnZone;
    private final Set<UUID> attackers = new HashSet<UUID>();
    private int taskId = -1;
    private HashMap<UUID, Integer> map = new HashMap<UUID, Integer>();
    private boolean dead = false;

    public Mob(String name, UUID id, Vector m) {
	this.name = name;
	this.id = id;
	this.spawnZone = m;
    }

    public static LivingEntity spawnEntity(String name, final Location loc, Vector m) {
	Entity entity = null;
	Mob mob = null;

	EntityType type = new MobInstance(name, null, null).getType();
	entity = loc.getWorld().spawnEntity(loc, type);
	mob = new MobInstance(name, entity.getUniqueId(), m);

	Manager.scheduleRepeatingTask(new Runnable() {
	    public void run() {
		ParticleEffect.SMOKE_NORMAL.display(0.1F, 0.3F, 0.1F, 0.25F, 10, loc, 30);

	    }
	}, 0L, 2L, 8L);
	if ((entity instanceof LivingEntity)) {
	    LivingEntity e = (LivingEntity) entity;
	    e.setCustomName(mob.getCustomName());
	    e.setCustomNameVisible((mob.getCustomName() != null) && (!mob.getCustomName().isEmpty()));
	    e.setCanPickupItems(false);
	    e.setMaxHealth(mob.getHP());
	    e.setHealth(mob.getHP());
	    if ((mob.eq() != null) && (!mob.eq().isEmpty())) {
		e.getEquipment().setItemInHand((ItemStack) mob.eq().get("HAND"));
		e.getEquipment().setItemInHandDropChance(0.0F);
		e.getEquipment().setHelmet((ItemStack) mob.eq().get("HELMET"));
		e.getEquipment().setHelmetDropChance(0.0F);
		e.getEquipment().setChestplate((ItemStack) mob.eq().get("CHESTPLATE"));
		e.getEquipment().setChestplateDropChance(0.0F);
		e.getEquipment().setLeggings((ItemStack) mob.eq().get("LEGGINGS"));
		e.getEquipment().setLeggingsDropChance(0.0F);
		e.getEquipment().setBoots((ItemStack) mob.eq().get("BOOTS"));
		e.getEquipment().setBootsDropChance(0.0F);
	    }
	    VolatileEntities.register(mob);

	    return e;
	}
	return null;
    }

    public abstract HashMap<String, ItemStack> eq();

    public abstract double getHP();

    public String getCustomName() {
	return (fightsBack() ? ChatColor.RED : ChatColor.BLUE) + "" + (isAgressive() ? ChatColor.ITALIC : "")
		+ getName() + " Lv " + getLevel();
    }

    public abstract EntityType getType();

    public String getName() {
	return this.name;
    }

    public abstract int getLevel();

    public boolean fightsBack() {
	return getDamage(false) > 0;
    }

    public abstract boolean isAgressive();

    public abstract int getExp();

    public abstract HashMap<ItemStack, Float> drops();

    public UUID getId() {
	return this.id;
    }

    public Vector getSpawnZone() {
	return this.spawnZone;
    }

    public abstract int getExtraTime();

    public abstract boolean isBoss();

    public void addAttacker(final UUID uniqueId) {
	attackers.add(uniqueId);
	start();
	if (map.containsKey(uniqueId)) {
	    Manager.cancelTask((Integer) map.get(uniqueId));
	}
	map.put(uniqueId, Integer.valueOf(Manager.scheduleTask(new Runnable() {
	    public void run() {
		PlayerManager.getPlayer(uniqueId).setBossId(null);
		removeAttacker(uniqueId);
	    }
	}, 1200L)));
    }

    public void removeAttacker(UUID uniqueId) {
	attackers.remove(uniqueId);
	BarAPI.removeBar(Utils.getPlayer(uniqueId));
    }

    public void cancel() {
	Manager.cancelTask(Integer.valueOf(taskId));
	taskId = -1;
    }

    private void start() {
	if (taskId >= 0) {
	    return;
	}
	taskId = Manager.scheduleRepeatingTask(new Runnable() {
	    public void run() {
		Set<UUID> a = new HashSet<UUID>();
		if (attackers.isEmpty()) {
		    cancel();
		}
		a.addAll(attackers);
		if (getHealth() > 0.0D) {
		    float p = (float) (getHealth() / getHP());
		    for (UUID id : a) {
			BarAPI.updateBar(Utils.getPlayer(id), getName(), p);
		    }
		}
	    }
	}, 20L, 10L);
    }

    public void clearAttackers() {
	for (UUID id : attackers) {
	    BarAPI.removeBar(Utils.getPlayer(id));
	}
	attackers.clear();
    }

    public Collection<? extends UUID> getAttackers() {
	return this.attackers;
    }

    public void die() {
	dead = true;
    }

    public boolean isDead() {
	return dead;
    }
}
