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

public class SpeedCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/speed §a<value> §7[player]";
    String permission = "atharion.speed";
    String permissionOther = "atharion.speedother";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1 || args.length == 2)
        {
            if (!sender.hasPermission(permission + "." + args[0]) && !sender.hasPermission(permission + ".*") ) { Error.noPermission(sender, permission + "." + args[0].toLowerCase()); return false; }

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

            float value;

            try
            {
                value = Float.valueOf(args[0]);
            }
            catch (NumberFormatException e)
            {
                sender.sendMessage(usage);

                return false;
            }

            value = (value / 10);

            String mode = "marche";

            if(victim.isFlying())
            {
                mode = "vol";
            }

            value += 0.1;

            if(value > 1) { value = 1; }

            if(value < 0) { value = 0; }


            if(mode.equals("marche"))
            {
                victim.setWalkSpeed(value);
            }
            else
            {
                victim.setFlySpeed(value);
            }

            if (victim != sender)
            {
                String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();

                sender.sendMessage("§aLa vitesse de §e" + mode + "§a de §e" + victim.getDisplayName() + "§a a été défini sur §e" + args[0]);
                victim.sendMessage("§aVotre vitesse de §e" + mode + "§a a été défini sur §e" + args[0] + "§a par §e" + name + "§a.");
            }
            else
            {
                sender.sendMessage("§aVotre vitesse de §e" + mode + "§a a été défini sur §e" + args[0]);
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

        if(args.length == 1)
        {
            value = new ArrayList<>();
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
