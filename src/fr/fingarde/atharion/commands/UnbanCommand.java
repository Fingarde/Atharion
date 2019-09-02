package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.objects.User;
import fr.fingarde.atharion.utils.Error;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnbanCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/unban §a<player>";
    String permission = "atharion.ban";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1)
        {
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            OfflinePlayer victim = null;

            for(OfflinePlayer player : Bukkit.getOfflinePlayers())
            {
                if (player.getName().equalsIgnoreCase(args[0]))
                {
                    victim = player;
                }
            }

            if (victim == null) { sender.sendMessage(usage); return false; }

            User user = new User(victim.getUniqueId());

            if(user.getBanTimestamp() == 0)  { sender.sendMessage("§e" + victim.getName() + "§a n'est pas banni."); return false; }
            user.setBanTimestamp(0);

            sender.sendMessage("§aVous avez débanni §e" + victim.getName() + "§a.");
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
