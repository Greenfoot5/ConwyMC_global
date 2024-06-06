package me.huntifi.conwymc.commands.staff.rank;

import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.database.LoadData;
import me.huntifi.conwymc.database.Permissions;
import me.huntifi.conwymc.database.StoreData;
import me.huntifi.conwymc.events.nametag.UpdateNameTagEvent;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.conwymc.util.RankPoints;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Manages a player's rank points
 */
public class RankPointsCommand implements CommandExecutor {

    /**
     * Set, add to, or remove from the player's rank points
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return whether valid arguments were supplied
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length != 3)
            return false;

        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> {
            try {
                // Get the player's stored rank points
                double rp = LoadData.getRankPoints(args[0]);
                if (rp < 0) {
                    Messenger.sendError(String.format("Could not find player: <red>%s", args[0]), sender);
                    return;
                }

                // Apply the provided operation with the corresponding amount
                switch (args[1].toLowerCase()) {
                    case "set":
                        setRankPoints(sender, args[0], Math.max(Double.parseDouble(args[2]), 0));
                        break;
                    case "add":
                        setRankPoints(sender, args[0], Math.max(rp + Double.parseDouble(args[2]), 0));
                        break;
                    case "remove":
                        setRankPoints(sender, args[0], Math.max(rp - Double.parseDouble(args[2]), 0));
                        break;
                    default:
                        Messenger.sendError(String.format("The operation <red>%s</red> is not supported!\n" +
                                "Please use one of the following:<red>? set, add, remove</red>", args[1]), sender);
                        break;
                }

            } catch (NumberFormatException e) {
                Messenger.sendError(String.format("The argument <red>%s</red> is not a number!", args[2]), sender);
            } catch (SQLException e) {
                Messenger.sendError("An error occurred while performing the command!\n" +
                        "Please contact staff if this issue persists.", sender);
                e.printStackTrace();
            }
        });

        return true;
    }

    /**
     * Update the online and offline donator data.
     * @param sender Source of the command
     * @param name The name of the player
     * @param rp The rank points
     */
    private void setRankPoints(CommandSender sender, String name, double rp) {
        StoreData.updateRank(name, rp);
        updateOnline(name, rp);
        Messenger.sendSuccess(String.format("%s now has <green>%f</green> rank points.",
                name, rp), sender);
    }

    /**
     * Update a player's rank if they are online.
     * @param name The name of the player
     * @param rp The rank points
     */
    private void updateOnline(String name, double rp) {
        // Ensure the player is online
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return;
        }

        // Ensure the player is actively being tracked
        UUID uuid = player.getUniqueId();
        PlayerData data = ActiveData.getData(uuid);
        if (data == null) {
            return;
        }

        // Get the player's top donator rank
        String rank = "";
        if (rp > 0) {
            rank = RankPoints.getTopRank(uuid);
        }

        // Get the player's regular donator rank otherwise
        if (rank.isEmpty()) {
            rank = RankPoints.getRank(rp);
        }

        // Apply the player's rank change
        data.setRankPoints(rp);
        data.setRank(rank);
        Permissions.setDonatorPermission(uuid, rank);
        Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(player, data));
    }
}
