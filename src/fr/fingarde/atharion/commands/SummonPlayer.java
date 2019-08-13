package fr.fingarde.atharion.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SummonPlayer implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§7[§bTeleportation§7] §cCette commande est uniquement utilisable par des joueurs.");
            return false;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("atharion.summon"))
        {
            sender.sendMessage("§cVous n'avez pas la permission.");
            return false;
        }

        if(args.length == 0)
        {
            player.sendMessage("§7[§bTeleportation§7] §cVous devez spécifier un joueur à téléporter.");
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
            player.sendMessage("§7[§bTeleportation§7] §cLe joueur §e" + args[0] + "§c est introuvable.");
            return false;
        }

        victim.sendMessage("§7[§bTeleportation§7] §e" + player.getName() + "§a vous a téléporté à lui.");

        victim.teleport(player);
        return true;
    }
}
