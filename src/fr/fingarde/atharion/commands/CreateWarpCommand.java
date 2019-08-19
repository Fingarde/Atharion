package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.Main;
import fr.fingarde.atharion.utils.Error;
import fr.fingarde.atharion.utils.LocationSerializer;
import fr.fingarde.atharion.utils.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateWarpCommand implements CommandExecutor
{
    String usage = "§bUsage: §r/setwarp §a<nom>";
    String permission = "atharion.creawarp";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1)
        {
            if (!(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            try
            {
                Connection connection = Main.getHikari().getConnection();
                Statement statement = connection.createStatement();

                String location = LocationSerializer.serializeCenteredYP(((Player) sender).getLocation());
                if (Warp.getByName(args[0]) == null)
                {
                    statement.executeUpdate("INSERT INTO Warps (name, location, item, description) VALUES ('" + args[0] + "', '" + location + "', '', '')");
                }
                else
                {
                   statement.executeUpdate("UPDATE Warps SET location = '" + location + "' WHERE name = '" + Warp.getByName(args[0]).getName() + "'");
                }

                statement.close();
                connection.close();
                sender.sendMessage("§e" + ((Player) sender).getDisplayName() + "§a vient de créer le warp §e" + args[0] + "§a.");

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
}
