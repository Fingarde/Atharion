package fr.fingarde.atharion;

import com.zaxxer.hikari.HikariDataSource;
import fr.fingarde.atharion.commands.*;
import fr.fingarde.atharion.listeners.ChatListener;
import fr.fingarde.atharion.listeners.ConnectionListerner;
import fr.fingarde.atharion.listeners.ProtectionListener;
import fr.fingarde.atharion.objects.User;
import fr.fingarde.atharion.objects.Warp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/*TODO
 BAN = TP DANS
 Prem pour parler
 Warps
 Ban in DB + WARNS


 */

public class Main extends JavaPlugin
{
    private static Main instance;
    private static HikariDataSource hikari;
    private static ConsoleCommandSender console = Bukkit.getConsoleSender();

    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        connectDatabase();
        createTables();

        registerCommands();
        registerListeners();

        restorePlayers();
        Warp.loadWarps();
    }

    private void connectDatabase()
    {
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", getConfig().getString("host"));
        hikari.addDataSourceProperty("port", getConfig().getInt("port"));
        hikari.addDataSourceProperty("databaseName", getConfig().getString("database"));
        hikari.addDataSourceProperty("user", getConfig().getString("user"));
        hikari.addDataSourceProperty("password", getConfig().getString("password"));

        hikari.addDataSourceProperty("allowPublicKeyRetrieval",true);
        hikari.addDataSourceProperty("verifyServerCertificate", false);
        hikari.addDataSourceProperty("useSSL", false);

        hikari.addDataSourceProperty("tcpKeepAlive", true);
        hikari.addDataSourceProperty("autoReconnect", true);
        hikari.addDataSourceProperty("connectTimeout", 300);

        hikari.addDataSourceProperty("characterEncoding","utf8");
        hikari.addDataSourceProperty("useUnicode","true");

        hikari.setMaximumPoolSize(2147483647);
        hikari.setMinimumIdle(0);
        hikari.setIdleTimeout(300);
    }

    private void createTables()
    {
        try
        {
            Connection connection = hikari.getConnection();
            Statement statement = connection.createStatement();

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Players(\n" +
                            "  id MEDIUMINT NOT NULL AUTO_INCREMENT,\n" +
                            "  uuid TEXT NOT NULL,\n" +
                            "  rank TEXT NOT NULL,\n" +
                            "  nickname TEXT NOT NULL,\n" +
                            "  prefix TEXT NOT NULL,\n" +
                            "  suffix TEXT NOT NULL,\n" +
                            "  joined_timestamp BIGINT NOT NULL,\n" +
                            "  muted_timestamp BIGINT NOT NULL,\n" +
                            "  jailed_timestamp BIGINT NOT NULL,\n" +
                            "  banned_timestamp BIGINT NOT NULL,\n" +
                            "  primary KEY (id)\n" +
                            ")");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Warps(\n" +
                            "  id MEDIUMINT NOT NULL AUTO_INCREMENT,\n" +
                            "  name TEXT NOT NULL,\n" +
                            "  location TEXT NOT NULL,\n" +
                            "  item TEXT NOT NULL,\n" +
                            "  primary KEY (id)\n" +
                            ")");

            statement.close();
            connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            console.sendMessage(ChatColor.RED + "[Atharion] ERROR: Impossible d'atteindre la base de données !");
            console.sendMessage(ChatColor.GOLD + "[Atharion] INFO: Le plugin se désactive automatiquement.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void registerCommands()
    {
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("gamemode").setTabCompleter(new GamemodeCommand());

        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("fly").setTabCompleter(new FlyCommand());

        getCommand("s").setExecutor(new SummonCommand());
        getCommand("s").setTabCompleter(new SummonCommand());

        getCommand("kick").setExecutor(new KickCommand());
        getCommand("kick").setTabCompleter(new KickCommand());

        getCommand("setrank").setExecutor(new RankCommand());
        getCommand("setrank").setTabCompleter(new RankCommand());

        getCommand("nick").setExecutor(new NickCommand());
        getCommand("nick").setTabCompleter(new NickCommand());

        getCommand("prefix").setExecutor(new PrefixCommand());
        getCommand("prefix").setTabCompleter(new PrefixCommand());

        getCommand("suffix").setExecutor(new SuffixCommand());
        getCommand("suffix").setTabCompleter(new SuffixCommand());

        getCommand("item").setExecutor(new ItemCommand());
        getCommand("item").setTabCompleter(new ItemCommand());

        getCommand("createwarp").setExecutor(new CreateWarpCommand());
        getCommand("setwarpitem").setExecutor(new SetWarpItemCommand());

        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("warp").setTabCompleter(new WarpCommand());

        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("mute").setTabCompleter(new MuteCommand());

        getCommand("unmute").setExecutor(new UnmuteCommand());
        getCommand("unmute").setTabCompleter(new UnmuteCommand());

        getCommand("ban").setExecutor(new BanCommand());
        getCommand("ban").setTabCompleter(new BanCommand());

        getCommand("unban").setExecutor(new UnbanCommand());
        getCommand("unban").setTabCompleter(new UnbanCommand());

    }

    private void registerListeners()
    {
        getServer().getPluginManager().registerEvents(new ConnectionListerner(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(), this);
    }

    private void restorePlayers()
    {
        for(Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            User user = new User(onlinePlayer.getUniqueId());

            user.loadName();
            user.loadPermissions();
            user.loadNameInTab();

            User.users.add(user);
        }
    }


    public static HikariDataSource getHikari()
    {
        return hikari;
    }

    public static Main getInstance()
    {
        return instance;
    }
}
