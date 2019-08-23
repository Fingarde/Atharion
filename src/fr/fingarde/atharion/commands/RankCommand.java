package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.Error;
import fr.fingarde.atharion.utils.Rank;
import fr.fingarde.atharion.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RankCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/setrank §a<player> <rank>";
    String permission = "atharion.setrank";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 2)
        {
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            Player victim = Bukkit.getPlayer(args[0]);
            if (victim == null) { sender.sendMessage(usage); return false; }

            User victimUser = User.getFromUUID(victim.getUniqueId());

            Rank rank = new Rank(args[1]);

            if(rank.isNull()) { sender.sendMessage(usage); return false; }

            victimUser.setRank(rank);

            sender.sendMessage("§e" + victim.getDisplayName() + "§a a désormais le rang §e" + rank.getName() + "§a.");


            if (victim != sender)
            {
                String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();

                victim.sendMessage("§e" + name + "§a vient de vous attribuer le rang §e" + rank.getName() + "§a.");
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
        else if (args.length == 2)
        {
            value = new ArrayList<>();
            ArrayList<String> args1Completer = Rank.getRanks();

            if (args[1].length() == 0)
            {
                for(String args1 : args1Completer)
                {
                    value.add(args1);
                }
            }
            else
            {
                for (String args1 : args1Completer)
                {
                    if (args1.toLowerCase().startsWith(args[1].toLowerCase()))
                    {
                        value.add(args1);
                    }
                }
            }
        }

        return value;
    }
}
