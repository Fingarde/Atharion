package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.Rank;
import fr.fingarde.atharion.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRank implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!sender.hasPermission("atharion.setrank"))
        {
            sender.sendMessage("§cVous n'avez pas la permission.");
            return false;
        }

        if(args.length < 2)
        {
            sender.sendMessage("§7[§bRang§7] §cVous devez spécifier un joueur et un rang a attribuer.");
            return false;
        }

        Player victim = null;

        for(Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if(!onlinePlayer.getName().toLowerCase().startsWith(args[0].toLowerCase())) continue;

            victim = onlinePlayer;
            break;
        }

        if(victim == null)
        {
            sender.sendMessage("§7[§bRang§7] §cLe joueur §e" + args[0] + "§c est introuvable.");
            return false;
        }

        User user = User.getFromUUID(victim.getUniqueId());
        Rank rank = new Rank(args[1].toLowerCase());

        user.setRank(rank);


        sender.sendMessage("§7[§bRang§7] §e" + victim.getDisplayName() + "§a a désormais le rang §e"  + rank.getName() + "§a.");

        return true;
    }
}
