package me.huntifi.conwymc;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.huntifi.conwymc.commands.chat.GlobalChatCommand;
import me.huntifi.conwymc.commands.chat.MessageCommand;
import me.huntifi.conwymc.commands.chat.ReplyCommand;
import me.huntifi.conwymc.commands.donator.JoinMessageCommand;
import me.huntifi.conwymc.commands.donator.LeaveMessageCommand;
import me.huntifi.conwymc.commands.info.*;
import me.huntifi.conwymc.commands.staff.SetStaffRankCommand;
import me.huntifi.conwymc.commands.staff.chat.StaffChatCommand;
import me.huntifi.conwymc.commands.staff.punishments.*;
import me.huntifi.conwymc.database.KeepAlive;
import me.huntifi.conwymc.database.MySQL;
import me.huntifi.conwymc.database.StoreData;
import me.huntifi.conwymc.events.chat.PlayerChat;
import me.huntifi.conwymc.events.connection.PlayerConnect;
import me.huntifi.conwymc.events.connection.PlayerDisconnect;
import me.huntifi.conwymc.events.nametag.UpdateNameTag;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

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

        // Connect to the database and register events, commands, and timed tasks
        sqlConnect();
        registerEvents();
        registerCommands();
        registerTimedTasks();

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

    /**
     * Register all events.
     */
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerChat(), plugin);
        getServer().getPluginManager().registerEvents(new PlayerConnect(), plugin);
        getServer().getPluginManager().registerEvents(new PlayerDisconnect(), plugin);
        getServer().getPluginManager().registerEvents(new UpdateNameTag(), plugin);
    }

    /**
     * Register all commands.
     */
    private void registerCommands() {
        // Chat
        Objects.requireNonNull(getCommand("GlobalChat")).setExecutor(new GlobalChatCommand());
        Objects.requireNonNull(getCommand("Message")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("Reply")).setExecutor(new ReplyCommand());

        // Donator
        Objects.requireNonNull(getCommand("JoinMessage")).setExecutor(new JoinMessageCommand());
        Objects.requireNonNull(getCommand("LeaveMessage")).setExecutor(new LeaveMessageCommand());

        // Info
        Objects.requireNonNull(getCommand("Discord")).setExecutor(new DiscordCommand());
        Objects.requireNonNull(getCommand("Ping")).setExecutor(new PingCommand());
        Objects.requireNonNull(getCommand("Rules")).setExecutor(new RulesCommand());
        Objects.requireNonNull(getCommand("TopDonators")).setExecutor(new TopDonatorsCommand());
        Objects.requireNonNull(getCommand("WebShop")).setExecutor(new WebShopCommand());
        Objects.requireNonNull(getCommand("WhoIs")).setExecutor(new WhoIsCommand());

        // Staff - Chat
        Objects.requireNonNull(getCommand("StaffChat")).setExecutor(new StaffChatCommand());

        // Staff - Punishments
        Objects.requireNonNull(getCommand("Ban")).setExecutor(new BanCommand());
        Objects.requireNonNull(getCommand("Kick")).setExecutor(new KickCommand());
        Objects.requireNonNull(getCommand("KickAll")).setExecutor(new KickAllCommand());
        Objects.requireNonNull(getCommand("Mute")).setExecutor(new MuteCommand());
        Objects.requireNonNull(getCommand("Unban")).setExecutor(new UnbanCommand());
        Objects.requireNonNull(getCommand("Unmute")).setExecutor(new UnmuteCommand());
        Objects.requireNonNull(getCommand("Warn")).setExecutor(new WarnCommand());

        // Staff - Other
        Objects.requireNonNull(getCommand("SetStaffRank")).setExecutor(new SetStaffRankCommand());
    }

    /**
     * Register all times tasks.
     */
    private void registerTimedTasks() {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new KeepAlive(), 0, 5900);
    }
}
