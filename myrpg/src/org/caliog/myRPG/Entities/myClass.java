package org.caliog.myRPG.Entities;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.Items.CustomItem;
import org.caliog.myRPG.Items.ItemEffect;
import org.caliog.myRPG.Items.ItemUtils;
import org.caliog.myRPG.Items.ItemEffect.ItemEffectType;
import org.caliog.myRPG.Spells.InvisibleSpell;
import org.caliog.myRPG.Spells.Spell;
import org.caliog.myRPG.Utils.FilePath;

public class myClass extends myPlayer {

    private int strength;
    private int intelligence;
    private int dexterity;
    private int vitality;
    protected int[] spell = { -1, -1, -1 };
    private HashMap<String, Spell> spells = new HashMap<String, Spell>();
    private final String type;
    private int spellTask = -1;
    private String[] spellItemName;
    private UUID boss = null;
    private boolean loaded = false;

    public myClass(Player player, String type) {
	super(player);
	this.type = type;
	setLoaded(load());
	setHealth(getMaxHealth());
    }

    public double getMaxHealth() {
	double h = super.getMaximumHealth();
	h += 20.0F + getRVitality() / 100.0F * 20.0F;
	return h;
    }

    public void resetHealth() {
	setHealth(getMaxHealth());
	getPlayer().setHealth(20.0D);
    }

    public void addHealth(double d) {
	if (getHealth() + d > getMaxHealth()) {
	    setHealth(getMaxHealth());
	} else {
	    setHealth(d + getHealth());
	}
    }

    public int getDefense() {
	int defense = super.getDefense();
	double p = 1.0D + (getRStrength() + getRDexterity()) / 200.0D;
	for (Spell s : this.spells.values()) {
	    if (s.isActive()) {
		defense += s.getDefense();
	    }
	}
	return (int) Math.round(p * defense);
    }

    public int getDamage(boolean b) {
	int damage = super.getDamage(b);
	double p = 1.0D + getRStrength() / 100.0D;
	if ((getCritical() > 0) && ((getRIntelligence() / 20.0F + getCritical()) / 100.0F > Math.random())) {
	    p = 2.0D;
	}
	for (Spell s : this.spells.values()) {
	    if (s.isActive()) {
		damage += s.getDamage();
	    }
	}
	return (int) Math.round(p * damage);
    }

    public int getRStrength() {
	int a = 0;
	for (CustomItem item : this.getEquipment()) {
	    for (ItemEffect effect : item.getEffects()) {
		if (effect.getType().equals(ItemEffectType.STR))
		    a += effect.getPower();
	    }
	}
	return getStrength() + a;
    }

    public int getStrength() {
	return this.strength;
    }

    public boolean skillStrength(int a) {
	if (getStrength() + a <= 100) {
	    setStrength(getStrength() + a);
	    return true;
	}
	return false;
    }

    public void setStrength(int strength) {
	if (strength <= 100) {
	    this.strength = strength;
	}
    }

    public int getRIntelligence() {
	int a = 0;
	for (CustomItem item : this.getEquipment()) {
	    for (ItemEffect effect : item.getEffects()) {
		if (effect.getType().equals(ItemEffectType.INT))
		    a += effect.getPower();
	    }
	}
	return getIntelligence() + a;
    }

    public int getIntelligence() {
	return this.intelligence;
    }

    public boolean skillIntelligence(int a) {
	if (getIntelligence() + a <= 100) {
	    setIntelligence(getIntelligence() + a);
	    return true;
	}
	return false;
    }

    public void setIntelligence(int intelligence) {
	if (intelligence <= 100) {
	    this.intelligence = intelligence;
	}
    }

    public int getRDexterity() {
	int a = 0;
	for (CustomItem item : this.getEquipment()) {
	    for (ItemEffect effect : item.getEffects()) {
		if (effect.getType().equals(ItemEffectType.DEX))
		    a += effect.getPower();
	    }
	}
	return getDexterity() + a;
    }

    public int getDexterity() {
	return this.dexterity;
    }

    public boolean skillDexterity(int a) {
	if (getDexterity() + a <= 100) {
	    setDexterity(getDexterity() + a);
	    return true;
	}
	return false;
    }

    public void setDexterity(int dexterity) {
	if (dexterity <= 100) {
	    this.dexterity = dexterity;
	}
    }

    public int getRVitality() {
	int a = 0;
	for (CustomItem item : this.getEquipment()) {
	    for (ItemEffect effect : item.getEffects()) {
		if (effect.getType().equals(ItemEffectType.VIT))
		    a += effect.getPower();
	    }
	}
	return getVitality() + a;
    }

    public int getVitality() {
	return this.vitality;
    }

    public boolean skillVitality(int a) {
	if (getVitality() + a <= 100) {
	    setVitality(getVitality() + a);
	    return true;
	}
	return false;
    }

    public void setVitality(int vitality) {
	if (vitality <= 100) {
	    this.vitality = vitality;
	}
    }

    public String getType() {
	return this.type;
    }

    public int getLevel() {
	return getPlayer().getLevel();
    }

