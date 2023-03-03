package me.huntifi.conwymc.commands.info;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Shows the player our web shop link
 */
public class WebShopCommand implements CommandExecutor {

    /**
     * Print our web shop link to the player.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        String message = "--------------------------------\n" +
                ChatColor.GREEN + "Link to the web shop:\n" +
                ChatColor.BLUE + "https://conwymc.tebex.io/\n" +
                "--------------------------------";
        sender.sendMessage(message);

        return true;
    }
}
