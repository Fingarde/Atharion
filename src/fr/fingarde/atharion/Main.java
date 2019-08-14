package fr.fingarde.atharion;

import com.zaxxer.hikari.HikariDataSource;
import fr.fingarde.atharion.commands.*;
import fr.fingarde.atharion.listeners.ChatListener;
import fr.fingarde.atharion.listeners.ConnectionListerner;
import fr.fingarde.atharion.listeners.ProtectionListener;
import fr.fingarde.atharion.utils.User;
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
                            "  UUID TEXT NOT NULL,\n" +
                            "  NICKNAME TEXT NOT NULL,\n" +
                            "  PREFIX TEXT NOT NULL,\n" +
                            "  SUFFIX TEXT NOT NULL,\n" +
                            "  RANK TEXT NOT NULL\n" +
                            ")");

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Warps(\n" +
                            "  NAME TEXT NOT NULL,\n" +
                            "  LOCATION TEXT NOT NULL\n" +
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

        getCommand("setrank").setExecutor(new SetRank());
        getCommand("nick").setExecutor(new Nick());
        getCommand("prefix").setExecutor(new Prefix());

        getCommand("kick").setExecutor(new Kick());
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

            user.loadPermissions();
            user.loadNameInTab();
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
