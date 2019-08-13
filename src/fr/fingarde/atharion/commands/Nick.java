package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Nick implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§7[§bSurnom§7] §cCette commande est uniquement utilisable par des joueurs.");
            return false;
        }

        Player player = (Player) sender;

        Player victim = player;

        if(!player.hasPermission("atharion.nick") && !player.hasPermission("atharion.nickother"))
        {
            sender.sendMessage("§cVous n'avez pas la permission.");
            return false;
        }

        if(args.length  == 0)
        {
            sender.sendMessage("§7[§bSurnom§7] §cVous devez spécifier un nom correct.");
            return false;
        }

        for(Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if(!onlinePlayer.getName().equalsIgnoreCase(args[0])) continue;

            victim = onlinePlayer;

            if(args.length == 1)
            {
                sender.sendMessage("§7[§bSurnom§7] §cVous ne pouvez pas vous renommer comme cela.");
                return false;
            }
        }

        if(!victim.getName().equalsIgnoreCase(player.getName()) && !player.hasPermission("atharion.nickother"))
        {
            sender.sendMessage("§7[§bSurnom§7] §cVous ne pouvez pas renommer ce joueur.");
            return false;
        }

        String nickname = "";

        for(int i = 0; i < args.length; i++)
        {
            if(!victim.getName().equalsIgnoreCase(player.getName()) && i == 0) continue;

            nickname += " " + args[i];
        }

        nickname = nickname.substring(1);
        nickname = nickname.replaceAll("&", "§");

        if(nickname.equalsIgnoreCase("off")) nickname = victim.getName();

        if(victim.getName().equalsIgnoreCase(player.getName()))
        {
            player.sendMessage("§7[§bSurnom§7] §aVotre nouveau surnom est §e"  + nickname + "§a.");
        }else
        {
            player.sendMessage("§7[§bSurnom§7] §aLe nouveau surnom de §e" + victim.getDisplayName() + "§a est §e" + nickname + "§a.");
            victim.sendMessage("§7[§bSurnom§7] §e" + player.getDisplayName() + " §aa changé votre pseudo en §e" + nickname + "§a.");
        }

        User user = User.getFromUUID(victim.getUniqueId());
        user.setNickname(nickname);

        return true;
    }
}
