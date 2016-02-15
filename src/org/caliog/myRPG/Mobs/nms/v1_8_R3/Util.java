package org.caliog.myRPG.Mobs.nms.v1_8_R3;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.caliog.myRPG.Mobs.nms.NMSUtil;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class Util implements NMSUtil {

	@Override
	public void setTarget(Entity e, LivingEntity target) {
		EntityCreature entity = (EntityCreature) ((CraftEntity) e).getHandle();
		EntityCreature t = (EntityCreature) ((CraftEntity) e).getHandle();
		try {
			Field goalsField = EntityInsentient.class.getDeclaredField("goalSelector");
			goalsField.setAccessible(true);
			PathfinderGoalSelector goals = (PathfinderGoalSelector) goalsField.get(entity);

			Field targetField = EntityInsentient.class.getDeclaredField("targetSelector");
			targetField.setAccessible(true);
			PathfinderGoalSelector targetSelector = (PathfinderGoalSelector) targetField.get(entity);

			Field listField = PathfinderGoalSelector.class.getDeclaredField("b");
			listField.setAccessible(true);
			List<?> list = (List<?>) listField.get(goals);
			list.clear();
			listField = PathfinderGoalSelector.class.getDeclaredField("c");
			listField.setAccessible(true);

			list = (List<?>) listField.get(goals);
			list.clear();

			goals.a(6, new PathfinderGoalRandomStroll((EntityCreature) entity, 0F));
			goals.a(0, new PathfinderGoalFloat((EntityInsentient) entity));
			goals.a(2, new PathfinderGoalMeleeAttack(entity, 1.0D, false));
			goals.a(5, new PathfinderGoalMoveTowardsRestriction(entity, 1.0D));
			goals.a(7, new PathfinderGoalRandomStroll(entity, 1.0D));
			targetSelector.a(1, new PathfinderGoalHurtByTarget(entity, true));
			targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(entity, t.getClass(), false));
		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}

}
