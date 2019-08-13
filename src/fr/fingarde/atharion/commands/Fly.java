package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§7[§bFly§7] §cCette commande est uniquement utilisable par des joueurs.");
            return false;
        }

        Player player = (Player) sender;

        Player victim = player;

        if(!player.hasPermission("atharion.fly") && !player.hasPermission("atharion.flyother"))
        {
            sender.sendMessage("§cVous n'avez pas la permission.");
            return false;
        }

        if(args.length  > 1)
        {
            for(Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                if(!onlinePlayer.getName().equalsIgnoreCase(args[0])) continue;

                victim = onlinePlayer;

                if(args.length == 1)
                {
                    sender.sendMessage("§7[§bFly§7] §cVous ne pouvez pas vous renommer comme cela.");
                    return false;
                }
            }
        }

        if(!victim.getName().equalsIgnoreCase(player.getName()) && !player.hasPermission("atharion.flyother"))
        {
            sender.sendMessage("§7[§bFly§7] §cVous n'avez pas la permission.");
            return false;
        }

        String value;

        if(victim.getAllowFlight())
        {
            value = "interdit de";
        }
        else
        {
            value = "autorisé à" ;
        }

        victim.setAllowFlight(!victim.getAllowFlight());

        if(victim.getName().equalsIgnoreCase(player.getName()))
        {
            player.sendMessage("§7[§bFly§7] §aVous êtes " + value + " voler.");
        }
        else
        {
            player.sendMessage("§7[§bFly§7] §e" + player.getDisplayName() + "§a vous a " + value + " voler voler.");
            player.sendMessage("§7[§bFly§7] §e" + victim.getDisplayName() + "§a est " + value + " voler.");
        }

        return true;
    }
}
