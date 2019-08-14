package fr.fingarde.atharion.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("§7[§bGamemode§7] §cCette commande est uniquement utilisable par des joueurs.");
            return false;
		}

        Player player = (Player) sender;

        if(args.length  == 0)
        {
            sender.sendMessage("§7[§bGamemode§7] §cVous devez spécifier un gamemode correct.");
            return false;
        }

        if(!player.hasPermission("atharion.gamemode") && !player.hasPermission("atharion.gamemodeother"))
        {
            sender.sendMessage("§cVous n'avez pas la permission.");
            return false;
        }

        Player victim = player;

        if(args.length > 1)
        {
            for(Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                if(!onlinePlayer.getName().equalsIgnoreCase(args[1])) continue;

                victim = onlinePlayer;
            }

            if(victim.getName().equalsIgnoreCase(player.getName()))
            {
                sender.sendMessage("§7[§bGamemode§7] §cVous devez spécifier un joueur correct.");
                return false;
            }

        }

        GameMode gamemode = null;

        if("survival".startsWith(args[0].toLowerCase()) || "survival".startsWith(args[0].toLowerCase()) ||args[0].equalsIgnoreCase("0")) gamemode = GameMode.SURVIVAL;
        else if("creative".startsWith(args[0].toLowerCase()) || "creatif".startsWith(args[0].toLowerCase()) ||args[0].equalsIgnoreCase("1")) gamemode = GameMode.CREATIVE;
        else if("adventure".startsWith(args[0].toLowerCase()) || "aventure".startsWith(args[0].toLowerCase()) ||args[0].equalsIgnoreCase("2")) gamemode = GameMode.ADVENTURE;
        else if("spectator".startsWith(args[0].toLowerCase()) || "spectateur".startsWith(args[0].toLowerCase()) ||args[0].equalsIgnoreCase("3")) gamemode = GameMode.SPECTATOR;

        victim.setGameMode(gamemode);


        if(victim.getName().equalsIgnoreCase(player.getName()))
        {
            player.sendMessage("§7[§bGamemode§7] §aVotre gamemode est maintenant §e"  + gamemode.name().toLowerCase() + "§a.");
        }
        else
        {
            if(!player.hasPermission("atharion.gamemodeother"))
            {
                sender.sendMessage("§7[§bGamemode§7] §cVous n'avez pas la permission.");
                return false;
            }

            player.sendMessage("§7[§bGamemode§7] §aLe gamemode de §e" + victim.getDisplayName() + "§a est désormais §e" + gamemode.name().toLowerCase() + "§a.");
            victim.sendMessage("§7[§bGamemode§7] §e" + player.getDisplayName() + " §aa changé votre gamemode en §e" + gamemode.name().toLowerCase() + "§a.");
        }


        return true;
    }
}
