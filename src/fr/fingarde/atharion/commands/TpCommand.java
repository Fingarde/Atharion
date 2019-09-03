package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.Error;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/tp §a<player>";
    String permission = "atharion.tp";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1)
        {
            if(!(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            Player victim = null;


            if (Bukkit.getPlayer(args[0]) != null)
            {
                victim = Bukkit.getPlayer(args[0]);
            }

            if(victim == null) { sender.sendMessage(usage); return false;  }


            ((Player) sender).teleport(victim);

            sender.sendMessage("§aTéléportation vers §e" + victim.getDisplayName());

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
