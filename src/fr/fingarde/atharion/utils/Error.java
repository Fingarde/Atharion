package fr.fingarde.atharion.utils;

import org.bukkit.command.CommandSender;

public class Error
{
    public static void noPermission(CommandSender sender, String permission)
    {
        sender.sendMessage("§7[§6Atharion§7] §cVous n'avez pas la permission §e" + permission + "§c.");
    }

    public static void onlyPlayer(CommandSender sender)
    {
        sender.sendMessage("§7[§6Atharion§7] §cCette commande est uniquement utilisable par un joueur.");
    }
}
