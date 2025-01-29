package me.greenfoot5.conwymc;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.greenfoot5.conwymc.commands.CosmeticsCommand;
import me.greenfoot5.conwymc.commands.chat.GlobalChatCommand;
import me.greenfoot5.conwymc.commands.chat.MessageCommand;
import me.greenfoot5.conwymc.commands.chat.ReplyCommand;
import me.greenfoot5.conwymc.commands.donator.JoinMessageCommand;
import me.greenfoot5.conwymc.commands.donator.LeaveMessageCommand;
import me.greenfoot5.conwymc.commands.info.CoinMultiplierCommand;
import me.greenfoot5.conwymc.commands.info.CoinsCommand;
import me.greenfoot5.conwymc.commands.info.DiscordCommand;
import me.greenfoot5.conwymc.commands.info.PingCommand;
import me.greenfoot5.conwymc.commands.info.RulesCommand;
import me.greenfoot5.conwymc.commands.info.TopBoostersCommand;
import me.greenfoot5.conwymc.commands.info.TopDonatorsCommand;
import me.greenfoot5.conwymc.commands.info.WebShopCommand;
import me.greenfoot5.conwymc.commands.staff.FlyCommand;
import me.greenfoot5.conwymc.commands.staff.chat.BroadcastCommand;
import me.greenfoot5.conwymc.commands.staff.chat.StaffChatCommand;
import me.greenfoot5.conwymc.commands.staff.currencies.AddCoinsCommand;
import me.greenfoot5.conwymc.commands.staff.currencies.SetCoinMultiplierCommand;
import me.greenfoot5.conwymc.commands.staff.currencies.SetCoinsCommand;
import me.greenfoot5.conwymc.commands.staff.currencies.TakeCoinsCommand;
import me.greenfoot5.conwymc.commands.staff.punishments.BanCommand;
import me.greenfoot5.conwymc.commands.staff.punishments.KickAllCommand;
import me.greenfoot5.conwymc.commands.staff.punishments.KickCommand;
import me.greenfoot5.conwymc.commands.staff.punishments.MuteCommand;
import me.greenfoot5.conwymc.commands.staff.punishments.UnbanCommand;
import me.greenfoot5.conwymc.commands.staff.punishments.UnmuteCommand;
import me.greenfoot5.conwymc.commands.staff.punishments.WarnCommand;
import me.greenfoot5.conwymc.commands.staff.rank.RankPointsCommand;
import me.greenfoot5.conwymc.commands.staff.rank.SetStaffRankCommand;
import me.greenfoot5.conwymc.commands.staff.rank.ToggleRankCommand;
import me.greenfoot5.conwymc.commands.staff.support.SupportCommand;
import me.greenfoot5.conwymc.database.KeepAlive;
import me.greenfoot5.conwymc.database.MySQL;
import me.greenfoot5.conwymc.database.StoreData;
import me.greenfoot5.conwymc.events.TabComplete;
import me.greenfoot5.conwymc.events.connection.PlayerConnect;
import me.greenfoot5.conwymc.events.connection.PlayerDisconnect;
import me.greenfoot5.conwymc.events.nametag.UpdateNameTag;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * The main class that controls the global ConwyMC plugin
 */
public final class ConwyMC extends JavaPlugin {

    /** The global ConwyMC plugin */
    public static Plugin plugin;

    /** The instance of the main class */
    public static ConwyMC instance;

    /** The instance that handles the database connection */
    public static MySQL SQL;

