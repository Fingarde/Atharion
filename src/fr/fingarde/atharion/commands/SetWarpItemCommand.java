package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.Main;
import fr.fingarde.atharion.objects.Warp;
import fr.fingarde.atharion.utils.Error;
import fr.fingarde.atharion.utils.ItemSerializer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SetWarpItemCommand implements CommandExecutor
{
    String usage = "§bUsage: §r/setwarpitem §a<nom>";
    String permission = "atharion.setwarpitem";

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

            if (((Player) sender).getInventory().getItemInMainHand().getType() == Material.AIR) { sender.sendMessage("§cVous devez tenir un objet en main."); return false; }
                if (Warp.getByName(args[0]) == null)
                {
                    { sender.sendMessage("§cLe warp §e" + args[0] + "§c n'existe pas."); return false; }
                }
                else
                {
                   statement.executeUpdate("UPDATE Warps SET item = '" + ItemSerializer.serializeItem(((Player) sender).getInventory().getItemInMainHand()) + "' WHERE name = '" + Warp.getByName(args[0]).getName() + "'");
                }

                statement.close();
                connection.close();
                sender.sendMessage("§e" + ((Player) sender).getDisplayName() + "§a vient de changer l'item du  warp §e" + args[0] + "§a.");

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
