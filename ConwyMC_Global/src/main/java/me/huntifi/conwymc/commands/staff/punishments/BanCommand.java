package me.huntifi.conwymc.commands.staff.punishments;

import me.huntifi.conwymc.Main;
import me.huntifi.conwymc.database.LoadData;
import me.huntifi.conwymc.database.Punishments;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.conwymc.util.PunishmentTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

/**
 * Bans a player from the server
 */
public class BanCommand implements CommandExecutor {

    /**
     * Ban a player.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a player, time, and reason are specified, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length < 3)
            return false;

        // Attempt to ban the player asynchronously, as the database is involved
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {
            try {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null)
                    banOffline(sender, args);
                else
                    ban(sender, player.getUniqueId(), Objects.requireNonNull(player.getAddress()).getAddress(), args);
            } catch (SQLException e) {
                Messenger.sendError("An error occurred while trying to ban: " + ChatColor.RED + args[0], sender);
                e.printStackTrace();
            }
        });

        return true;
    }

    /**
     * Get the UUID of a player that is currently offline and ban them.
     * @param sender Source of the command
     * @param args Passed command arguments
     */
    private void banOffline(CommandSender sender, String[] args) throws SQLException {
        UUID uuid = LoadData.getUUID(args[0]);
        if (uuid == null)
            Messenger.sendError("Could not find player: " + ChatColor.RED + args[0], sender);
        else
            ban(sender, uuid, null, args);
    }

    /**
     * Ban the player.
     * @param sender Source of the command
     * @param uuid Unique ID of the player to ban
     * @param args Passed command arguments
     */
    private void ban(CommandSender sender, UUID uuid, InetAddress ip, String[] args) throws SQLException {
        // Get the ban duration
        long duration = PunishmentTime.getDuration(args[1]);
        if (duration == 0) {
            PunishmentTime.wrongFormat(sender);
            return;
        }

        // Get the ban reason
        String reason = String.join(" ", args).split(" ", 3)[2];

        // Apply the ban to our database
        Punishments.add(args[0], uuid, ip, "ban", reason, duration);
        Messenger.sendInfo("Successfully banned: " + ChatColor.DARK_AQUA + args[0], sender);

        // Kick the player if they are online
        kick(uuid, reason, args[1]);
    }

    /**
     * Kick the banned player from the server.
     * @param uuid The unique ID of the player
     * @param reason The reason for the ban
     * @param duration The duration of the ban
     */
    private void kick(UUID uuid, String reason, String duration) {
        Bukkit.getScheduler().runTask(Main.getPlugin(), () -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.kickPlayer(ChatColor.DARK_RED + "\n[BAN] " + ChatColor.RED + reason
                        + ChatColor.DARK_RED + "\n[EXPIRES IN] " + ChatColor.RED + PunishmentTime.getExpire(duration));
            }
        });
    }
}