    @Override
    public void onEnable() {
        getLogger().info("Enabling Plugin...");

        // Set important global variables
        plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyMC");
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
     * Register all events.
     */
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerConnect(), plugin);
        getServer().getPluginManager().registerEvents(new PlayerDisconnect(), plugin);
        getServer().getPluginManager().registerEvents(new UpdateNameTag(), plugin);
        getServer().getPluginManager().registerEvents(new GlobalChatCommand(), plugin);
        getServer().getPluginManager().registerEvents(new StaffChatCommand(), plugin);
        getServer().getPluginManager().registerEvents(new TabComplete(), plugin);
    }

    /**
     * Register all commands.
     */
    private void registerCommands() {
        // Chat
        Objects.requireNonNull(getCommand("GlobalChat")).setExecutor(new GlobalChatCommand());
        Objects.requireNonNull(getCommand("Message")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("Reply")).setExecutor(new ReplyCommand());

        // Support
        Objects.requireNonNull(getCommand("Support")).setExecutor(new SupportCommand());

        // Donator
        Objects.requireNonNull(getCommand("JoinMessage")).setExecutor(new JoinMessageCommand());
        Objects.requireNonNull(getCommand("LeaveMessage")).setExecutor(new LeaveMessageCommand());

        // Cosmetics
        Objects.requireNonNull(getCommand("Cosmetics")).setExecutor(new CosmeticsCommand());

        // Info
        Objects.requireNonNull(getCommand("Coins")).setExecutor(new CoinsCommand());
        Objects.requireNonNull(getCommand("CoinMultiplier")).setExecutor(new CoinMultiplierCommand());
        Objects.requireNonNull(getCommand("Discord")).setExecutor(new DiscordCommand());
        Objects.requireNonNull(getCommand("Ping")).setExecutor(new PingCommand());
        Objects.requireNonNull(getCommand("Rules")).setExecutor(new RulesCommand());
        Objects.requireNonNull(getCommand("TopBoosters")).setExecutor(new TopBoostersCommand());
        Objects.requireNonNull(getCommand("TopDonators")).setExecutor(new TopDonatorsCommand());
        Objects.requireNonNull(getCommand("MonthlyBoosters")).setExecutor(new TopBoostersCommand());
        Objects.requireNonNull(getCommand("WebShop")).setExecutor(new WebShopCommand());

        // Staff - Chat
        Objects.requireNonNull(getCommand("Broadcast")).setExecutor(new BroadcastCommand());
        Objects.requireNonNull(getCommand("StaffChat")).setExecutor(new StaffChatCommand());

        // Staff - Currencies
        Objects.requireNonNull(getCommand("AddCoins")).setExecutor(new AddCoinsCommand());
        Objects.requireNonNull(getCommand("SetCoins")).setExecutor(new SetCoinsCommand());
        Objects.requireNonNull(getCommand("SetCoinMultiplier")).setExecutor(new SetCoinMultiplierCommand());
        Objects.requireNonNull(getCommand("TakeCoins")).setExecutor(new TakeCoinsCommand());

        // Staff - Punishments
        Objects.requireNonNull(getCommand("Ban")).setExecutor(new BanCommand());
        Objects.requireNonNull(getCommand("Kick")).setExecutor(new KickCommand());
        Objects.requireNonNull(getCommand("KickAll")).setExecutor(new KickAllCommand());
        Objects.requireNonNull(getCommand("Mute")).setExecutor(new MuteCommand());
        Objects.requireNonNull(getCommand("Unban")).setExecutor(new UnbanCommand());
        Objects.requireNonNull(getCommand("Unmute")).setExecutor(new UnmuteCommand());
        Objects.requireNonNull(getCommand("Warn")).setExecutor(new WarnCommand());

        // Staff - Rank
        Objects.requireNonNull(getCommand("RankPoints")).setExecutor(new RankPointsCommand());
        Objects.requireNonNull(getCommand("SetStaffRank")).setExecutor(new SetStaffRankCommand());
        Objects.requireNonNull(getCommand("ToggleRank")).setExecutor(new ToggleRankCommand());

        // Staff - Other
        Objects.requireNonNull(getCommand("Fly")).setExecutor(new FlyCommand());
    }

    /**
     * Register all times tasks.
     */
    private void registerTimedTasks() {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new KeepAlive(), 0, 5900);
    }
}
