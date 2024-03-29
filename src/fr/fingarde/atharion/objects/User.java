package fr.fingarde.atharion.objects;

import fr.fingarde.atharion.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scoreboard.Team;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

public class User
{
    public static ArrayList<User> users = new ArrayList<>();

    private UUID uuid;

    private Rank rank;
    private String nickname;
    private String prefix;
    private String suffix;
    private String displayName;
    private Player player;
    private long joinTimestamp;
    private long muteTimestamp;
    private long jailTimestamp;
    private long banTimestamp;

    public User(UUID uuid)
    {
        this.uuid = uuid;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Players WHERE uuid = '" + this.uuid.toString() + "'");

            if (!result.next()) { return; }

            this.rank = new Rank(result.getString("rank"));
            this.nickname = result.getString("nickname");
            this.prefix = result.getString("prefix");
            this.suffix = result.getString("suffix");

            this.joinTimestamp = result.getLong("joined_timestamp");

            this.muteTimestamp = result.getLong("muted_timestamp");
            this.jailTimestamp = result.getLong("jailed_timestamp");
            this.banTimestamp = result.getLong("banned_timestamp");

            this.player = Bukkit.getPlayer(this.uuid);

            statement.close();
            connection.close();
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

            statement.executeUpdate("UPDATE Players SET rank = '" + this.rank.getName() + "' WHERE uuid = '" + this.uuid + "'");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadPermissions();
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

            statement.executeUpdate("UPDATE Players SET prefix = '" + this.prefix.replaceAll("'", "\\\\'") + "' WHERE uuid = '" + this.uuid + "'");

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

            statement.executeUpdate("UPDATE Players SET suffix = '" + this.suffix.replaceAll("'", "\\\\'") + "' WHERE uuid = '" + this.uuid + "'");


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

            statement.executeUpdate("UPDATE Players SET nickname = '" + this.nickname.replaceAll("'", "\\\\'") + "' WHERE uuid = '" + this.uuid + "'");

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
        if(!player.isOp())
        {
            PermissionAttachment removeAtachement = this.player.addAttachment(Main.getInstance());

            for(PermissionAttachmentInfo s : player.getEffectivePermissions())
            {
                removeAtachement.setPermission(s.getPermission(), false);
            }
        }

        if (this.rank.getPermissions() != null)
        {
            PermissionAttachment attachment = this.player.addAttachment(Main.getInstance());

            for (String permission : this.rank.getPermissions())
            {
                if (permission.startsWith("-"))
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
        if (this.rank.getTeamName() != null)
        {
            try
            {
                Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(this.rank.getTeamName());
            }
            catch (IllegalArgumentException e) { }

            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(this.rank.getTeamName());
            team.addEntry(this.player.getName());
        }

        /*try
        {
            Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("noCollision").setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
        catch (IllegalArgumentException e) { }

        Team noCollisionTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("noCollision");

        noCollisionTeam.addEntry(this.player.getName());*/

        this.player.setPlayerListName(((this.muteTimestamp > 0) ? "§7[§8Mute§7] " : "") + this.displayName);
    }


    public void loadName()
    {
        this.player.setDisplayName(this.player.getName());
        if (this.nickname != "" && this.player.hasPermission("atharion.havenickname")) { this.player.setDisplayName(this.nickname); }

        String localPrefix = "";
        String localSuffix = "";

        if (this.rank.getPrefix() != null) { localPrefix = this.rank.getPrefix() + " " + "§r"; }
        if (this.rank.getSuffix() != null) { localSuffix =  "§r" + " " + this.rank.getSuffix(); }

        if (this.prefix != "" && this.player.hasPermission("atharion.haveprefix")) { localPrefix = ((this.prefix.length() == 2 && this.prefix.startsWith("§")) ? this.prefix : (this.prefix + " ")) + "§r"; }
        if (this.suffix != "" && this.player.hasPermission("atharion.havesuffix")) { localSuffix =  "§r" + ((this.suffix.length() == 2 && this.prefix.startsWith("§")) ? this.suffix : (" " + this.suffix)); }

        this.displayName = localPrefix + player.getDisplayName() + localSuffix;
    }

    public long getJoinTimestamp()
    {
        return joinTimestamp;
    }

    public long getMuteTimestamp()
    {
        return muteTimestamp;
    }

    public void setMuteTimestamp(long timestamp)
    {
        this.muteTimestamp = timestamp;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE Players SET muted_timestamp = '" + this.muteTimestamp + "' WHERE uuid = '" + this.uuid + "'");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getJailTimestamp()
    {
        return jailTimestamp;
    }

    public void setJailTimestamp(long timestamp)
    {
        this.jailTimestamp = timestamp;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE Players SET jailed_timestamp = '" + this.jailTimestamp + "' WHERE uuid = '" + this.uuid + "'");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getBanTimestamp()
    {
        return banTimestamp;
    }

    public void setBanTimestamp(long timestamp)
    {
        this.banTimestamp = timestamp;

        try
        {
            Connection connection = Main.getHikari().getConnection();

            Statement statement = connection.createStatement();

            statement.executeUpdate("UPDATE Players SET banned_timestamp = '" + this.banTimestamp + "' WHERE uuid = '" + this.uuid + "'");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getFromUUID(UUID uuid)
    {
        for (User userInArray : users)
        {
            if (userInArray.getUUID() == uuid) { return userInArray; }
        }

        return null;
    }
}
