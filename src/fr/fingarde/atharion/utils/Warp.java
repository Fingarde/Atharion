package fr.fingarde.atharion.utils;

import fr.fingarde.atharion.Main;
import org.bukkit.Material;

import javax.xml.stream.Location;
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

    static ArrayList<Warp> warps = new ArrayList<>();


    public Warp(String name, Location location, Material material, String description)
    {
        this.warp = name;
        this.location = location;
        this.item = material;
        this.description = description;
    }

    public String geName()
    {
        return warp;
    }

    public static void loadWarps()
    {
        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Warps");

            while (result.next())
            {


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
              if (warpArray.geName().equalsIgnoreCase(name)) { return warpArray; }
        }

        return null;
    }

}
