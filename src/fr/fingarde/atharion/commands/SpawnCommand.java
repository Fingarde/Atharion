package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.objects.Warp;
import fr.fingarde.atharion.utils.Error;
import fr.fingarde.atharion.objects.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/spawn §a<player>";
    String permission = "atharion.spawn";
    String permissionOther = "atharion.spawnother";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length <= 1)
        {
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            Player victim = null;

            if(args.length == 1)
            {
                if (Bukkit.getPlayer(args[0]) != null)
                {
                    victim = Bukkit.getPlayer(args[0]);
                }
            }

            if(victim == null && !(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if(victim == null) { victim = (Player) sender; }

            if (victim != sender && !sender.hasPermission(permissionOther)) { Error.noPermission(sender, permissionOther); return false; }

            if(Warp.getByName("Spawn") == null) { sender.sendMessage("§aLe spawn n'a pas été defini"); return false; }

            victim.teleport(Warp.getByName("Spawn").getLocation());


            if (victim != sender)
            {
                String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();

                sender.sendMessage("§aTéléportation de §e" + victim.getDisplayName() + "§a vers §espawn");
                victim.sendMessage("§e" + name + "§a vous a téléporté vers §espawn");
            }
            else
            {
                sender.sendMessage("§aTéléportation vers §espawn");
            }

            return true;
        }
        else
        {
            sender.sendMessage(usage);
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        List<String> value = null;

        if (args.length == 1)
        {
            value = new ArrayList<>();
            List<String> args0Completer = new ArrayList<>();

            for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                args0Completer.add(onlinePlayer.getName());
            }

            if (args[0].length() == 0)
            {
                value = args0Completer;
            }
            else
            {
                for (String args0 : args0Completer)
                {
                    if (args0.toLowerCase().startsWith(args[0].toLowerCase()))
                    {
                        value.add(args0);
                    }
                }
            }
        }

        return value;
    }
}
