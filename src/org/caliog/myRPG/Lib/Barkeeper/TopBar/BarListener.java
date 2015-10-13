package org.caliog.myRPG.Lib.Barkeeper.TopBar;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.caliog.myRPG.Manager;

public class BarListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLogout(PlayerQuitEvent event) {
	TopBar.removeBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onKick(PlayerKickEvent event) {
	TopBar.removeBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
	teleport(event.getPlayer(), event.getTo().clone());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onRespawn(PlayerRespawnEvent event) {
	teleport(event.getPlayer(), event.getRespawnLocation().clone());
    }

    private void teleport(final Player player, final Location loc) {
	Manager.scheduleTask(new Runnable() {

	    @Override
	    public void run() {
		FakeEntity entity = TopBar.getEntity(player);
		if (entity != null) {
		    TopBar.updateBar(player, entity.getName(), entity.getHealth() / entity.getMaxHealth(), loc);
		}

	    }
	}, 2L);
    }
}
