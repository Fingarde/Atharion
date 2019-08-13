package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Prefix implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§7[§bPrefix§7] §cCette commande est uniquement utilisable par des joueurs.");
            return false;
        }

        Player player = (Player) sender;

        Player victim = player;

        if(!player.hasPermission("atharion.prefix") && !player.hasPermission("atharion.prefixother"))
        {
            sender.sendMessage("§cVous n'avez pas la permission.");
            return false;
        }

        if(args.length  == 0)
        {
            sender.sendMessage("§7[§bPrefix§7] §cVous devez spécifier un nom correct.");
            return false;
        }

        for(Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if(!onlinePlayer.getName().equalsIgnoreCase(args[0])) continue;

            victim = onlinePlayer;

            if(args.length == 1)
            {
                sender.sendMessage("§7[§bPrefix§7] §cVous devez spécifier un nom correct.");
                return false;
            }
        }

        if(!victim.getName().equalsIgnoreCase(player.getName()) && !player.hasPermission("atharion.prefixother"))
        {
            sender.sendMessage("§7[§bPrefix§7] §cVous ne pouvez pas changer le préfix ce joueur.");
            return false;
        }

        String prefix = "";

        for(int i = 0; i < args.length; i++)
        {
            if(!victim.getName().equalsIgnoreCase(player.getName()) && i == 0) continue;

            prefix += " " + args[i];
        }

        prefix = prefix.substring(1);
        prefix = prefix.replaceAll("&", "§");

        if(prefix.equalsIgnoreCase("off")) prefix = "";

        if(victim.getName().equalsIgnoreCase(player.getName()))
        {
            player.sendMessage("§7[§bPrefix§7] §aVotre nouveau préfix est §e"  + prefix + "§a.");
        }
        else
        {
            player.sendMessage("§7[§bPrefix§7] §aLe nouveau préfix de §e" + victim.getDisplayName() + "§a est §e" + prefix + "§a.");
            victim.sendMessage("§7[§bPrefix§7] §e" + player.getDisplayName() + " §aa changé votre préfix en §e" + prefix + "§a.");
        }

        User user = User.getFromUUID(victim.getUniqueId());
        user.setPrefix(prefix);

        return true;
    }
}
