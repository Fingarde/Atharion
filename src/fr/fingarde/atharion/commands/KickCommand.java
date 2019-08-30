package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.Error;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KickCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/kick §a<player> §7[message]";
    String permission = "atharion.kick";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length >= 1)
        {
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            Player victim = Bukkit.getPlayer(args[0]);
            if (victim == null) { sender.sendMessage(usage); return false; }

            String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();

            String basicMessage = "§e" + name + "§a vient de vous exclure d'§e§lAtharion§r§a.";
            String message = "";

            if (args.length > 1)
            {
                basicMessage += " pour \n§e";

                for (int i = 1; i < args.length; i++)
                {
                    message += args[i];
                }
            }

            message = message.replaceAll("&", "§");

            victim.kickPlayer(basicMessage + message);

            Bukkit.broadcastMessage("§e" + name + "§a vient d'exclure §e" + victim.getDisplayName() + ((message.length() == 0) ? "" : "§a pour §e" + message + "§a."));

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
            Collection<? extends Player> args0Completer = Bukkit.getOnlinePlayers();

            if (args[0].length() == 0)
            {
                for(Player args0 : args0Completer)
                {
                    value.add(args0.getName());
                }
            }
            else
            {
                for (Player args0 : args0Completer)
                {
                    if (args0.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    {
                        value.add(args0.getName());
                    }
                }
            }
        }

        return value;
    }
}