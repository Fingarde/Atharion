package fr.fingarde.atharion.utils;

import fr.fingarde.atharion.Main;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Warp
{
    private String warp;
    private Location location;
    private final Material item;
    private final String description;

    public static ArrayList<Warp> warps = new ArrayList<>();


    public Warp(String name, Location location, Material material, String description)
    {
        this.warp = name;
        this.location = location;
        this.item = material;
        this.description = description;
    }

    public String getName()
    {
        return warp;
    }

    public Location getLocation()
    {
        return location;
    }

    public Material getItem()
    {
        return item;
    }

    public String getDescription()
    {
        return description;
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
                Warp warp = new Warp(result.getString("name"), LocationSerializer.deserialize(result.getString("location")), (result.getString("item") == "") ? null : Material.valueOf(result.getString("item").toUpperCase()), result.getString("description"));

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
