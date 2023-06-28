package me.huntifi.conwymc.commands.staff.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Broadcasts a message
 */
public class BroadcastCommand implements CommandExecutor {

    /**
     * Send a message to all players on the server.
     * <b>TODO: Send the message to all servers in the network</b>
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0)
            return false;

        String broadcastPrefix = "§2[§4ConwyMC§2] - §a";

        Bukkit.broadcastMessage(broadcastPrefix + ChatColor.translateAlternateColorCodes('&', String.join(" ", args)));
        return true;
    }
}
