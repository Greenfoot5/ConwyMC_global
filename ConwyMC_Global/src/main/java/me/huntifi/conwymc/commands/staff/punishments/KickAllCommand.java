package me.huntifi.conwymc.commands.staff.punishments;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Kicks all players from the server
 */
public class KickAllCommand implements CommandExecutor {

    /**
     * Kick all players.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        String message = ChatColor.RED + "You were kicked by " + ChatColor.WHITE + sender.getName();
        if (args.length > 1)
            message += ChatColor.RED + " for: " + String.join(" ", args);

        for (Player player : Bukkit.getOnlinePlayers())
            player.kickPlayer(message);

        return true;
    }
}