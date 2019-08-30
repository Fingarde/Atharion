package fr.fingarde.atharion.objects;

import fr.fingarde.atharion.Main;
import fr.fingarde.atharion.utils.ItemSerializer;
import fr.fingarde.atharion.utils.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Warp
{
    private String warp;
    private Location location;
    private final ItemStack item;

    public static ArrayList<Warp> warps = new ArrayList<>();


    public Warp(String name, Location location, ItemStack item)
    {
        this.warp = name;
        this.location = location;
        this.item = item;
    }

    public String getName()
    {
        return warp;
    }

    public Location getLocation()
    {
        return location;
    }

    public ItemStack getItem()
    {
        return item;
    }

    public static void loadWarps()
    {
        warps.clear();

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Warps");

            while (result.next())
            {
                Warp warp = new Warp(result.getString("name"), LocationSerializer.deserialize(result.getString("location")), (result.getString("item") == "") ? null : ItemSerializer.deserializeItem(result.getString("item")));

                warps.add(warp);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public static Warp getByName(String name)
    {
        for (Warp warpArray : warps)
        {
              if (warpArray.getName().equalsIgnoreCase(name)) { return warpArray; }
        }

        return null;
    }
}
