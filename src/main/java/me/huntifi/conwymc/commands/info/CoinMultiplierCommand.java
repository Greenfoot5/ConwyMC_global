package me.huntifi.conwymc.commands.info;

import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Shows the player the active coin multiplier
 */
public class CoinMultiplierCommand implements CommandExecutor {

    /**
     * Print the active coin multiplier to the player.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Messenger.sendInfo(String.format("%sCoin Multiplier: %s%.1f",
                ChatColor.GOLD, ChatColor.YELLOW, PlayerData.getCoinMultiplier()), sender);
        return true;
    }
}
