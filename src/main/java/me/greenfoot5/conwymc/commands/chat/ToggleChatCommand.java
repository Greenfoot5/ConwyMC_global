package me.greenfoot5.conwymc.commands.chat;

import io.papermc.paper.chat.ChatRenderer;
import me.greenfoot5.conwymc.ConwyMC;
import me.greenfoot5.conwymc.commands.staff.punishments.MuteCommand;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Toggles chat mode or sends a message in the chat mode
 */
public abstract class ToggleChatCommand extends ChatCommand implements ChatRenderer, Listener {

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
     * @param message The message content to send
     */
    private void sendMessage(CommandSender sender, String message) {
        String content = Messenger.clean(message);

        // Get alternative versions of the message
        Component componentMessage;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            componentMessage = render(p, p.displayName(), Component.text(content), getReceivers(p));
        } else {
            String color = getChatColor(sender);

            Component c = Messenger.mm.deserialize(color + message);
            componentMessage = getName(sender).append(Component.text(": "))
                    .append(c);
        }

        getReceivers(sender).sendMessage(componentMessage);
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
    protected abstract Audience getReceivers(CommandSender sender);
}
