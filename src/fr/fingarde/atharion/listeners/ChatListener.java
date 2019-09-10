package fr.fingarde.atharion.listeners;

import fr.fingarde.atharion.Main;
import fr.fingarde.atharion.utils.Error;
import fr.fingarde.atharion.objects.User;
import fr.fingarde.atharion.utils.TimestampConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.Date;

public class ChatListener implements Listener
{
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();

        event.setCancelled(true);

        if (!player.hasPermission("atharion.chat")) { Error.noPermission(player, "atharion.chat"); return; }

        User user = User.getFromUUID(player.getUniqueId());

        if(user.getMuteTimestamp() != 0)
        {

            if(user.getMuteTimestamp() != 1 && user.getMuteTimestamp() - new Date().getTime() < 0)
            {
                user.setMuteTimestamp(0);
                user.loadNameInTab();
            }
            else if(user.getMuteTimestamp() - new Date().getTime() > 1 || user.getMuteTimestamp() == 1)
            {
                String messageRefused = "";

                if(user.getMuteTimestamp() == 1)
                {
                    messageRefused = "§cVous avez été réduit au silence, veuillez contacter un modérateur sur discord §ehttps://discord.gg/KeSFqmE";
                }
                else
                {
                    messageRefused = "§cVous avez été réduit au silence pendant §e" + TimestampConverter.getTime(user.getMuteTimestamp() - new Date().getTime());
                }

                player.sendMessage(messageRefused);
                return;
            }
        }

        String message = event.getMessage().replaceAll("&", "§");

        message = "§r" + message;

        for (Player players : Bukkit.getOnlinePlayers())
        {
            if (message.toLowerCase().contains(players.getName().toLowerCase()))
            {
                String color = ChatColor.getLastColors(message.substring(0, message.toLowerCase().lastIndexOf(players.getName().toLowerCase())));
                int start = message.toLowerCase().indexOf(players.getName().toLowerCase());

                message = message.replaceAll(message.substring(start, start + players.getName().length()), ChatColor.GREEN  + players.getName() + color);

                players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f , 2);
            }
        }

      for (Player onlinePlayer : Bukkit.getOnlinePlayers())
      {
          onlinePlayer.sendMessage(ChatColor.WHITE + user.getDisplayName() + "§r" + ChatColor.GOLD + " > " + ChatColor.WHITE + message);
      }

        Main.getConsole().sendMessage(player.getName() + ": " + message);
    }
}
