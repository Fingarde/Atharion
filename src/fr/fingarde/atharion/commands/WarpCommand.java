package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.objects.Warp;
import fr.fingarde.atharion.utils.Error;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/warp §a<nom>";
    String permission = "atharion.warp";
    String permissionOther = "atharion.warpother";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length == 0)
        {
            if (!(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if (!sender.hasPermission(permission + "opengui") && !sender.hasPermission(permission + ".*") ) { Error.noPermission(sender, permission + ".opengui"); return false; }

            ArrayList<Warp> warpsToAddInInv = new ArrayList<>();//

            for(Warp warp : Warp.warps)
            {
                if(warp.getItem() != null && sender.hasPermission(permission + "." + warp.getName()))
                {
                    warpsToAddInInv.add(warp);
                }
            }

            Inventory inventory = Bukkit.createInventory((Player) sender, (warpsToAddInInv.size() > 54) ? 54 : toMultipleOf9(warpsToAddInInv.size()), "  §8§lWarps");

            for (Warp warp : warpsToAddInInv)
            {
                inventory.addItem(warp.getItem());
            }

            ((Player) sender).openInventory(inventory);

            return true;
        }
        else if (args.length == 1 || args.length == 2)
        {
            if (Warp.getByName(args[0]) == null) { sender.sendMessage(usage); return false; }

            if (!sender.hasPermission(permission + "." + args[0]) && !sender.hasPermission(permission + ".*") ) { Error.noPermission(sender, permission + "." + args[0].toLowerCase()); return false; }

            Location location = Warp.getByName(args[0]).getLocation();

            Player victim = null;


            if(args.length == 2)
            {
                if (Bukkit.getPlayer(args[1]) != null)
                {
                    victim = Bukkit.getPlayer(args[1]);
                }
            }

            if(victim == null && !(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if(victim == null) { victim = (Player) sender; }

            if (victim != sender && !sender.hasPermission(permissionOther)) { Error.noPermission(sender, permissionOther); return false; }

            victim.teleport(location);

            if (victim != sender)
            {
                String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();

                sender.sendMessage("§aTeleportation de §e" + victim.getDisplayName() + "§a vers §e" + Warp.getByName(args[0]).getName());
                victim.sendMessage("§e" + name + "§a vous a téléporté vers §e" + Warp.getByName(args[0]).getName());
            }
            else
            {
                sender.sendMessage("§aTeleportation vers §e" + Warp.getByName(args[0]).getName());
            }

            return true;
        }
        else
        {
            sender.sendMessage(usage);

            return false;
        }
    }

    private int toMultipleOf9(int i)
{
    return ((int) Math.ceil(i / (float) 9) * 9);
}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        List<String> value = null;

        if (args.length == 1)
        {
            value = new ArrayList<>();
            ArrayList<Warp> args0Completer = Warp.warps;

            if (args[0].length() == 0)
            {
                for(Warp args0 : args0Completer)
                {
                    if(sender.hasPermission(permission + "." + args0.getName())) { value.add(args0.getName()); }
                }
            }
            else
            {
                for (Warp args0 : args0Completer)
                {
                    if (args0.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    {
                        if(sender.hasPermission(permission + "." + args0.getName())) { value.add(args0.getName()); }
                    }
                }
            }
        }
        else if (args.length == 2)
        {
            value = new ArrayList<>();
            Collection<? extends Player> args1Completer = Bukkit.getOnlinePlayers();

            if (args[1].length() == 0)
            {
                for(Player args1 : args1Completer)
                {
                    value.add(args1.getName());
                }
            }
            else
            {
                for (Player args1 : args1Completer)
                {
                    if (args1.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    {
                        value.add(args1.getName());
                    }
                }
            }
        }

        return value;
    }
}
