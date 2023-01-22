package me.huntifi.conwymc;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.huntifi.conwymc.database.KeepAlive;
import me.huntifi.conwymc.database.MySQL;
import me.huntifi.conwymc.database.StoreData;
import me.huntifi.conwymc.events.connection.PlayerConnect;
import me.huntifi.conwymc.events.connection.PlayerDisconnect;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * The main class that controls the global ConwyMC plugin
 */
public final class Main extends JavaPlugin {

    /** The global ConwyMC plugin */
    private static Plugin plugin;

    /** The instance of the main class */
    private static Main instance;

    /** The instance that handles the database connection */
    private static MySQL SQL;

    @Override
    public void onEnable() {
        getLogger().info("Enabling Plugin...");

        // Set important global variables
        plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyMC_Global");
        instance = this;

        // Connect to the database
        sqlConnect();

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerConnect(), plugin);
        getServer().getPluginManager().registerEvents(new PlayerDisconnect(), plugin);

        // Register timed tasks
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new KeepAlive(), 0, 5900);

        getLogger().info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling Plugin...");

        // Unregister all listeners and stop running timed tasks
        HandlerList.unregisterAll(plugin);
        Bukkit.getServer().getScheduler().cancelTasks(plugin);

        // Save data and disconnect from the database
        StoreData.storeAll();
        try {
            SQL.disconnect();
        } catch (SQLException | NullPointerException e) {
            getLogger().warning("The database connection could not be closed because it does not exist!");
        }

        getLogger().info("Plugin has been disabled!");
    }

    /**
     * Get the global ConwyMC plugin.
     * @return The global ConwyMC plugin
     */
    public static Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get the instance of the main class.
     * @return The main instance
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * Connect to the SQL database.
     */
    private void sqlConnect() {
        try {
            // Get the database information
            YamlDocument dbConfig = YamlDocument.create(new File(getDataFolder(), "database.yml"));
            String host = dbConfig.getString("host");
            int port = dbConfig.getInt("port");
            String database = dbConfig.getString("database");
            String username = dbConfig.getString("username");
            String password = dbConfig.getString("password");

            // Connect to the database
            SQL = new MySQL(host, port, database, username, password);
            SQL.connect();
            getLogger().info("<!> Database is connected! <!>");
        } catch (IOException | SQLException e) {
            // Connecting to the database failed
            getLogger().warning("<!> Database is not connected! <!>");
        }
    }

    /**
     * Get the database connection.
     * @return The connection to the database
     * @throws SQLException If a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return SQL.getConnection();
    }
}
