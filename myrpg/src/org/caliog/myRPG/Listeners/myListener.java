package org.caliog.myRPG.Listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.caliog.myRPG.Manager;
import org.caliog.myRPG.myConfig;
import org.caliog.myRPG.Entities.Fighter;
import org.caliog.myRPG.Entities.PlayerManager;
import org.caliog.myRPG.Entities.Playerface;
import org.caliog.myRPG.Entities.VolatileEntities;
import org.caliog.myRPG.Entities.myClass;
import org.caliog.myRPG.Group.GroupManager;
import org.caliog.myRPG.Items.ItemUtils;
import org.caliog.myRPG.Items.Weapon;
import org.caliog.myRPG.Items.Custom.Apple_1;
import org.caliog.myRPG.Items.Custom.Apple_2;
import org.caliog.myRPG.Items.Custom.Apple_3;
import org.caliog.myRPG.Items.Custom.Apple_4;
import org.caliog.myRPG.Items.Custom.Apple_5;
import org.caliog.myRPG.Items.Custom.Skillstar;
import org.caliog.myRPG.Messages.Msg;
import org.caliog.myRPG.Mobs.Mob;
import org.caliog.myRPG.Mobs.MobSpawnZone;
import org.caliog.myRPG.Mobs.MobSpawner;
import org.caliog.myRPG.Utils.EntityUtils;
import org.caliog.myRPG.Utils.ParticleEffect;
import org.caliog.myRPG.Utils.SkillInventoryView;

public class myListener implements Listener {
    private HashMap<Integer, Integer> entityTasks = new HashMap<Integer, Integer>();
    private HashMap<UUID, UUID> damaged = new HashMap<UUID, UUID>();
    private HashMap<UUID, Short> bows = new HashMap<UUID, Short>();

