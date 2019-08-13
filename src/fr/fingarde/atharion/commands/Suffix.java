package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Suffix implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§7[§bSuffix§7] §cCette commande est uniquement utilisable par des joueurs.");
            return false;
        }

        Player player = (Player) sender;

        Player victim = player;

        if(!player.hasPermission("atharion.suffix") && !player.hasPermission("atharion.suffixother"))
        {
            sender.sendMessage("§cVous n'avez pas la permission.");
            return false;
        }

        if(args.length  == 0)
        {
            sender.sendMessage("§7[§bSuffix§7] §cVous devez spécifier un nom correct.");
            return false;
        }

        for(Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if(!onlinePlayer.getName().equalsIgnoreCase(args[0])) continue;

            victim = onlinePlayer;

            if(args.length == 1)
            {
                sender.sendMessage("§7[§bSuffix§7] §cVous devez spécifier un nom correct.");
                return false;
            }
        }

        if(!victim.getName().equalsIgnoreCase(player.getName()) && !player.hasPermission("atharion.suffixother"))
        {
            sender.sendMessage("§7[§bSuffix§7] §cVous ne pouvez pas changer le suffix ce joueur.");
            return false;
        }

        String suffix = "";

        for(int i = 0; i < args.length; i++)
        {
            if(!victim.getName().equalsIgnoreCase(player.getName()) && i == 0) continue;

            suffix += " " + args[i];
        }

        suffix = suffix.substring(1);
        suffix = suffix.replaceAll("&", "§");

        if(suffix.equalsIgnoreCase("off")) suffix = "";

        if(victim.getName().equalsIgnoreCase(player.getName()))
        {
            player.sendMessage("§7[§bSuffix§7] §aVotre nouveau préfix est §e"  + suffix + "§a.");
        }
        else
        {
            player.sendMessage("§7[§bSuffix§7] §aLe nouveau suffix de §e" + victim.getDisplayName() + "§a est §e" + suffix + "§a.");
            victim.sendMessage("§7[§bSuffix§7] §e" + player.getDisplayName() + " §aa changé votre suffix en §e" + suffix + "§a.");
        }

        User user = User.getFromUUID(victim.getUniqueId());
        user.setPrefix(suffix);

        return true;
    }
}
