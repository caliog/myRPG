package org.caliog.Villagers.nms;

import org.bukkit.entity.Player;
import org.caliog.Villagers.NPC.Trader;
import org.caliog.Villagers.NPC.VillagerNPC;

public interface NMSUtil {

	public abstract void initVillager(VillagerNPC npc);

	public abstract boolean openInventory(Trader trader, Player player);

}
