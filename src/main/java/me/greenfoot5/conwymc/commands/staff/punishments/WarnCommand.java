package me.greenfoot5.conwymc.commands.staff.punishments;

import me.greenfoot5.conwymc.ConwyMC;
import me.greenfoot5.conwymc.database.LoadData;
import me.greenfoot5.conwymc.database.Punishments;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Warns a player
 */
public class WarnCommand implements CommandExecutor {

    /**
     * Warn a player.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return Whether a player and reason are supplied
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2)
            return false;

        // Attempt to warn the player asynchronously, as the database is involved
        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> {
            try {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null)
                    warnOffline(sender, args);
                else
                    warn(sender, player.getUniqueId(), args);
            } catch (SQLException e) {
                Messenger.sendError("An error occurred while trying to warn: <red>" + args[0], sender);
                e.printStackTrace();
            }
        });

        return true;
    }

    /**
     * Get the UUID of a player that is currently offline and warn them.
     * @param sender Source of the command
     * @param args Passed command arguments
     */
    private void warnOffline(CommandSender sender, String[] args) throws SQLException {
        UUID uuid = LoadData.getUUID(args[0]);
        if (uuid == null) {
            Messenger.sendError("Could not find player: <red>" + args[0], sender);
        } else {
            warn(sender, uuid, args);
        }
    }

    /**
     * Warn the player.
     * @param sender Source of the command
     * @param uuid Unique ID of the player to mute
     * @param args Passed command arguments
     */
    private void warn(CommandSender sender, UUID uuid, String[] args) throws SQLException {
        // Get the warning reason
        String reason = String.join(" ", args).split(" ", 2)[1];

        // Apply the warning to our database
        Punishments.add(args[0], uuid, null, "warn", reason, 0);
        warnOnline(uuid, reason);
        Messenger.sendInfo("Successfully warned: <aqua>" + args[0], sender);
    }

    /**
     * Warn the online player.
     * @param uuid The unique ID of the player
     * @param reason The reason for the warning
     */
    private void warnOnline(UUID uuid, String reason) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
            Messenger.sendError("You were warned for: <red>" + reason, player);
    }
}
