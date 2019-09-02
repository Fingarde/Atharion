package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.Main;
import fr.fingarde.atharion.objects.Warp;
import fr.fingarde.atharion.utils.Error;
import fr.fingarde.atharion.utils.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeleteWarpCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/deletewarp §a<nom>";
    String permission = "atharion.deletewarp";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1)
        {
            if (!(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            try
            {

                String location = LocationSerializer.serializeCenteredYP(((Player) sender).getLocation());
                if (Warp.getByName(args[0]) == null)
                {
                    sender.sendMessage("§aLe warp §e" + args[0] + "§a n'existe pas.");
                    return false;
                }
                else
                {
                    Connection connection = Main.getHikari().getConnection();
                    Statement statement = connection.createStatement();


                    statement.executeUpdate("DELETE FROM Warps WHERE name = '" + Warp.getByName(args[0]).getName() + "'");

                    statement.close();
                    connection.close();

                }
                sender.sendMessage("§aVous venez de supprimer le warp §e" + args[0] + "§a.");

                Warp.loadWarps();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
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
            ArrayList<Warp> args0Completer = Warp.warps;

            if (args[0].length() == 0)
            {
                for(Warp args0 : args0Completer)
                {
                    if(sender.hasPermission(permission + "." + args0.getName())) { value.add(args0.getName()); }
                }
            }
            else
            {
                for (Warp args0 : args0Completer)
                {
                    if (args0.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    {
                        if(sender.hasPermission(permission + "." + args0.getName())) { value.add(args0.getName()); }
                    }
                }
            }
        }

        return value;
    }
}
