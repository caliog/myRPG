package org.caliog.Villagers.nms.v1_8_R3;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.caliog.Villagers.NPC.Trader;
import org.caliog.Villagers.NPC.VillagerNPC;
import org.caliog.Villagers.NPC.Util.Recipe;
import org.caliog.Villagers.nms.NMSUtil;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.MerchantRecipe;
import net.minecraft.server.v1_8_R3.MerchantRecipeList;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.StatisticList;

public class Util extends NMSUtil {

	public static net.minecraft.server.v1_8_R3.Entity getHandle(Entity entity) {
		return ((CraftEntity) entity).getHandle();
	}

	@Override
	public void initVillager(VillagerNPC npc) {
		EntityLiving entity = (EntityLiving) getHandle(npc.getBukkitEntity());
		try {
			Field goalsField = EntityInsentient.class.getDeclaredField("goalSelector");
			goalsField.setAccessible(true);
			PathfinderGoalSelector goals = (PathfinderGoalSelector) goalsField.get(entity);

			Field listField = PathfinderGoalSelector.class.getDeclaredField("b");
			listField.setAccessible(true);
			List<?> list = (List<?>) listField.get(goals);
			list.clear();
			listField = PathfinderGoalSelector.class.getDeclaredField("c");
			listField.setAccessible(true);

			list = (List<?>) listField.get(goals);
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
			Field careerLevelField = EntityVillager.class.getDeclaredField("by");
			careerLevelField.setAccessible(true);
			careerLevelField.set(villager, Integer.valueOf(10));

			Field recipeListField = EntityVillager.class.getDeclaredField("br");
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
			villager.a_(((CraftPlayer) player).getHandle());
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

	private net.minecraft.server.v1_8_R3.ItemStack getHandle(ItemStack item) {
		if (item == null)
			return null;
		return CraftItemStack.asNMSCopy(item);
	}

}
