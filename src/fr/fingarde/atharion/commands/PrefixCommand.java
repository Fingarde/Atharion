package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.Error;
import fr.fingarde.atharion.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PrefixCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/prefix §7[prefix|reset] \n§r/prefix §a<player> §7[prefix|reset]";
    String permission = "atharion.prefix";
    String permissionOther = "atharion.prefixother";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length >= 1)
        {
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            Player player = (Player) sender;

            Player victim = player;

            if (Bukkit.getPlayer(args[0]) != null)
            {
                victim = Bukkit.getPlayer(args[0]);
            }

            if (victim != player && !(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if (victim != player && !sender.hasPermission(permissionOther)) { Error.noPermission(sender, permissionOther); return false; }


            String prefix = "";
            for (int i = 0; i < args.length; i++)
            {
                if(victim != player && i == 0) continue;

                prefix += " " + args[i];
            }

            prefix = prefix.substring(1);
            prefix = prefix.replaceAll("&", "§");

            if (prefix.equalsIgnoreCase("reset")) { prefix = ""; }
            User user = User.getFromUUID(victim.getUniqueId());

            sender.sendMessage("§aLe préfix de §e" + victim.getDisplayName() + "§a a été défini sur §e" + prefix + "§a.");

            user.setPrefix(prefix);

            if (victim != sender)
            {
                String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();

                victim.sendMessage("§aVotre préfix a été défini sur §e" + prefix + "§a par §e" + name);
            }
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
            List<String> args0Completer = new ArrayList<>();

            for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                args0Completer.add(onlinePlayer.getName());
            }

            args0Completer.add("RESET");

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