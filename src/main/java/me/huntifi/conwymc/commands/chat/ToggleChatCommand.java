package me.huntifi.conwymc.commands.chat;

import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.commands.staff.punishments.MuteCommand;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

/**
 * Toggles chat mode or sends a message in the chat mode
 */
public abstract class ToggleChatCommand extends ChatCommand {

    /**
     * Toggle this chat mode if no arguments are provided.
     * Send the provided arguments as message in the chat mode.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return True
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> {
            String message = String.join(" ", args);

            if (message.isEmpty()) {
                if (sender instanceof Player)
                    toggleChatMode((Player) sender);
                else
                    Messenger.sendError("Console cannot toggle chat modes", sender);
            } else if (!(sender instanceof Player && MuteCommand.isMuted(((Player) sender).getUniqueId()))) {
                sendMessage(sender, message);
            }
        });

        return true;
    }

    /**
     * Send the message from the sender to the receivers via this chat mode.
     * Play a tag sound for all tagged players.
     * @param sender The sender of the message
     * @param message The message
     */
    private void sendMessage(CommandSender sender, String message) {
        // Get alternative versions of the message
        String formattedMessage = getFormattedMessage(sender, message);
        String lowerMessage = message.toLowerCase();

        // Loop over all receivers to send the message and play the tag sound
        for (CommandSender receiver : getReceivers(sender)) {
            receiver.sendMessage(formattedMessage);

            // Cannot tag self or console
            if (Objects.equals(sender, receiver) || !(receiver instanceof Player))
                continue;

            if (lowerMessage.contains("@" + receiver.getName().toLowerCase()))
                playTagSound((Player) receiver);
        }
    }

    /**
     * Toggle this chat mode for the player.
     * @param player The player
     */
    protected abstract void toggleChatMode(Player player);

    /**
     * Get everyone who should receive the message.
     * @param sender The sender of the message
     * @return The receivers of the message
     */
    protected abstract Collection<CommandSender> getReceivers(CommandSender sender);

    /**
     * Get the message with formatting applied.
     * @param sender The sender of the message
     * @param message The message
     * @return The formatted message
     */
    protected abstract String getFormattedMessage(CommandSender sender, String message);
}
