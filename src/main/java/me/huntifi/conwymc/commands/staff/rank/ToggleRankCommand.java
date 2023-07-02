package me.huntifi.conwymc.commands.staff.rank;

import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.events.nametag.UpdateNameTagEvent;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Configures a player's shown rank
 */
public class ToggleRankCommand implements CommandExecutor {

    /**
     * Switch between displaying staff and donator rank.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console cannot toggle their rank!", sender);
            return true;
        }

        Player player = (Player) sender;
        PlayerData data = ActiveData.getData(player.getUniqueId());
        if (data.toggleRank()) {
            Messenger.sendInfo("Staff rank toggled on", sender);
        } else {
            Messenger.sendInfo("Staff rank toggled off", sender);
        }

        Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(player, data.getDisplayRank()));
        return true;

    }
}
