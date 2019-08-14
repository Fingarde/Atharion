package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.Error;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GamemodeCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/gamemode §a<0|1|2|3> §7[player]";
    String permission = "atharion.gamemode";
    String permissionOther = "atharion.gamemodeother";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1)
        {
            if (!(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            Player player = (Player) sender;

            GameMode gamemode = getGamemode(args[0]);
            if (gamemode == null) { player.sendMessage(usage); return false; }

            player.setGameMode(gamemode);

            player.sendMessage("§aVotre mode de jeu a été définit sur §e" + gamemode.name().toLowerCase() + "§a.");

        }
        else if (args.length == 2)
        {
            if (!sender.hasPermission(permissionOther)) { Error.noPermission(sender, permissionOther); return false; }

            GameMode gamemode = getGamemode(args[0]);
            if (gamemode == null) { sender.sendMessage(usage); return false; }

            Player victim = Bukkit.getPlayer(args[1]);
            if (victim == null) { sender.sendMessage(usage); return false; }

            victim.setGameMode(gamemode);

            sender.sendMessage("§aLe mode de jeu de §e" + victim.getDisplayName() + "§a a été définit sur §e" + gamemode.name().toLowerCase() + "§a.");

            if (victim != sender)
            {
                if(sender instanceof Player)
                {
                    victim.sendMessage("§aVotre mode de jeu a été définit sur §e" + gamemode.name().toLowerCase() + "§a par §e" + ((Player) sender).getDisplayName() + "§a.");
                }
                else
                {
                    victim.sendMessage("§aVotre mode de jeu a été définit sur §e" + gamemode.name().toLowerCase() + "§a par §e" + sender.getName() + "§a.");
                }
            }
        }
        else
        {
            sender.sendMessage(usage);
        }

        return true;
    }

    private GameMode getGamemode(String value)
    {
        value = value.toLowerCase();

        if ("survival".startsWith(value) || "survie".startsWith(value) || value.equalsIgnoreCase("0")) return GameMode.SURVIVAL;
        else if ("creative".startsWith(value) || "creatif".startsWith(value) || value.equalsIgnoreCase("1")) return GameMode.CREATIVE;
        else if ("adventure".startsWith(value) || "aventure".startsWith(value) || value.equalsIgnoreCase("2")) return GameMode.ADVENTURE;
        else if ("spectator".startsWith(value) || "spectateur".startsWith(value) || value.equalsIgnoreCase("3")) return GameMode.SPECTATOR;

        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        List<String> value = null;

        if (args.length == 1)
        {
            value = new ArrayList<>();
            List<String> args0Completer = Arrays.asList("survival", "creative", "adventure", "spectator");

            if (args[0].length() == 0)
            {
                value = args0Completer;
            }
            else
            {
                for (String args0 : args0Completer)
                {
                    if (args0.startsWith(args[0].toLowerCase())) value.add(args0);
                }
            }
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
                    if (args1.getName().toLowerCase().startsWith(args[1].toLowerCase())) value.add(args1.getName());
                }
            }
        }

        return value;
    }
}
