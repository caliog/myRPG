package org.caliog.myRPG.Listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
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
import org.caliog.myRPG.Entities.PlayerManager;
import org.caliog.myRPG.Entities.Playerface;
import org.caliog.myRPG.Entities.myClass;
import org.caliog.myRPG.Group.GManager;
import org.caliog.myRPG.Items.ItemUtils;
import org.caliog.myRPG.Items.Weapon;
import org.caliog.myRPG.Items.Custom.Apple_1;
import org.caliog.myRPG.Items.Custom.Apple_2;
import org.caliog.myRPG.Items.Custom.HealthPotion;
import org.caliog.myRPG.Items.Custom.Skillstar;
import org.caliog.myRPG.Lib.Barkeeper.CenterBar.CenterBar;
import org.caliog.myRPG.Messages.Msg;
import org.caliog.myRPG.Mobs.Pet;
import org.caliog.myRPG.Utils.ChestHelper;
import org.caliog.myRPG.Utils.EntityUtils;
import org.caliog.myRPG.Utils.GroupManager;
import org.caliog.myRPG.Utils.ParticleEffect;
import org.caliog.myRPG.Utils.SkillInventoryView;
import org.caliog.myRPG.Utils.Utils;
import org.caliog.myRPG.Utils.Vector;

public class myListener implements Listener {

	HashMap<UUID, String[]> petMap = new HashMap<UUID, String[]>(); // player,
																	// mob, name

