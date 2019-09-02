package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.objects.User;
import fr.fingarde.atharion.utils.Error;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnmuteCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/unmute §a<player>";
    String permission = "atharion.mute";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1)
        {
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            Player victim = null;

            if (Bukkit.getPlayer(args[0]) != null)
            {
                victim = Bukkit.getPlayer(args[0]);
            }

            if (victim == null) { sender.sendMessage(usage); return false; }

            User user = User.getFromUUID(victim.getUniqueId());

            if(user.getMuteTimestamp() == 0)  { sender.sendMessage("§e" + victim.getName() + "§a n'est pas muet."); return false; }

            user.setMuteTimestamp(0);
            user.loadName();
            user.loadNameInTab();

            String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();
            victim.sendMessage("§e" + name + "§a vous a rendu la parole.");
            sender.sendMessage("§aVous avez rendu la parole à §e" + victim.getDisplayName() + "§a.");

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
