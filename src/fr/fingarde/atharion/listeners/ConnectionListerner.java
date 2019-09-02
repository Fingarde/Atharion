package fr.fingarde.atharion.listeners;

import fr.fingarde.atharion.Main;
import fr.fingarde.atharion.objects.User;
import fr.fingarde.atharion.objects.Warp;
import fr.fingarde.atharion.utils.TimestampConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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
                statement.executeUpdate("INSERT INTO Players (uuid, rank, nickname, prefix, suffix, joined_timestamp, muted_timestamp, jailed_timestamp, banned_timestamp) VALUES ('" + player.getUniqueId().toString() + "', 'visiteur', '', '', '', '" + new Date().getTime() + "', '0', '0', '0')");

                statement.close();
                connection.close();
            }

            User user = new User(player.getUniqueId());
            User.users.add(user);

            if(user.getBanTimestamp() != 0)
            {

                if(user.getBanTimestamp() != 1 && user.getBanTimestamp() - new Date().getTime() < 0)
                {
                    user.setBanTimestamp(0);

                }
                else if(user.getBanTimestamp() - new Date().getTime() > 1 || user.getBanTimestamp() == 1)
                {
                    String messageRefused;

                    if(user.getBanTimestamp() == 1)
                    {
                        messageRefused = "§cVous avez été banni\nveuillez contacter un modérateur sur discord\n§ehttps://discord.gg/KeSFqmE";
                    }
                    else
                    {
                        messageRefused = "§cVous avez été banni\n§e" + TimestampConverter.getTime(user.getBanTimestamp() - new Date().getTime());
                    }

                    Bukkit.broadcastMessage(player.getName() + " tried to join but he was banned");
                    player.kickPlayer(messageRefused);
                    return;
                }
            }

            if(user.getBanTimestamp() == 0)
            {
                user.loadPermissions();
                user.loadName();
                user.loadNameInTab();

                event.setJoinMessage(user.getDisplayName() + "§a a rejoint §e§lAtharion");
            }
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
