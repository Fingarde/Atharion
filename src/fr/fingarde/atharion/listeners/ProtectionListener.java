package fr.fingarde.atharion.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ProtectionListener implements Listener
{
    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent event)
    {
        if (event.getPlayer().hasPermission("atharion.interact")) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteractEntity2(PlayerInteractEntityEvent event)
    {
        if (event.getPlayer().hasPermission("atharion.interact")) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getPlayer().hasPermission("atharion.interact")) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player)) return;

        if (event.getDamager().hasPermission("atharion.interact")) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(BlockBreakEvent event)
    {
        if (!(event.getPlayer() instanceof Player)) return;

        if (event.getPlayer().hasPermission("atharion.interact")) return;
        event.setCancelled(true);
    }

}