	@EventHandler(priority = EventPriority.HIGH)
	public void creatureSpawnEvent(CreatureSpawnEvent event) {
		if (myConfig.isWorldDisabled(event.getEntity().getWorld()))
			return;

		if (!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) {
			if (myConfig.isNaturalSpawnDisabled(event.getEntity().getWorld().getName()))
				event.setCancelled(true);
		}

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void levelUp(PlayerLevelChangeEvent event) {
		if (myConfig.isWorldDisabled(event.getPlayer().getWorld()))
			return;
		myClass player = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
		if (player == null) {
			return;
		}
		if (!PlayerManager.changedClass.contains(player.getPlayer().getUniqueId())) {
			if (event.getOldLevel() + 1 == event.getNewLevel()) {
				if (myConfig.isFireworkEnabled()) {
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
					data.addEffects(new FireworkEffect[] { FireworkEffect.builder().withColor(c).with(FireworkEffect.Type.STAR).build() });
					data.setPower(0);

					firework.setFireworkMeta(data);
				}

				CenterBar.display(player.getPlayer(), "", ChatColor.GOLD + "Level " + event.getNewLevel());
				Playerface.giveItem(player.getPlayer(), new Skillstar(3));
				Msg.sendMessage(event.getPlayer(), "level-reached", Msg.LEVEL, String.valueOf(event.getNewLevel()));
			}
		} else
			PlayerManager.changedClass.remove(player.getPlayer().getUniqueId());

		GroupManager.updateGroup(player.getPlayer(), event.getNewLevel());

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void inventoryClose(InventoryCloseEvent event) {
		if (myConfig.isWorldDisabled(event.getPlayer().getWorld()))
			return;
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

		// chests
		if (event.getInventory().getHolder() instanceof Chest) {
			Chest chest = (Chest) event.getInventory().getHolder();
			ChestHelper.loot(chest);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void spellEvent(final PlayerInteractEvent event) {
		if (myConfig.isWorldDisabled(event.getPlayer().getWorld()))
			return;
		boolean useable = ItemUtils.checkForUse(event.getPlayer(), event.getItem());
		final myClass c = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
		if (c == null) {
			return;
		}
		if (!Weapon.isWeapon(c, event.getItem())) {
			return;
		}
		if (useable) {
			if (myConfig.spellsEnabled())
				c.register(event.getAction());

		} else {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void skillstar(PlayerInteractEvent event) {
		if (myConfig.isWorldDisabled(event.getPlayer().getWorld()))
			return;
		ItemStack stack = event.getPlayer().getItemInHand();
		if (((event.getAction().equals(Action.RIGHT_CLICK_AIR)) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
				&& (Skillstar.isSkillstar(stack))) {
			event.getPlayer().openInventory(new SkillInventoryView(event.getPlayer(), event.getPlayer().getInventory()));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void playerJoin(PlayerJoinEvent event) {
		if (!PlayerManager.login(event.getPlayer())) {
			PlayerManager.register(event.getPlayer(), myConfig.getDefaultClass());
		}
		if (myConfig.isWorldDisabled(event.getPlayer().getWorld()))
			return;
		myClass player = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
		if (player == null) {
			return;
		}
		if (event.getPlayer().getLevel() <= 0) {
			event.getPlayer().setLevel(1);
		}
		player.getPlayer().setSaturation(2.0F);
		// respawn(player.getPlayer());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void playerQuit(PlayerQuitEvent event) {
		if (GManager.isInGroup(event.getPlayer())) {
			GManager.leaveGroup(event.getPlayer());
		}
		PlayerManager.logout(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void pickupItemEvent(PlayerPickupItemEvent event) {
		if (!Playerface.isAccessible(event.getPlayer(), event.getItem())) {
			event.setCancelled(true);
		} else {
			// potions stack
			ItemStack stack = event.getItem().getItemStack();
			if (stack != null && stack.getItemMeta() != null && stack.getItemMeta().getDisplayName() != null) {
				String name = event.getItem().getItemStack().getItemMeta().getDisplayName();
				for (ItemStack hp : HealthPotion.all())
					if (name.equalsIgnoreCase(hp.getItemMeta().getDisplayName())) {
						ItemStack[] contents = event.getPlayer().getInventory().getContents();
						for (int i = 0; i < contents.length; i++) {
							if (contents[i] == null)
								continue;
							ItemMeta meta = null;
							if ((meta = contents[i].getItemMeta()) != null && meta.getDisplayName() != null)
								if (meta.getDisplayName().equalsIgnoreCase(hp.getItemMeta().getDisplayName())) {
									contents[i].setAmount(contents[i].getAmount() + 1);
									event.getPlayer().playSound(event.getItem().getLocation(), Sound.ITEM_PICKUP, 0.2F, 0.2F);
									event.getItem().remove();
									event.setCancelled(true);
									return;
								}
						}

					}
			}
			//

			event.setCancelled(false);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void dropItem(PlayerDropItemEvent event) {
		if (myConfig.isWorldDisabled(event.getPlayer().getWorld()))
			return;
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
		if (clazz == null) {
			return;
		}
	}

	/*
	 * @EventHandler(priority = EventPriority.NORMAL)
	 * 
	 * public void onBowShoot(EntityShootBowEvent event) { if (myConfig.isWorldDisabled(event.getEntity().getWorld())) return; if ((!(event.getEntity()
	 * instanceof Player)) || (PlayerManager.getPlayer(event.getEntity().getUniqueId()) == null)) { return; } final Player player = (Player) event.getEntity();
	 * ItemStack stack = event.getBow(); myClass clazz = PlayerManager.getPlayer(player.getUniqueId()); if (clazz == null) { return; } if
	 * (Weapon.isWeapon(clazz, stack)) { Weapon weapon = Weapon.getInstance(clazz, stack); if (weapon.getType().equals(Material.BOW)) { final short d = (short)
	 * (stack.getDurability()); Manager.scheduleTask(new Runnable() { public void run() { player.getItemInHand().setDurability(d); } }); } } }
	 */

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPotion(final PlayerInteractEvent event) {
		if (myConfig.isWorldDisabled(event.getPlayer().getWorld()))
			return;
		myClass player = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
		if (player == null) {
			return;
		}
		if (player.getHealth() == player.getMaxHealth()) {
			return;
		}
		String apple1 = new Apple_1(1).getItemMeta().getDisplayName();
		String apple2 = new Apple_2(1).getItemMeta().getDisplayName();
		String hp1 = HealthPotion.getHP1(1).getItemMeta().getDisplayName();
		String hp2 = HealthPotion.getHP2(1).getItemMeta().getDisplayName();
		String hp3 = HealthPotion.getHP3(1).getItemMeta().getDisplayName();
		if ((event.getAction().equals(Action.RIGHT_CLICK_AIR)) || (event.getAction().equals(Action.RIGHT_CLICK_AIR))) {
			ItemStack stack = event.getItem();
			if ((stack.hasItemMeta()) && (stack.getItemMeta().hasDisplayName())) {
				String name = stack.getItemMeta().getDisplayName();
				boolean isApple = true;
				if (name.equals(hp1)) {
					player.addHealth(0.1D * player.getMaxHealth());
				} else if (name.equals(hp2)) {
					player.addHealth(0.2D * player.getMaxHealth());
				} else if (name.equals(hp3)) {
					player.addHealth(0.4D * player.getMaxHealth());
				} else if (name.equals(apple1)) {
					player.addHealth(0.5D * player.getMaxHealth());
				} else if (name.equals(apple2)) {
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

	@EventHandler(priority = EventPriority.NORMAL)
	public void onChestOpen(InventoryOpenEvent event) {
		if (event.getInventory().getHolder() instanceof Chest) {
			if (!ChestHelper.isAvailable(event.getPlayer().getUniqueId(),
					new Vector(((Chest) event.getInventory().getHolder()).getLocation())))
				event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEggThrow(final PlayerEggThrowEvent event) {
		if (petMap.containsKey(event.getPlayer().getUniqueId())) {
			final String[] a = petMap.get(event.getPlayer().getUniqueId());
			final Location loc = event.getEgg().getLocation().getBlock().getLocation();
			Manager.scheduleTask(new Runnable() {

				@Override
				public void run() {
					PlayerManager.getPlayer(event.getPlayer().getUniqueId()).spawnPet(loc, a[0], a[1]);
				}
			});
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEggThrow(final PlayerInteractEvent event) {
		myClass player = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
		ItemStack egg = event.getItem();
		if (egg == null)
			return;
		if (!egg.getType().equals(Material.EGG))
			return;
		if (egg.getItemMeta() == null)
			return;
		String eggName = egg.getItemMeta().getDisplayName();
		String[] s = eggName.split("\\(");
		String name = Utils.cleanString(s[0]);
		String mob = Utils.cleanString(s[1].substring(0, s[1].length() - 1));
		String[] a = { mob, name };
		petMap.put(player.getPlayer().getUniqueId(), a);
		Manager.scheduleTask(new Runnable() {

			@Override
			public void run() {
				petMap.remove(event.getPlayer().getUniqueId());

			}
		}, 20L);

	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPetCall(PlayerInteractEvent event) {
		myClass player = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
		if (!player.getPlayer().isSneaking())
			return;
		ItemStack hand = player.getPlayer().getItemInHand();
		if (hand == null || !hand.getType().equals(Material.LEASH))
			return;
		for (Pet pet : player.getPets()) {
			Entity entity = EntityUtils.getEntity(pet.getId(), event.getPlayer().getWorld());
			LivingEntity le = (LivingEntity) entity;
			if (le == null)
				continue;
			if (le.isLeashed())
				if (le.getLeashHolder().getUniqueId().equals(player.getPlayer().getUniqueId()))
					continue;
			Location v = le.getLocation().subtract(player.getPlayer().getLocation());
			Location loc = player.getPlayer().getLocation().add(v.toVector().normalize().multiply(3));
			le.teleport(loc);
			le.setLeashHolder(player.getPlayer());
			if (hand.getAmount() == 1)
				player.getPlayer().setItemInHand(null);
			else
				hand.setAmount(hand.getAmount() - 1);
			break;
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) {
		if (myConfig.isWorldDisabled(event.getPlayer().getWorld()))
			return;
		myClass player = PlayerManager.getPlayer(event.getPlayer().getUniqueId());
		if (player == null)
			return;
		String cf = myConfig.getChatFormat();
		if (cf == null || !cf.contains("%PLAYER%") || !cf.contains("%MESSAGE%"))
			return;
		cf = cf.replace("%PLAYER%", player.getPlayer().getName());
		cf = cf.replace("%MESSAGE%", event.getMessage());
		String clazz = player.getType();
		String group = GroupManager.getGroup(player);
		String level = String.valueOf(player.getLevel());
		if (clazz == null)
			clazz = "";
		if (group == null)
			group = "";
		if (level == null)
			level = "";
		String format = cf.replace("%CLASS%", clazz).replace("%GROUP%", group).replace("%LEVEL%", level);
		event.setFormat(ChatColor.translateAlternateColorCodes('&', format));
	}
}
