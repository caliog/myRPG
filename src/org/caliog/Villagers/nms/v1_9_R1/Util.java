package org.caliog.Villagers.nms.v1_9_R1;

import java.lang.reflect.Field;
import java.util.Set;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.caliog.Villagers.NPC.Trader;
import org.caliog.Villagers.NPC.VillagerNPC;
import org.caliog.Villagers.NPC.Util.Recipe;
import org.caliog.Villagers.nms.NMSUtil;

import net.minecraft.server.v1_9_R1.EntityCreature;
import net.minecraft.server.v1_9_R1.EntityHuman;
import net.minecraft.server.v1_9_R1.EntityInsentient;
import net.minecraft.server.v1_9_R1.EntityVillager;
import net.minecraft.server.v1_9_R1.MerchantRecipe;
import net.minecraft.server.v1_9_R1.MerchantRecipeList;
import net.minecraft.server.v1_9_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_9_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_9_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_9_R1.StatisticList;

public class Util implements NMSUtil {

	public static net.minecraft.server.v1_9_R1.Entity getHandle(Entity entity) {
		return ((CraftEntity) entity).getHandle();
	}

	@Override
	public void initVillager(VillagerNPC npc) {
		EntityInsentient entity = (EntityInsentient) getHandle(npc.getBukkitEntity());
		try {
			Field goalsField = EntityInsentient.class.getDeclaredField("goalSelector");
			goalsField.setAccessible(true);
			PathfinderGoalSelector goals = (PathfinderGoalSelector) goalsField.get(entity);

			Field listField = PathfinderGoalSelector.class.getDeclaredField("b");
			listField.setAccessible(true);
			Set<?> list = (Set<?>) listField.get(goals);
			list.clear();
			listField = PathfinderGoalSelector.class.getDeclaredField("c");
			listField.setAccessible(true);

			list = (Set<?>) listField.get(goals);
			list.clear();

			goals.a(6, new PathfinderGoalRandomStroll((EntityCreature) entity, 0F));
			goals.a(7, new PathfinderGoalLookAtPlayer((EntityInsentient) entity, EntityHuman.class, 8.0F));

		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {

			e.printStackTrace();
		}

	}

	@Override
	public boolean openInventory(Trader trader, Player player) {
		Recipe recipe = trader.getRecipe();
		if (recipe.isEmpty())
			return false;
		try {
			EntityVillager villager = new EntityVillager(((CraftPlayer) player).getHandle().world, 0);
			if ((trader.getName() != null)) {
				villager.setCustomName(trader.getName());
			}
			Field careerLevelField = EntityVillager.class.getDeclaredField("bH");
			careerLevelField.setAccessible(true);
			careerLevelField.set(villager, Integer.valueOf(10));

			Field recipeListField = EntityVillager.class.getDeclaredField("trades");
			recipeListField.setAccessible(true);
			MerchantRecipeList recipeList = (MerchantRecipeList) recipeListField.get(villager);
			if (recipeList == null) {
				recipeList = new MerchantRecipeList();
				recipeListField.set(villager, recipeList);
			}
			recipeList.clear();
			for (org.bukkit.inventory.ItemStack[] rec : recipe.getRecipe()) {
				recipeList.add(createRecipe(rec[0], rec[1], rec[2]));
			}
			villager.setTradingPlayer(((CraftPlayer) player).getHandle());
			((CraftPlayer) player).getHandle().openTrade(villager);
			((CraftPlayer) player).getHandle().b(StatisticList.F);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	private MerchantRecipe createRecipe(ItemStack item1, ItemStack item2, ItemStack item3) {
		MerchantRecipe recipe = new MerchantRecipe(getHandle(item1), getHandle(item2), getHandle(item3));

		Field maxUsesField;
		try {
			maxUsesField = MerchantRecipe.class.getDeclaredField("maxUses");
			maxUsesField.setAccessible(true);
			maxUsesField.set(recipe, Integer.valueOf(99999));
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
		}
		return recipe;
	}

	private net.minecraft.server.v1_9_R1.ItemStack getHandle(ItemStack item) {
		if (item == null)
			return null;
		return CraftItemStack.asNMSCopy(item);
	}

}
