package fr.fingarde.atharion.utils;

import fr.fingarde.atharion.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Rank
{
    private String rank;
    private MemorySection data;

    public Rank(String rank)
    {
        File file = getConfig();

        YamlConfiguration config = new YamlConfiguration();

        try
        {
            config.load(file);

            if(!config.contains(rank))
            {
                return;
            }

            this.rank = rank;
            this.data = (MemorySection) config.get(rank);
        }
        catch (InvalidConfigurationException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getPrefix()
    {
        if(data.get("prefix") == null) return null;
        return (String) data.get("prefix");
    }

    public String getSuffix()
    {
        if(data.get("suffix") == null) return null;
        return (String) data.get("suffix");
    }

    public String getTeamName()
    {
        if(data.get("placeintab") == null) return "999_" + rank;
        return data.getInt("placeintab") + "_" + rank;
    }

    public ArrayList<String> getPermissions()
    {
        ArrayList<String> permissions = new ArrayList<>();

        if(data.get("permissions") != null)
        {
            permissions =  (ArrayList<String>) data.get("permissions");
        }

        if(getInherit() != null)
        {
            for(Rank inherit : getInherit())
            {
                permissions.addAll(inherit.getPermissions());
            }
        }

        return permissions;
    }

    public String getName()
    {
        return rank;
    }

    private ArrayList<Rank> getInherit()
    {
        ArrayList<Rank> inheritRanks = new ArrayList<>();

        if(data.get("inherit") == null) return null;

        for(String inherit : (ArrayList<String>) data.get("inherit"))
        {
            inheritRanks.add(new Rank(inherit));
        }

        if(inheritRanks.size() == 0) return null;
        return inheritRanks;
    }

    public boolean isNull()
    {
        if(this.rank == null) { return true; }
        else { return false; }
    }

    public File getConfig() {
        File file = new File(Main.getInstance().getDataFolder(), "group.yml");

        return file;
    }

    public static ArrayList<String> getRanks()
    {
        File file = new File(Main.getInstance().getDataFolder(), "group.yml");

        YamlConfiguration config = new YamlConfiguration();

        try
        {
            config.load(file);
            return new ArrayList<String>(config.getKeys(false));
        }
        catch (InvalidConfigurationException | IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
