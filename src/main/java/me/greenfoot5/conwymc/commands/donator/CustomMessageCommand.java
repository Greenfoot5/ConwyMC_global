package me.greenfoot5.conwymc.commands.donator;

import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.database.ActiveData;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Manages custom messages
 */
public abstract class CustomMessageCommand implements CommandExecutor {

    /**
     * Change the player's custom message if valid arguments are supplied
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return Whether valid arguments are supplied
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError(String.format("Console cannot set their %s message!", getMessageType()), sender);
            return true;
        }

        if (args.length != 0 && args[0].equalsIgnoreCase("reset")) {
            setMessage((Player) sender, "");
            return true;
        } else if (args.length < 2) {
            return false;
        } else if (args[0].equalsIgnoreCase("set")) {
            setMessage((Player) sender, String.join(" ", args).split(" ", 2)[1]);
            return true;
        }

        return false;
    }

    /**
     * Set the player's custom message
     * @param player The player
     * @param message The custom message
     */
    private void setMessage(Player player, String message) {
        if (message.length() > 128) {
            Messenger.sendError(String.format("Your %s message cannot be longer than 128 characters!",
                    getMessageType()), player);
            return;
        }

        setMessageData(ActiveData.getData(player.getUniqueId()), message);
        if (message.isEmpty()) {
            Messenger.sendSuccess(String.format("Your %s message has been reset.", getMessageType()), player);
        } else {
            Messenger.sendSuccess(String.format("Your %s message has been set to: <green>%s",
                    getMessageType(), message), player);
        }
    }

    /**
     * Update the player's data with the new custom message
     * @param data The player's data
     * @param message The custom message
     */
    protected abstract void setMessageData(PlayerData data, String message);

    /**
     * Get the message type of this command
     * @return The message type
     */
    protected abstract String getMessageType();
}
