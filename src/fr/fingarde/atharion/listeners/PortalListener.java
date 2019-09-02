package fr.fingarde.atharion.listeners;

import fr.fingarde.atharion.objects.Warp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class PortalListener implements Listener
{
    ArrayList<Player> wasOnWater = new ArrayList<>();

    @EventHandler
    public void onEnterPortal(PlayerMoveEvent event)
    {

        if(event.getPlayer().getLocation().getBlock().getType() == Material.WATER || hasEmeraldBlockUnder(event.getPlayer().getLocation()))
        {
            if(wasOnWater.contains(event.getPlayer())) { return; }

            wasOnWater.add(event.getPlayer());

            Block emeraldBlock = null;
            for(int y = 1; y < 31; y++)
            {
                if(event.getPlayer().getLocation().getBlock().getRelative(0, -y , 0).getType() == Material.EMERALD_BLOCK)
                {
                    emeraldBlock = event.getPlayer().getLocation().getBlock().getRelative(0, -y , 0);
                    break;
                }
            }

            if(emeraldBlock == null) return;

            if(emeraldBlock.getRelative(0, -1, 0).getType().name().toLowerCase().contains("sign"))
            {
                Sign sign = (Sign) emeraldBlock.getRelative(0, -1, 0).getState();

                if(sign.getLine(0).equalsIgnoreCase("§7[§3warp§7]"))
                {
                    if(Warp.getByName(sign.getLine(1)) == null) { return; }

                    Warp warp = Warp.getByName(sign.getLine(1));

                    event.getPlayer().teleport(warp.getLocation());
                    event.getPlayer().sendMessage("§aTéléportation vers §e" + warp.getName());
                }
            }
        }
        else
        {
            if(wasOnWater.contains(event.getPlayer())) {  wasOnWater.remove(event.getPlayer()); }
        }
    }

    private boolean hasEmeraldBlockUnder(Location location)
    {
        Block block = location.getBlock();

        if(block.getRelative(0, -1, 0).getType() == Material.EMERALD_BLOCK ) return true;
        else if(block.getRelative(0, -2, 0).getType() == Material.EMERALD_BLOCK ) return true;
        else if(block.getRelative(0, -3, 0).getType() == Material.EMERALD_BLOCK ) return true;

        return false;
    }

    @EventHandler
    public void onSign(SignChangeEvent event)
    {
        if(event.getLine(0).equalsIgnoreCase("§7[§3warp§7]") && !event.getPlayer().hasPermission("atharion.createsign"))
        {
            event.setLine(0, "[warp]");
        }
        else if(event.getLine(0).equalsIgnoreCase("[warp]") && event.getPlayer().hasPermission("atharion.createsign"))
        {
            event.setLine(0, "§7[§3warp§7]");
        }
    }
}
