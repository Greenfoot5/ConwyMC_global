package me.huntifi.conwymc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class that controls the global ConwyMC plugin
 */
public final class Main extends JavaPlugin {

    /** The global ConwyMC plugin */
    private static Plugin plugin;

    /** The instance of the main class */
    private static Main instance;

    @Override
    public void onEnable() {
        getLogger().info("Enabling Plugin...");

        plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyMC_Global");
        instance = this;

        getLogger().info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling Plugin...");

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
}
