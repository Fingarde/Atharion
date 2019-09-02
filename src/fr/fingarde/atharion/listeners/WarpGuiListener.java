package fr.fingarde.atharion.listeners;

import fr.fingarde.atharion.objects.Warp;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class WarpGuiListener implements Listener
{
    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        if(event.getView().getTitle().equals("  §8§lWarps"))
        {
            event.setCancelled(true);

            for(Warp warp : Warp.warps)
            {
                if(warp.getItem() == event.getCurrentItem())
                {
                    event.getWhoClicked().teleport(warp.getLocation());
                    event.getWhoClicked().sendMessage("§aTéléportation vers §e" + warp.getName());
                    break;
                }
            }
        }
    }
}
