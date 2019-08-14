package fr.fingarde.atharion.listeners;

import fr.fingarde.atharion.utils.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        User user = User.getFromUUID(player.getUniqueId());

        event.setCancelled(true);

        String message = event.getMessage().replaceAll("&", "§");

        message = "§r" + message;

        for(Player players : Bukkit.getOnlinePlayers())
        {
            if(message.toLowerCase().contains(players.getName().toLowerCase()))
            {
                String color = ChatColor.getLastColors(message.substring(0, message.toLowerCase().lastIndexOf(players.getName().toLowerCase())));
                int start = message.toLowerCase().indexOf(players.getName().toLowerCase());

                message = message.replaceAll(message.substring(start, start + players.getName().length()), ChatColor.GREEN  + players.getName() + color);

                players.playSound(players.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f , 2);
            }
        }

      for(Player onlinePlayer : Bukkit.getOnlinePlayers())
      {
          onlinePlayer.sendMessage(ChatColor.WHITE + user.getDisplayName() + "§r" + ChatColor.GOLD + " > " + ChatColor.WHITE + message);
      }

    }
}