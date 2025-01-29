package me.greenfoot5.conwymc.commands.staff.punishments;

import me.greenfoot5.conwymc.ConwyMC;
import me.greenfoot5.conwymc.database.ActiveData;
import me.greenfoot5.conwymc.database.Punishments;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

/**
 * Unmutes a player
 */
public class UnmuteCommand implements CommandExecutor {

    /**
     * Unmute a player.
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

        // Attempt to unmute the player asynchronously, as the database is involved
        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> {
            try {
                Punishments.end(args[0], "mute");
                unmuteOnline(args[0]);
                Messenger.sendInfo("Successfully unmuted: <aqua>" + args[0], sender);
            } catch (SQLException e) {
                Messenger.sendError("An error occurred while trying to unmute: <red>" + args[0], sender);
                e.printStackTrace();
            }
        });

        return true;
    }

    /**
     * Unmute the online player.
     * @param name The name of the player
     */
    private void unmuteOnline(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            ActiveData.getData(player.getUniqueId()).setMute(null, null);
            Messenger.sendInfo("Your mute has been removed!", player);
        }
    }
}
