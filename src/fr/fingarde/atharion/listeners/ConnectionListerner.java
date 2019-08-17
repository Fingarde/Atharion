package fr.fingarde.atharion.listeners;

import fr.fingarde.atharion.Main;
import fr.fingarde.atharion.utils.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionListerner implements Listener
{
    @EventHandler
    public void onPing(ServerListPingEvent event)
    {
        event.setMotd(centerMotdLine("§6Le monde d'Atharion") + "\n" + centerMotdLine("§bMonde fantaisie§r    -    §e1.13.2"));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        try
        {
            Connection connection = Main.getHikari().getConnection();
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Players WHERE uuid = '" + player.getUniqueId().toString() + "'");

            if(!result.next())
            {
                statement.executeUpdate("INSERT INTO Players (uuid, rank, nickname, prefix, suffix) VALUES ('" + player.getUniqueId().toString() + "', 'visiteur', '', '', '')");

                statement.close();
                connection.close();
            }

            User user = new User(player.getUniqueId());

            user.loadNameInTab();
            user.loadPermissions();

            event.setJoinMessage(user.getDisplayName() + "§a a rejoint §e§lAtharion");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getFromUUID(player.getUniqueId());

        event.setQuitMessage(user.getDisplayName() + "§c a quitté §e§lAtharion");

        User.users.remove(user);
    }

    public String centerMotdLine(String text)
    {
        String textWColor = text;

        for (int i = 0; i < textWColor.length(); i++)
        {
            if (textWColor.charAt(i) == '§')
            {
                textWColor = textWColor.replaceAll(textWColor.substring(i, i + 2), "");
            }
        }

        int spaceToAdd  = (54 - textWColor.length()) / 2;

        String spaces = "";

        for (int i = 0; i < spaceToAdd; i++) spaces += " ";

        return spaces + text;
    }

}
