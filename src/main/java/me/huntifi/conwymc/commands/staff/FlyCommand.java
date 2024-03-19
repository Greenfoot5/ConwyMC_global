package me.huntifi.conwymc.commands.staff;

import me.huntifi.conwymc.util.Messenger;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Allows staff players to fly
 */
public class FlyCommand implements CommandExecutor {

    /**
     * Toggle flight for the player.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console cannot fly!", sender);
            return true;
        }

        Player player = (Player) sender;
        if (player.getGameMode() == GameMode.SPECTATOR) {
            Messenger.sendError("Spectators can already fly!", sender);
            return true;
        }

        player.setAllowFlight(!player.getAllowFlight());
        player.setFlying(player.getAllowFlight());
        player.setFlySpeed(0.2f);

        if (player.getAllowFlight())
            Messenger.sendSuccess("Flying has been enabled for you, enjoy your flight!", sender);
        else
            Messenger.sendSuccess("Flying has been disabled for you!", sender);

        return true;
    }
}