    private int getCritical() {
	int a = 0;
	for (CustomItem item : this.getEquipment()) {
	    for (ItemEffect effect : item.getEffects()) {
		if (effect.getType().equals(ItemEffectType.CRIT))
		    a += effect.getPower();
	    }
	}
	return a;
    }

    public int getDodge() {
	int a = 0;
	for (CustomItem item : this.getEquipment()) {
	    for (ItemEffect effect : item.getEffects()) {
		if (effect.getType().equals(ItemEffectType.DODG))
		    a += effect.getPower();
	    }
	}
	return a;
    }

    public void register(Action action) {
	int s = -1;
	switch (action) {
	case PHYSICAL:
	    s = 1;
	    break;
	case LEFT_CLICK_AIR:
	    s = 1;
	    break;
	case RIGHT_CLICK_BLOCK:
	    return;
	case RIGHT_CLICK_AIR:
	    s = 0;
	    break;
	case LEFT_CLICK_BLOCK:
	    return;
	default:
	    return;
	}
	if ((this.spell[0] == -1)
		&& (this.getPlayer().getItemInHand().getType().equals(Material.BOW) ? s == 0 : s == 1)
		&& (!getPlayer().isSneaking())) {
	    return;
	}
	for (int i = 0; i < this.spell.length; i++) {
	    if (this.spell[i] == -1) {
		this.spell[i] = s;
		if (i == 0) {
		    this.spellItemName = Playerface.spell(getPlayer(), this.spell);
		} else {
		    this.spellItemName[1] = Playerface.spell(getPlayer(), this.spell)[1];
		}
		if (i == this.spell.length - 1) {
		    castSpell();
		}
		if (this.spellTask != -1) {
		    Manager.cancelTask(Integer.valueOf(this.spellTask));
		}
		this.spellTask = Manager.scheduleTask(new Runnable() {
		    public void run() {
			for (int i = 0; i < myClass.this.spell.length; i++) {
			    myClass.this.spell[i] = -1;
			}
			if ((myClass.this.getPlayer().getItemInHand().hasItemMeta())
				&& (myClass.this.getPlayer().getItemInHand().getItemMeta().hasDisplayName())
				&& (myClass.this.getPlayer().getItemInHand().getItemMeta().getDisplayName()
					.equals(myClass.this.spellItemName[1]))) {
			    ItemUtils.setDisplayName(myClass.this.getPlayer().getItemInHand(),
				    myClass.this.spellItemName[0]);
			    myClass.this.spellItemName = null;
			}
		    }
		}, 40L);

		return;
	    }
	}
    }

    protected void castSpell() {

    }

    public String[] getSpellItemName() {
	return this.spellItemName;
    }

    public void regainFood() {
	if (getPlayer().getFoodLevel() < 20) {
	    getPlayer().setFoodLevel(getPlayer().getFoodLevel() + 1);
	}
    }

    public void setLevel(int level) {
	getPlayer().setLevel(level);
    }

    public void save() throws IOException {
	File folder = new File(FilePath.players + this.type);
	folder.mkdir();
	File file = new File(folder.getAbsolutePath() + "/" + getPlayer().getName() + ".yml");
	YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
	config.set("level", Integer.valueOf(getLevel()));
	config.set("exp", Float.valueOf(getPlayer().getExp()));
	config.set("int", Integer.valueOf(getIntelligence()));
	config.set("vit", Integer.valueOf(getVitality()));
	config.set("str", Integer.valueOf(getStrength()));
	config.set("dex", Integer.valueOf(getDexterity()));
	config.save(file);
    }

    public boolean load() {
	File folder = new File(FilePath.players + this.type);
	folder.mkdir();
	File file = new File(folder.getAbsolutePath() + "/" + getPlayer().getName() + ".yml");
	if (file.exists()) {
	    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
	    int l = config.getInt("level");
	    if (l != 1) {
		setLevel(0);
	    } else {
		setLevel(2);
	    }
	    setLevel(l == 0 ? 1 : l);
	    getPlayer().setExp((float) config.getDouble("exp"));
	    setIntelligence(config.getInt("int"));
	    setVitality(config.getInt("vit"));
	    setStrength(config.getInt("str"));
	    setDexterity(config.getInt("dex"));

	    return true;
	} else
	    return false;
    }

    public boolean isBossFight() {
	return this.boss != null;
    }

    public UUID getBossId() {
	return this.boss;
    }

    public void setBossId(UUID uuid) {
	this.boss = uuid;
    }

    public boolean isInvisible() {
	for (Spell spell : this.spells.values()) {
	    if (((spell instanceof InvisibleSpell)) && (spell.isActive())) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void reset() {
	super.reset();
	this.dexterity = 0;
	this.intelligence = 0;
	this.vitality = 0;
	this.strength = 0;

    }

    public boolean isLoaded() {
	return loaded;
    }

    public void setLoaded(boolean loaded) {
	this.loaded = loaded;
    }

    public void addSpell(String id, Spell spell) {
	spells.put(id, spell);
    }

}