    @EventHandler(priority = EventPriority.HIGH)
    public void creatureSpawnEvent(CreatureSpawnEvent event) {
	if (!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) {
	    event.setCancelled(true);
	}
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
	if (((event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
		|| (event.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) || (event.getCause()
		.equals(EntityDamageEvent.DamageCause.FIRE_TICK))) && ((event.getEntity() instanceof Player))) {
	    if (PlayerManager.getPlayer(event.getEntity().getUniqueId()).damage(event.getDamage())) {
		playerDeathEvent(new PlayerDeathEvent((Player) event.getEntity(), new ArrayList<ItemStack>(), 0, ""));
		respawn((Player) event.getEntity());
	    }
	    event.setDamage(0.0D);
	}
	if ((VolatileEntities.getMob(event.getEntity().getUniqueId()) != null)
		&& ((event.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) || (event.getCause()
			.equals(EntityDamageEvent.DamageCause.FIRE_TICK)))) {
	    event.setCancelled(true);
	    event.getEntity().setFireTicks(0);
	}
	myClass player = PlayerManager.getPlayer(event.getEntity().getUniqueId());
	if ((player != null) && (!(event instanceof EntityDamageByEntityEvent))) {
	    player.damage(event.getDamage());
	    event.setDamage(0.0D);
	}
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamageByPlayer(EntityDamageByEntityEvent event) {
	if (((event.getDamager() instanceof Player))
		&& (PlayerManager.getPlayer(event.getDamager().getUniqueId()) != null)) {
	    if (!ItemUtils.checkForUse((Player) event.getDamager(), ((Player) event.getDamager()).getItemInHand())) {
		event.setCancelled(true);
	    } else {
		final Player p = (Player) event.getDamager();
		final short d = p.getItemInHand().getDurability();
		if (!p.getItemInHand().getType().equals(Material.BOW)) {
		    Manager.scheduleTask(new Runnable() {
			public void run() {
			    p.getItemInHand().setDurability(d);
			}
		    });
		}
	    }
	}
	onEntityDamageByEntity(new EntityDamageByEntityEvent(event.getDamager(), event.getEntity(), event.getCause(),
		event.getDamage()));
	onMobDamageByPlayer(new EntityDamageByEntityEvent(event.getDamager(), event.getEntity(), event.getCause(),
		event.getDamage()));
	event.setDamage(0.0D);
    }

    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
	if (event.isCancelled()) {
	    return;
	}
	if (!(event.getEntity() instanceof LivingEntity)) {
	    return;
	}
	boolean shooter = false;
	if ((event.getDamager() != null) && ((event.getDamager() instanceof Projectile))
		&& (((Projectile) event.getDamager()).getShooter() != null)
		&& ((((Projectile) event.getDamager()).getShooter() instanceof LivingEntity))) {
	    shooter = true;
	}
	if ((!(event.getDamager() instanceof LivingEntity)) && (!shooter)) {
	    return;
	}
	LivingEntity mdamager;
	if (shooter) {
	    mdamager = (LivingEntity) ((Projectile) event.getDamager()).getShooter();
	} else {
	    mdamager = (LivingEntity) event.getDamager();
	}
	if (((mdamager instanceof Player)) && ((event.getEntity() instanceof Player))) {
	    return;
	}
	Fighter damager = PlayerManager.getPlayer(mdamager.getUniqueId());
	if (damager == null) {
	    damager = VolatileEntities.getMob(mdamager.getUniqueId());
	}
	double damage = event.getDamage();
	if (damager != null) {
	    if ((this.damaged.containsKey(event.getEntity().getUniqueId()))
		    && (((UUID) this.damaged.get(event.getEntity().getUniqueId())).equals(mdamager.getUniqueId()))) {
		event.setCancelled(true);
		return;
	    }
	    this.damaged.put(event.getEntity().getUniqueId(), mdamager.getUniqueId());

	    Manager.scheduleTask(new Runnable() {
		public void run() {
		    myListener.this.damaged.remove(event.getEntity().getUniqueId());
		}
	    }, 2L);
	    damager.fight();

	    damage = damager.getDamage(!event.getCause().equals(EntityDamageEvent.DamageCause.CUSTOM));
	}
	Mob mob;
	if ((mob = VolatileEntities.getMob(event.getEntity().getUniqueId())) != null) {
	    damage -= mob.getDefense();
	    mob.fight();
	} else {
	    myClass entity;
	    if ((entity = PlayerManager.getPlayer(event.getEntity().getUniqueId())) != null) {
		damage -= entity.getDefense();
		entity.fight();
		if (entity.getDodge() / 100 > Math.random()) {
		    event.setCancelled(true);
		}
	    }
	}
	if (((event.getEntity() instanceof Player))
		&& (PlayerManager.getPlayer(event.getEntity().getUniqueId()) != null)
		&& (PlayerManager.getPlayer(event.getEntity().getUniqueId()).damage(damage))) {
	    PlayerManager.getPlayer(event.getEntity().getUniqueId()).setKiller(mdamager.getUniqueId());
	    playerDeathEvent(new PlayerDeathEvent((Player) event.getEntity(), new ArrayList<ItemStack>(), 0,
		    ChatColor.GOLD + "You were killed!"));
	    respawn((Player) event.getEntity());
	    damage = 0.0D;
	    event.getEntity().setFireTicks(0);
	}
	event.setDamage(damage);
    }

    public void onMobDamageByPlayer(EntityDamageByEntityEvent event) {
	if (event.isCancelled()) {
	    return;
	}
	if (!(event.getEntity() instanceof Damageable)) {
	    return;
	}
	if (!(event.getEntity() instanceof LivingEntity)) {
	    return;
	}
	boolean shooterisplayer = false;
	if ((event.getDamager() != null) && ((event.getDamager() instanceof Projectile))
		&& (((Projectile) event.getDamager()).getShooter() != null)
		&& (((Projectile) event.getDamager()).getShooter() instanceof Player)) {//TODO DANGER; because of ProjectileSource is not LivingEntity
	    shooterisplayer = true;
	}
	if ((!(event.getDamager() instanceof Player)) && (!shooterisplayer)) {
	    return;
	}
	Player player = null;
	if (shooterisplayer) {
	    player = (Player) ((Projectile) event.getDamager()).getShooter();
	} else {
	    player = (Player) event.getDamager();
	}
	if (this.entityTasks.containsKey(Integer.valueOf(event.getEntity().getEntityId()))) {
	    Manager.cancelTask((Integer) this.entityTasks.get(Integer.valueOf(event.getEntity().getEntityId())));
	}
	final LivingEntity e = (LivingEntity) event.getEntity();
	final Mob mob = VolatileEntities.getMob(e.getUniqueId());
	if (mob == null) {
	    return;
	}
	if (mob.isBoss()) {
	    if (PlayerManager.getPlayer(player.getUniqueId()).isBossFight()) {
		VolatileEntities.getMob(PlayerManager.getPlayer(player.getUniqueId()).getBossId()).removeAttacker(
			player.getUniqueId());
	    }
	    PlayerManager.getPlayer(player.getUniqueId()).setBossId(mob.getId());
	    mob.addAttacker(player.getUniqueId());
	}
	double damage = event.getDamage();
	if (damage < 0.0D) {
	    damage = 0.0D;
	}
	e.setCustomName(EntityUtils.getBar(mob.getHealth() - damage, mob.getHP()));
	this.entityTasks.put(Integer.valueOf(event.getEntity().getEntityId()),
		Integer.valueOf(Manager.scheduleTask(new Runnable() {
		    public void run() {
			e.setCustomName(mob.getCustomName());
		    }
		}, 100L)));
	if (mob.damage(damage)) {
	    mob.setKiller(player.getUniqueId());
	    e.setHealth(0.0D);
	    EntityDeathEvent ed = new EntityDeathEvent(e, new ArrayList<ItemStack>());
	    Bukkit.getPluginManager().callEvent(ed);
	}
	event.setDamage(0.0D);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMobDeath(final EntityDeathEvent event) {
	event.setDroppedExp(0);
	event.getDrops().clear();
	if ((!(event.getEntity() instanceof Creature)) && (!(event.getEntity() instanceof Slime))
		&& (!(event.getEntity() instanceof Ghast))) {
	    return;
	}
	final Mob mob = VolatileEntities.getMob(event.getEntity().getUniqueId());
	if (mob == null) {
	    return;
	}
	mob.die();
	Manager.scheduleTask(new Runnable() {
	    public void run() {
		VolatileEntities.remove(event.getEntity().getUniqueId());
		for (MobSpawnZone z : MobSpawner.zones) {
		    if (z.getM().equals(mob.getSpawnZone())) {
			z.askForSpawn(mob.getExtraTime());
		    }
		}
	    }
	}, 25L);

	//Player related
	final myClass player = PlayerManager.getPlayer(mob.getKillerId());
	if (player == null) {
	    return;
	}
	mob.setKiller(null);

	//Player related
	double diff = player.getLevel() - mob.getLevel();
	if (diff < 3.0D) {
	    diff = 1.0D;
	} else if (diff < 6.0D) {
	    diff = 1.5D;
	} else if (diff < 10.0D) {
	    diff = 2.0D;
	} else if (diff < 25.0D) {
	    diff = 4.0D;
	} else if (diff < 50.0D) {
	    diff = 10.0D;
	} else if (diff < 100.0D) {
	    diff = 100.0D;
	}
	event.getEntity().setCustomName(
		ChatColor.BLACK + "[  " + ChatColor.YELLOW + "+ " + Playerface.killed(player.getPlayer(), mob)
			+ " XP  " + ChatColor.BLACK + "]");
	for (ItemStack stack : mob.drops().keySet()) {
	    if (Math.random() * diff < ((Float) mob.drops().get(stack)).floatValue()) {
		Playerface.dropItem(player.getPlayer(), event.getEntity().getLocation(), stack);
		player.getPlayer().playSound(event.getEntity().getLocation(), Sound.LEVEL_UP, 0.6F, 2.0F);
		break;
	    }
	}
	if ((player.getLevel() - mob.getLevel() < 4) && (Weapon.isWeapon(player, player.getPlayer().getItemInHand()))) {
	    final ItemStack hand = player.getPlayer().getItemInHand();
	    Weapon w = Weapon.getInstance(player, hand);
	    int level = w.getLevel();
	    int mLevel = w.getMinLevel();
	    int max = (level + 2) * (mLevel + 2);
	    float p = (this.bows.containsKey(player.getPlayer().getUniqueId()) ? ((Short) this.bows.get(player
		    .getPlayer().getUniqueId())).shortValue() : (float) hand.getDurability())
		    / (float) w.getType().getMaxDurability();
	    int current = Math.round(p == 0.0F ? 0.0F : (1.0F - p) * max);
	    current++;
	    if ((current == max) && (w.getLevel() != 9)) {
		w.raiseLevel(player.getPlayer());
		String[] a = { Msg.WEAPON, Msg.LEVEL }, b = { w.getName(), String.valueOf(w.getLevel()) };
		Msg.sendMessage(player.getPlayer(), "level-weapon", a, b);
	    } else {
		p = 1.0F - (float) current / (float) max;
		final short newD = (short) Math.round(p * w.getType().getMaxDurability());
		Manager.scheduleTask(new Runnable() {
		    public void run() {
			if (myListener.this.bows.containsKey(player.getPlayer().getUniqueId())) {
			    myListener.this.bows.put(player.getPlayer().getUniqueId(), Short.valueOf(newD));
			}
			hand.setDurability(newD);
		    }
		});
	    }
	}
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void playerDeathEvent(PlayerDeathEvent event) {
	Firework firework = (Firework) event.getEntity().getWorld()
		.spawn(event.getEntity().getLocation(), Firework.class);
	FireworkMeta data = firework.getFireworkMeta();
	data.addEffects(new FireworkEffect[] { FireworkEffect.builder().flicker(false).withColor(Color.RED)
		.withFade(Color.FUCHSIA).with(FireworkEffect.Type.CREEPER).build() });
	data.setPower(new Random().nextInt(2) + 1);
	firework.setFireworkMeta(data);

	myClass p = PlayerManager.getPlayer(event.getEntity().getUniqueId());
	if (p.isBossFight()) {
	    UUID id = p.getBossId();
	    if ((id != null) && (VolatileEntities.getMob(id) != null)) {
		VolatileEntities.getMob(id).removeAttacker(p.getPlayer().getUniqueId());
	    }
	    p.setBossId(null);
	}
	float newExp = event.getEntity().getExp() - 0.25F * event.getEntity().getExp();
	if (newExp < 0) {
	    newExp = 0F;
	}
	event.getEntity().setExp(newExp);
	Msg.sendMessage(event.getEntity(), "dead-message");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void levelUp(PlayerLevelChangeEvent event) {
	myClass player = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
	if (player == null) {
	    return;
	}
	if (event.getOldLevel() + 1 == event.getNewLevel()) {
	    Location loc = player.getPlayer().getLocation();
	    Firework firework = (Firework) player.getPlayer().getWorld().spawn(loc, Firework.class);
	    FireworkMeta data = firework.getFireworkMeta();
	    Color c = Color.YELLOW;
	    if (event.getNewLevel() > 20) {
		c = Color.GREEN;
	    }
	    if (event.getNewLevel() > 40) {
		c = Color.BLUE;
	    }
	    if (event.getNewLevel() > 60) {
		c = Color.LIME;
	    }
	    data.addEffects(new FireworkEffect[] { FireworkEffect.builder().withColor(c).with(FireworkEffect.Type.STAR)
		    .build() });
	    data.setPower(0);
	    firework.setFireworkMeta(data);
	    Playerface.giveItem(player.getPlayer(), new Skillstar(3));
	    Msg.sendMessage(event.getPlayer(), "level-reached", Msg.LEVEL, String.valueOf(event.getNewLevel()));
	}
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void inventoryClose(InventoryCloseEvent event) {
	if (!(event.getPlayer() instanceof Player)) {
	    return;
	}
	if ((event.getInventory().getType().equals(InventoryType.PLAYER))
		|| (event.getInventory().getType().equals(InventoryType.CRAFTING))) {
	    ItemStack[] armor = (ItemStack[]) event.getPlayer().getInventory().getArmorContents().clone();
	    for (int i = 0; i < armor.length; i++) {
		if ((armor[i] != null) && (!armor[i].getType().equals(Material.AIR))
			&& (!ItemUtils.checkForUse((Player) event.getPlayer(), armor[i]))) {
		    Playerface.giveItem((Player) event.getPlayer(), armor[i]);
		    armor[i] = null;
		}
	    }
	    event.getPlayer().getInventory().setArmorContents(armor);
	}
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void spellEvent(final PlayerInteractEvent event) {
	boolean useable = ItemUtils.checkForUse(event.getPlayer(), event.getItem());
	final myClass c = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
	if (c == null) {
	    return;
	}
	if (!Weapon.isWeapon(c, event.getItem())) {
	    return;
	}
	ItemMeta meta = event.getPlayer().getItemInHand().getItemMeta();
	String displayName = null;
	if (meta != null) {
	    displayName = meta.getDisplayName();
	}
	if ((useable)
		|| ((displayName != null) && (c.getSpellItemName() != null) && (displayName
			.equals(c.getSpellItemName()[1])))) {
	    Manager.scheduleTask(new Runnable() {
		public void run() {
		    c.register(event.getAction());
		}
	    });
	} else {
	    event.setCancelled(true);
	    return;
	}
    }

    @EventHandler(priority = EventPriority.LOW)
    public void skillstar(PlayerInteractEvent event) {
	ItemStack stack = event.getPlayer().getItemInHand();
	if (((event.getAction().equals(Action.RIGHT_CLICK_AIR)) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
		&& (Skillstar.isSkillstar(stack))) {
	    event.getPlayer()
		    .openInventory(new SkillInventoryView(event.getPlayer(), event.getPlayer().getInventory()));
	}
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void playerJoin(PlayerJoinEvent event) {
	if (!PlayerManager.login(event.getPlayer())) {
	    PlayerManager.register(event.getPlayer(), myConfig.getStandardClass());
	}

	myClass player = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
	if (player == null) {
	    return;
	}
	if (event.getPlayer().getLevel() <= 0) {
	    event.getPlayer().setLevel(1);
	}
	player.getPlayer().setSaturation(2.0F);
	//respawn(player.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void playerQuit(PlayerQuitEvent event) {
	if (GroupManager.isInGroup(event.getPlayer())) {
	    GroupManager.leaveGroup(event.getPlayer());
	}
	PlayerManager.logout(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void entityTarget(EntityTargetEvent event) {
	Mob mob = VolatileEntities.getMob(event.getEntity().getUniqueId());
	if ((mob == null) || (event.getTarget() == null)) {
	    return;
	}
	myClass player = PlayerManager.getPlayer(event.getTarget().getUniqueId());
	if ((player != null) && (player.isInvisible()) && ((!player.isFighting()) || (!mob.isFighting()))) {
	    event.setCancelled(true);
	}
	if ((!mob.isAgressive()) && (!mob.isFighting())) {
	    event.setCancelled(true);
	}
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void pickupItemEvent(PlayerPickupItemEvent event) {
	if (!Playerface.isAccessible(event.getPlayer(), event.getItem())) {
	    event.setCancelled(true);
	} else {
	    event.setCancelled(false);
	}
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void dropItem(PlayerDropItemEvent event) {
	Playerface.dropItem(event.getPlayer(), event.getPlayer().getLocation(), event.getItemDrop());
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.NORMAL)
    public void inventoryClick(final InventoryClickEvent event) {
	if ((event.getView() instanceof SkillInventoryView)) {
	    final myClass player = PlayerManager.getPlayer(event.getView().getPlayer().getUniqueId());
	    if (event.getRawSlot() < 9) {
		if (Skillstar.isSkillstar(event.getCursor())) {
		    if (event.getRawSlot() == 0) {
			if (player.skillStrength(event.getCursor().getAmount())) {
			    event.setCursor(null);
			} else {
			    Msg.sendMessage(player.getPlayer(), "str-full");
			}
		    } else if (event.getRawSlot() == 1) {
			if (player.skillDexterity(event.getCursor().getAmount())) {
			    event.setCursor(null);
			} else {
			    Msg.sendMessage(player.getPlayer(), "dex-full");
			}
		    } else if (event.getRawSlot() == 2) {
			if (player.skillIntelligence(event.getCursor().getAmount())) {
			    event.setCursor(null);
			} else {
			    Msg.sendMessage(player.getPlayer(), "int-full");
			}
		    } else if (event.getRawSlot() == 3) {
			if (player.skillVitality(event.getCursor().getAmount())) {
			    event.setCursor(null);
			} else {
			    Msg.sendMessage(player.getPlayer(), "vit-full");
			}
		    }
		}
		Manager.scheduleTask(new Runnable() {
		    public void run() {
			player.getPlayer().closeInventory();
			player.getPlayer().openInventory(event.getView());
		    }
		});
		event.setCancelled(true);
	    }
	}
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void playerItemHeld(PlayerItemHeldEvent event) {
	myClass clazz = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
	ItemStack stack = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
	if ((stack == null) || (!stack.hasItemMeta()) || (!stack.getItemMeta().hasDisplayName())) {
	    return;
	}
	if ((clazz == null) || (clazz.getSpellItemName() == null)) {
	    return;
	}
	if (stack.getItemMeta().getDisplayName().equals(clazz.getSpellItemName()[1])) {
	    ItemUtils.setDisplayName(stack, clazz.getSpellItemName()[0]);
	}
    }

    public void respawn(Player player) {
	Location l = player.getBedSpawnLocation();
	if (l == null)
	    l = player.getWorld().getSpawnLocation();
	PlayerManager.getPlayer(player.getUniqueId()).resetHealth();
	player.teleport(l);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBowShoot(EntityShootBowEvent event) {
	if ((!(event.getEntity() instanceof Player))
		|| (PlayerManager.getPlayer(event.getEntity().getUniqueId()) == null)) {
	    return;
	}
	final Player player = (Player) event.getEntity();
	ItemStack stack = event.getBow();
	myClass clazz = PlayerManager.getPlayer(player.getUniqueId());
	if (clazz == null) {
	    return;
	}
	if (Weapon.isWeapon(clazz, stack)) {
	    Weapon weapon = Weapon.getInstance(clazz, stack);
	    if (weapon.getType().equals(Material.BOW)) {
		short d = (short) (stack.getDurability() - 1);
		if (d < 0) {
		    d = 0;
		}
		this.bows.put(player.getUniqueId(), Short.valueOf(d));
		Manager.scheduleTask(new Runnable() {
		    public void run() {
			short d = ((Short) myListener.this.bows.get(player.getUniqueId())).shortValue();
			player.getItemInHand().setDurability(d);
			myListener.this.bows.remove(player.getUniqueId());
		    }
		});
	    }
	}
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBowShoot(final PlayerInteractEvent event) {
	myClass player = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
	if (player == null) {
	    return;
	}
	if (player.getHealth() == player.getMaxHealth()) {
	    return;
	}
	String apple1 = new Apple_1(1).getItemMeta().getDisplayName();
	String apple2 = new Apple_2(1).getItemMeta().getDisplayName();
	String apple3 = new Apple_3(1).getItemMeta().getDisplayName();
	String apple4 = new Apple_4(1).getItemMeta().getDisplayName();
	String apple5 = new Apple_5(1).getItemMeta().getDisplayName();
	if ((event.getAction().equals(Action.RIGHT_CLICK_AIR)) || (event.getAction().equals(Action.RIGHT_CLICK_AIR))) {
	    ItemStack stack = event.getItem();
	    if ((stack.hasItemMeta()) && (stack.getItemMeta().hasDisplayName())) {
		String name = stack.getItemMeta().getDisplayName();
		boolean isApple = true;
		if (name.equals(apple1)) {
		    player.addHealth(5.0D);
		} else if (name.equals(apple2)) {
		    player.addHealth(10.0D);
		} else if (name.equals(apple3)) {
		    player.addHealth(20.0D);
		} else if (name.equals(apple4)) {
		    player.addHealth(0.5D * player.getMaxHealth());
		} else if (name.equals(apple5)) {
		    player.addHealth(player.getMaxHealth());
		} else {
		    isApple = false;
		}
		if (isApple) {
		    int amount = stack.getAmount() - 1;
		    if (amount > 0) {
			event.getPlayer().getItemInHand().setAmount(amount);
		    } else {
			Manager.scheduleTask(new Runnable() {
			    public void run() {
				event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
				event.getPlayer().updateInventory();
			    }
			});
		    }
		    ParticleEffect.HEART.display(0.05F, 0.2F, 0.05F, 0.2F, 10, event.getPlayer().getEyeLocation(), 20);

		}
	    }
	}
    }
    /*
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMoveTeleport(final PlayerMoveEvent event) {
    
    if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.DIAMOND_BLOCK)) {
        if (event.getPlayer().isSneaking()) {
    	event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 3));
    	Manager.scheduleTask(new Runnable() {
    	    public void run() {
    		event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
    	    }
    	}, 100L);

    	Manager.scheduleTask(new Runnable() {
    	    public void run() {
    		event.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
    	    }
    	}, 160L);
        }
        } else if ((event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.EMERALD_BLOCK))
        	&& (event.getPlayer().isSneaking())) {
            final Location l = PlayerManager.getPlayer(event.getPlayer().getUniqueId()).getLastLocation();
            if (l != null) {
        	event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 3));
        	Manager.scheduleTask(new Runnable() {
        	    public void run() {
        		event.getPlayer().teleport(l);
        	    }
        	}, 100L);

        	Manager.scheduleTask(new Runnable() {
        	    public void run() {
        		event.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
        	    }
        	}, 160L);
            }
    }
    }*/

}
