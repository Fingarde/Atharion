package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.Error;
import fr.fingarde.atharion.utils.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class WarpCommand implements CommandExecutor
{
    String usage = "§bUsage: §r/warp §a<nom>";
    String permission = "atharion.warp";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length == 0)
        {
            if (!(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if (!sender.hasPermission(permission + ".gui") && !sender.hasPermission(permission + ".*") ) { Error.noPermission(sender, permission + ".gui"); return false; }

            ArrayList<Warp> warpsToAddInInv = new ArrayList<>();//

            for(Warp warp : Warp.warps)
            {
                if(warp.getItem() != null && sender.hasPermission(permission + warp.getName().toLowerCase()))
                {
                    warpsToAddInInv.add(warp);
                }
            }

            Inventory inventory = Bukkit.createInventory((Player) sender, (warpsToAddInInv.size() > 54) ? 54 : toMultipleOf9(warpsToAddInInv.size()), " §c§lWarps");

            for(Warp warp : warpsToAddInInv)
            {
                ItemStack itemStack = new ItemStack(warp.getItem());

                ItemMeta itemMeta =  itemStack.getItemMeta();

                itemMeta.setDisplayName("§r" + warp.getName());

                ArrayList<String> lore = new ArrayList<>();

                lore.add(warp.getDescription());

                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);

                // TODO replace item + desc par itemstack + add command setlore + setname
                inventory.addItem(itemStack);
            }

            ((Player) sender).openInventory(inventory);
        }
        else if (args.length == 1)
        {
            if (!(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }

            if (Warp.getByName(args[0]) == null) { sender.sendMessage(usage); return false; }

            if (!sender.hasPermission(permission + "." + args[0].toLowerCase()) && !sender.hasPermission(permission + ".*") ) { Error.noPermission(sender, permission + "." + args[0].toLowerCase()); return false; }

            Location location = Warp.getByName(args[0]).getLocation();

            ((Player) sender).teleport(location);
        }
        else
        {
            sender.sendMessage(usage);
        }

        return true;
    }

    private int toMultipleOf9(int i)
    {
        return ((int) Math.ceil(i / (float) 9) * 9);
    }
}
