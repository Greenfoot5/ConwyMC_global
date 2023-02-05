package me.huntifi.conwymc.commands.staff;

import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.database.Permissions;
import me.huntifi.conwymc.events.nametag.UpdateNameTagEvent;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.conwymc.util.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Gives a player a staff rank
 */
public class SetStaffRankCommand implements CommandExecutor {

    /**
     * Give the specified staff rank to the specified player
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

        // Get the target player
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            Messenger.sendError("Could not find player: " + ChatColor.RED + args[0], sender);
            return true;
        }

        // Set the player's staff rank
        String rank = args.length == 1 || args[1].equalsIgnoreCase("none") ? "" : args[1].toLowerCase();
        if (Permissions.setStaffPermission(player.getUniqueId(), rank)) {
            ActiveData.getData(player.getUniqueId()).setStaffRank(rank);
            Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(player, NameTag.convertRank(rank)));
        } else {
            Messenger.sendError("Rank " + ChatColor.RED + rank + ChatColor.DARK_RED + " is invalid!", sender);
        }

        return true;
    }
}
