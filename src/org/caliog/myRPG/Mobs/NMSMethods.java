package org.caliog.myRPG.Mobs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.caliog.myRPG.NMS.NMS;

public class NMSMethods {

	public static void setTarget(Entity e, LivingEntity target) {
		try {
			Class<?> entityCreature = NMS.getNMSClass("EntityCreature");
			Class<?> craftEntity = NMS.getNMSClass("CraftEntity");
			Class<?> entityInsentient = NMS.getNMSClass("EntityInsentient");
			Class<?> pathfinderGoalSelector = NMS.getNMSClass("PathfinderGoalSelector");
			Class<?> pathfinderGoal = NMS.getNMSClass("PathfinderGoal");
			Class<?> pathfinderGoalRandomStroll = NMS.getNMSClass("PathfinderGoalRandomStroll");
			Class<?> pathfinderGoalFloat = NMS.getNMSClass("PathfinderGoalFloat");
			Class<?> pathfinderGoalMeleeAttack = NMS.getNMSClass("PathfinderGoalMeleeAttack");
			Class<?> pathfinderGoalMoveTowardsRestriction = NMS.getNMSClass("PathfinderGoalMoveTowardsRestriction");
			Class<?> pathfinderGoalHurtByTarget = NMS.getNMSClass("PathfinderGoalHurtByTarget");
			Class<?> pathfinderGoalNearestAttackableTarget = NMS.getNMSClass("PathfinderGoalNearestAttackableTarget");

			Object entity = entityCreature.cast(craftEntity.getMethod("getHandle").invoke(e));
			Object t = entityCreature.cast(craftEntity.getMethod("getHandle").invoke(target));
			Field goalsField = entityInsentient.getDeclaredField("goalSelector");
			goalsField.setAccessible(true);
			Object goals = pathfinderGoalSelector.cast(goalsField.get(entity));

			Field targetField = entityInsentient.getDeclaredField("targetSelector");
			targetField.setAccessible(true);
			Object targetSelector = pathfinderGoalSelector.cast(goalsField.get(entity));

			// TODO field name "b" is variable
			Field listField = pathfinderGoalSelector.getDeclaredField("b");
			listField.setAccessible(true);
			List<?> list = (List<?>) listField.get(goals);
			list.clear();
			// TODO field name "c" is variable
			listField = pathfinderGoalSelector.getDeclaredField("c");
			listField.setAccessible(true);

			list = (List<?>) listField.get(goals);
			list.clear();

			// TODO method name "a" is variable
			Method a = pathfinderGoalSelector.getMethod("a", int.class, pathfinderGoal);
			a.invoke(goals, 6, pathfinderGoalRandomStroll.getConstructor(entityCreature, double.class).newInstance(entity, 0F));
			a.invoke(goals, 0, pathfinderGoalFloat.getConstructor(entityInsentient).newInstance(entity));
			a.invoke(goals, 2,
					pathfinderGoalMeleeAttack.getConstructor(entityCreature, double.class, boolean.class).newInstance(entity, 1.0D, false));
			a.invoke(goals, 5, pathfinderGoalMoveTowardsRestriction.getConstructor(entityCreature, double.class).newInstance(entity, 1.0D));
			a.invoke(goals, 7, pathfinderGoalRandomStroll.getConstructor(entityCreature, double.class).newInstance(entity, 1D));
			a.invoke(targetSelector, 1, pathfinderGoalHurtByTarget.getConstructor(entityCreature, boolean.class).newInstance(entity, true));
			a.invoke(targetSelector, 2, pathfinderGoalNearestAttackableTarget.getConstructor(entityCreature, Class.class, boolean.class)
					.newInstance(entity, t.getClass(), false));
		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}

}
