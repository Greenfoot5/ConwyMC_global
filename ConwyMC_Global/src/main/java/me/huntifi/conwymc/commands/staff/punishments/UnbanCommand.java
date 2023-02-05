package me.huntifi.conwymc.commands.staff.punishments;

import me.huntifi.conwymc.Main;
import me.huntifi.conwymc.database.Punishments;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

/**
 * Unbans a player from the server
 */
public class UnbanCommand implements CommandExecutor {

    /**
     * Unban a player
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a player is specified, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0)
            return false;

        // Attempt to unban the player asynchronously, as the database is involved
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {
            try {
                Punishments.end(args[0], "ban");
                Messenger.sendInfo("Successfully unbanned: " + ChatColor.DARK_AQUA + args[0], sender);
            } catch (SQLException e) {
                Messenger.sendError("An error occurred while trying to unban: " + ChatColor.RED + args[0], sender);
                e.printStackTrace();
            }
        });

        return true;
    }
}
