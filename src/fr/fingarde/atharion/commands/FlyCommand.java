package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.Error;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FlyCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/fly §7[player] [on|off]";
    String permission = "atharion.fly";
    String permissionOther = "atharion.flyother";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            if (!(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            Player player = (Player) sender;

            String state = "activé";
            if (player.getAllowFlight()) { state = "désactivé"; }

            player.setAllowFlight(!player.getAllowFlight());

            player.sendMessage("§aFly mode §e" + state + "§a.");
        }
        else if (args.length == 1 || args.length == 2)
        {
            if (!sender.hasPermission(permissionOther))
            {
                Error.noPermission(sender, permissionOther);
                return false;
            }

            Player victim = Bukkit.getPlayer(args[0]);
            if (victim == null)
            {
                sender.sendMessage(usage);
                return false;
            }

            String state = "activé";
            boolean allowFlight = true;

            if (victim.getAllowFlight())
            {
                state = "désactivé";
                allowFlight = false;
            }

            if (args.length > 1)
            {
                String mode = getAllowFlight(args[1]);
                if (mode == null)
                {
                    sender.sendMessage(usage);
                    return false;
                }

                if (Boolean.parseBoolean(mode))
                {
                    state = "activé";
                    allowFlight = true;
                }
                else
                {
                    state = "désactivé";
                    allowFlight = false;
                }
            }

            victim.setAllowFlight(allowFlight);

            sender.sendMessage("§aFly mode §e" + state + "§a pour §e" + victim.getDisplayName() + "§a.");

            if (victim != sender)
            {
                if (sender instanceof Player)
                {
                    victim.sendMessage("§aFly mode §e" + state + "§a par §e" + ((Player) sender).getDisplayName() + "§a.");
                }
                else
                {
                    victim.sendMessage("§aFly mode §e" + state + "§a par §e" + sender.getName() + "§a.");
                }
            }
        }
        else
        {
            sender.sendMessage(usage);
        }

        return true;
    }

    private String getAllowFlight(String value)
    {
        value = value.toLowerCase();

        if (value.equals("on") || value.equals("enable") || value.equals("true")) return "true";
        else if (value.equals("off") || value.equals("disable") || value.equals("false")) return "false";

        return null;
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
        else if (args.length == 2)
        {
            value = new ArrayList<>();
            List<String> args1Completer = Arrays.asList("on", "off");

            if (args[1].length() == 0)
            {
                value = args1Completer;
            }
            else
            {
                for (String args1 : args1Completer)
                {
                    if (args1.startsWith(args[1].toLowerCase())) value.add(args1);
                }
            }
        }

        return value;
    }
}
