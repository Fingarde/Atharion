package fr.fingarde.atharion.commands;

import fr.fingarde.atharion.utils.Error;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemCommand implements CommandExecutor, TabCompleter
{

    String usage = "§bUsage: §r/item §a<name|lore> §7[name|line number] [text]";
    String permission = "atharion.item";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length >= 1)
        {
            if (!(sender instanceof Player)) { Error.onlyPlayer(sender); return false; }
            if (!sender.hasPermission(permission)) { Error.noPermission(sender, permission); return false; }

            Player player = (Player) sender;

            if(player.getInventory().getItemInMainHand().getType() == Material.AIR) { player.sendMessage("§cVous devez tenir un objet en main."); return false; }

            if (args[0].equalsIgnoreCase("name"))
            {
                if (args.length < 2) { sender.sendMessage(usage); return false; }

                String name = "";
                for (int i = 1; i < args.length; i++)
                {
                    name += " " + args[i];
                }

                name = name.substring(1);
                name = name.replaceAll("&", "§");

                ItemStack itemStack = player.getInventory().getItemInMainHand();
                ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.setDisplayName("§r" + name);

                itemStack.setItemMeta(itemMeta);

                player.sendMessage("§aLe nom de votre objet a été changé en §e" + name + "§a.");
                return true;
            }
            else if (args[0].equalsIgnoreCase("lore"))
            {
                if (args.length < 2) { sender.sendMessage(usage); return false; }

                try
                {
                    int line = Integer.parseInt(args[1]);

                    String value = "";

                    for (int i = 2; i < args.length; i++)
                    {
                        value += " " + args[i];
                    }

                    if (value.length() > 1) value = value.substring(1);
                    value = value.replaceAll("&", "§");

                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    List<String> oldLore = new ArrayList<>();
                    if(itemMeta.getLore() != null)
                    {
                        oldLore = itemMeta.getLore();
                    }

                    List<String> newLore = new ArrayList<>();

                    for(int i = 0; i < (line + 1 > oldLore.size() ? line + 1 : oldLore.size()); i++ )
                    {

                        if(line == i)
                        {
                            newLore.add("§r" + value);
                        }
                        else  if(oldLore.size() > i)
                        {
                            newLore.add(oldLore.get(i));
                        }
                        else
                        {
                            newLore.add("");
                        }
                    }

                    itemMeta.setLore(newLore);
                    itemStack.setItemMeta(itemMeta);

                    player.sendMessage("§aLa ligne §e" + line + "§a de votre objet a été changé en §e" + value + "§a.");
                    return true;

                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage(usage);
                    return false;
                }
            }

            sender.sendMessage(usage);
            return false;
        }
        else
        {
            sender.sendMessage(usage);
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        List<String> value = null;

        if (args.length == 1)
        {
            value = new ArrayList<>();
            List<String> args0Completer = Arrays.asList("name", "lore");

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

