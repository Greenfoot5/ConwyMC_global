package me.huntifi.conwymc.commands.info;

import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

/**
 * Shows a player's ping
 */
public class PingCommand implements CommandExecutor {

    /**
     * Print the ping of a player.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender && args.length == 0) {
            Messenger.sendError("Console cannot use /ping!", sender);
            return true;
        }

        Player player;
        if (args.length == 0) {
            player = (Player) sender;
        } else {
            player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                Messenger.sendError(String.format("Could not find player: %s%s",  Messenger.ERROR_SECONDARY, args[0]), sender);
                return true;
            }
        }

        String playerName = args.length == 0 ? "Your" : player.getName() + "'s";
        Messenger.sendInfo(String.format("%s ping is %s%s%sms.",
                playerName, Messenger.INFO_SECONDARY, getPing(player), Messenger.INFO_PRIMARY), sender);
        return true;
    }

    /**
     * Get the ping of a player.
     * @param player The player for whom to get the ping
     * @return The ping of the player
     */
    private int getPing(Player player) {
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
