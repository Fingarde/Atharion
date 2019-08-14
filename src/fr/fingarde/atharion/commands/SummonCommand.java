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

public class SummonCommand implements CommandExecutor, TabCompleter
{

    String usage = "§bUsage: §r/s §7[player]";
    String permission = "atharion.summon";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1)
        {
            if (!(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            Player player = (Player) sender;

            Player victim = Bukkit.getPlayer(args[0]);
            if (victim == null) { sender.sendMessage(usage); return false; }

            victim.teleport(player);

            player.sendMessage("§aVous avez téléporté §e" + victim.getDisplayName() + "§a vers vous.");
            victim.sendMessage("§e" + player.getDisplayName() + "§a vous avez téléporté vers lui.");
        }
        else
        {
            sender.sendMessage(usage);
        }

        return true;
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
                    if (args0.getName().toLowerCase().startsWith(args[0].toLowerCase())) value.add(args0.getName());
                }
            }
        }

        return value;
    }
}
