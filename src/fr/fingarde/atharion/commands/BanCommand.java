package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.objects.User;
import fr.fingarde.atharion.utils.Error;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BanCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/mute §a<player> §7[time] [message]";
    String permission = "atharion.mute";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length >= 1)
        {
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            OfflinePlayer victim = null;

            for(OfflinePlayer player : Bukkit.getOfflinePlayers())
            {
                if (player.getName().equalsIgnoreCase(args[0]))
                {
                    victim = player;
                }
            }

            if (victim == null) { sender.sendMessage(usage); return false; }

            long unbanAt = 1;
            String time = "";

            String message = "";


            boolean silent = false;

            if (args.length > 1)
            {
                for (int i = 1; i < args.length; i++)
                {
                    message +=  " " + args[i];
                }

                message = message.substring(1);
                message = message.replaceAll("&", "§");

                if (message.endsWith(" -s")) { silent = true ; message = message.substring(0, message.length() - 3); }

                if (args[1].contains("y") || args[1].contains("mo") || args[1].contains("d") || args[1].contains("h") || args[1].contains("m") || args[1].contains("s"))
                {
                    long timeMute = getTimeMute(args[1])[0];

                    if (timeMute != 0)
                    {
                        unbanAt = new Date().getTime() + timeMute;

                        if(args.length == 2) { message = ""; }
                        else
                        {
                            if (message.length() < args[1].length()) { message = ""; }
                            else { message = message.substring(args[1].length() + 1); }
                        }
                    }
                }

                long[] timeArray = getTimeMute(args[1]);

                if (timeArray[1] != 0) time += timeArray[1] + ((timeArray[1] == 1) ? "an " : "ans ");
                if (timeArray[2] != 0) time += timeArray[2] + "mois ";
                if (timeArray[3] != 0) time += timeArray[3] + ((timeArray[3] == 1) ? "semaine " : "semaines ");
                if (timeArray[4] != 0) time += timeArray[4] + ((timeArray[4] == 1) ? "jour " : "jours ");
                if (timeArray[5] != 0) time += timeArray[5] + ((timeArray[5] == 1) ? "heure " : "heures ");
                if (timeArray[6] != 0) time += timeArray[6] + ((timeArray[6] == 1) ? "minute " : "minutes ");
                if (timeArray[7] != 0) time += timeArray[7] + ((timeArray[7] == 1) ? "seconde " : "secondes ");
            }

            if(unbanAt == 1) time = "A vie ";

            if (message == "") { message = "Raison non spécifié"; }

            User user = ((victim.isOnline()) ? User.getFromUUID(victim.getUniqueId()) : new User(victim.getUniqueId()));
            user.setBanTimestamp(unbanAt);

            String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();

            if(victim.isOnline()) ((Player) victim).kickPlayer("§e" + name + "§a vous a banni d'Atharion\n§e" + time + "\n§apour\n§e" + message);

            if (!silent) { Bukkit.broadcastMessage("§e" + name + "§a a banni §e" + ((victim.isOnline()) ? ((Player) victim).getDisplayName() : victim.getName()) + "§a, §e" + time + "§apour §e" + message + "§a."); }
            else { sender.sendMessage("§aVous avez banni §e" + ((victim.isOnline()) ? ((Player) victim).getDisplayName() : victim.getName()) + "§a, §e" + time + "§apour §e" + message + "§a."); }

            return true;
        }
        else
        {
            sender.sendMessage(usage);
            return false;
        }
    }

    private long[] getTimeMute(String arg)
    {
        int years = 0, months = 0, weeks = 0, days = 0, hours = 0, minutes = 0, seconds = 0;

        Matcher mat = Pattern.compile("\\d{1,}y").matcher(arg);
        if (mat.find()) { years = Integer.parseInt(mat.group().substring(0, mat.group().length() - 1)); }

        mat = Pattern.compile("\\d{1,}mo").matcher(arg);
        if (mat.find()) { months = Integer.parseInt(mat.group().substring(0, mat.group().length() - 2)); }

        mat = Pattern.compile("\\d{1,}w").matcher(arg);
        if (mat.find()) { weeks = Integer.parseInt(mat.group().substring(0, mat.group().length() - 1)); }

        mat = Pattern.compile("\\d{1,}d").matcher(arg);
        if (mat.find()) { days = Integer.parseInt(mat.group().substring(0, mat.group().length() - 1)); }

        mat = Pattern.compile("\\d{1,}h").matcher(arg);
        if (mat.find()) { hours = Integer.parseInt(mat.group().substring(0, mat.group().length() - 1)); }

        mat = Pattern.compile("\\d{1,}m").matcher(arg);

        if(months != 0) { mat.find(); }
        if (mat.find()) { minutes = Integer.parseInt(mat.group().substring(0, mat.group().length() - 1)); }


        mat = Pattern.compile("\\d{1,}s").matcher(arg);
        if(mat.find()) seconds = Integer.parseInt(mat.group().substring(0, mat.group().length() - 1)) ;

        long totalMonths = years * 12;
        long totalDays = ((totalMonths + months) * 30) + (weeks * 7) + days;
        long totalHours = (totalDays * 24) + hours;
        long totalMinutes = (totalHours * 60) + minutes;
        long totalSeconds = (totalMinutes * 60) + seconds;

        return new long[] { (totalSeconds * 1000), years, months, weeks, days, hours, minutes, seconds };
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        List<String> value = null;

        if (args.length == 1)
        {
            value = new ArrayList<>();
            List<String> args0Completer = new ArrayList<>();

            for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                args0Completer.add(onlinePlayer.getName());
            }

            if (args[0].length() == 0)
            {
                value = args0Completer;
            }
            else
            {
                for (String args0 : args0Completer)
                {
                    if (args0.toLowerCase().startsWith(args[0].toLowerCase()))
                    {
                        value.add(args0);
                    }
                }
            }
        }

        return value;
    }
}
