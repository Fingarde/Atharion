package fr.fingarde.atharion.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kick implements CommandExecutor
{
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
            sender.sendMessage("§7[§bKick§7] §cVous devez spécifier un joeur correct.");
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
    }
}