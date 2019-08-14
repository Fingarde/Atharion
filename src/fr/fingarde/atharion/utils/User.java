package fr.fingarde.atharion.utils;

import fr.fingarde.atharion.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.Team;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

public class User
{
    private static ArrayList<User> users = new ArrayList<>();

    private UUID uuid;

    private Rank rank;
    private String nickname;
    private String prefix;
    private String suffix;
    private String displayName;
    private Player player;

    public User(UUID uuid)
    {
        this.uuid = uuid;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Players WHERE UUID = '" + this.uuid.toString() + "'");

            if(!result.next()) return;

            this.rank = new Rank(result.getString("RANK"));
            this.nickname = result.getString("NICKNAME");
            this.prefix = result.getString("PREFIX");
            this.suffix = result.getString("SUFFIX");

            this.player = Bukkit.getPlayer(this.uuid);

            loadName();
            loadNameInTab();

            statement.close();
            connection.close();

            users.add(this);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public UUID getUUID()
    {
        return this.uuid;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Rank getRank()
    {
        return rank;
    }

    public void setRank(Rank rank)
    {
        this.rank = rank;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE Players SET RANK = '" + this.rank.getName() + "' WHERE UUID = '" + this.uuid + "'");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadName();
        loadNameInTab();
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE Players SET PREFIX = '" + this.prefix + "' WHERE UUID = '" + this.uuid + "'");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadName();
        loadNameInTab();
    }

    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(String suffix)
    {
        this.suffix = suffix;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE Players SET SUFFIX = '" + this.suffix + "' WHERE UUID = '" + this.uuid + "'");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadName();
        loadNameInTab();
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE Players SET NICKNAME = '" + this.nickname + "' WHERE UUID = '" + this.uuid + "'");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadName();
        loadNameInTab();
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void loadPermissions()
    {
        if(this.rank.getPermissions() != null)
        {
            PermissionAttachment attachment = this.player.addAttachment(Main.getInstance());

            for(String permission : this.rank.getPermissions())
            {
                if(permission.startsWith("-"))
                {
                    permission = permission.substring(1);
                    attachment.setPermission(permission, false);
                }
                else
                {
                    attachment.setPermission(permission, true);
                }
            }
        }
    }

    public void loadNameInTab()
    {
        if(this.rank.getTeamName() != null)
        {
            try
            {
                Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(this.rank.getTeamName());
            }
            catch (IllegalArgumentException e) { }

            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(this.rank.getTeamName());
            team.addEntry(this.player.getName());
        }

        this.player.setPlayerListName(this.displayName);
    }


    private void loadName()
    {
        if(this.nickname != "") this.player.setDisplayName(this.nickname);

        String localPrefix = "";
        String localSuffix = "";

        if(this.rank.getPrefix() != null) localPrefix = this.rank.getPrefix() + " ";
        if(this.rank.getSuffix()!= null) localSuffix =  " " + this.rank.getSuffix();

        if(this.prefix != "")  localPrefix = this.prefix + " ";
        if(this.suffix != "")  localSuffix = " " + this.suffix;


        this.displayName = localPrefix + player.getDisplayName() + localSuffix;
    }

    public static User getFromUUID(UUID uuid)
    {
        for(User userInArray : users)
        {
            if(userInArray.getUUID() == uuid) return userInArray;
        }

        return null;
    }
}