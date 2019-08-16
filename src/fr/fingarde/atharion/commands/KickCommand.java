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

            String basicMessage = "§e" + name + "§a vient de vous exclure d'§e§lAtharion§r§a";
            String message = "";

            if (args.length > 1)
            {
                basicMessage += " pour \n§e";

                for (int i = 1; i < args.length; i++)
                {
                    message += args[i];
                }
            }

            victim.kickPlayer(basicMessage + message);

            Bukkit.broadcastMessage("§e" + name + "§a vient d'exclure §e" + victim.getDisplayName() + ((message.length() == 0) ? "" : "§a pour §e" + message + "§a."));
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
                    if (args0.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    {
                        value.add(args0.getName());
                    }
                }
            }
        }

        return value;
    }

    /*
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§7[§bKick§7] §cCette commande est uniquement utilisable par des joueurs.");
            return false;
        }

        Player player = (Player) sender;



        if(args.length  == 0)
        {
            sender.sendMessage("§7[§bKick§7] §cVous devez spécifier un nom correct.");
            return false;
        }

        if(!player.hasPermission("atharion.kick"))
        {
            sender.sendMessage("§cVous n'avez pas la permission.");
            return false;
        }

        Player victim = null;


        for(Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if(!onlinePlayer.getName().equalsIgnoreCase(args[0])) continue;

            victim = onlinePlayer;
        }

        if(victim == null)
        {
            sender.sendMessage("§7[§bKick§7] §cVous devez spécifier un joueur correct.");
            return false;
        }

        String message = "";

        if(args.length != 1)
        {
            for (int i = 0; i < args.length; i++) {
                if (i == 0) continue;

                message += " " + args[i];
            }

            message = message.substring(1);
            message = message.replaceAll("&", "§");
        }
        else
        {
            message = "Vous avez été explusé par un modérateur";
        }

        if(message.endsWith("-s"))
        {
            message = message.substring(0, message.length() - 1);
        }
        else
        {
            Bukkit.broadcastMessage("§7[§bKick§7] §e" + victim.getDisplayName() + "§a a été expulsé par §e" + player.getName() + "§a pour §e" + message + "§a .");
        }

        victim.kickPlayer(message);

        player.sendMessage("§7[§bKick§7] §aVous avez expulsé §e" + victim.getDisplayName() + "§a pour §e" + message + "§a .");

        return true;
    }*/
}