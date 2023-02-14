package me.huntifi.conwymc.commands.chat;

import me.huntifi.conwymc.Main;
import me.huntifi.conwymc.commands.staff.punishments.MuteCommand;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {
            String message = String.join(" ", args);

            if (message.isEmpty()) {
                if (sender instanceof Player)
                    toggleChatMode((Player) sender);
                else
                    Messenger.sendError("Console cannot toggle chat modes", sender);
            } else if (!(sender instanceof Player && MuteCommand.isMuted(((Player) sender).getUniqueId()))) {
                sendMessage(sender, message);
                tagPlayers(message.toLowerCase());
            }
        });

        return true;
    }

    /**
     * Play a tag sound for all tagged players.
     * @param message The message in lower case
     */
    private void tagPlayers(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (shouldTagPlayer(player, message))
                playTagSound(player);
        }
    }

    /**
     * Check whether a player should be tagged.
     * @param player The player
     * @param message The message
     * @return Whether the player should be tagged
     */
    protected boolean shouldTagPlayer(Player player, String message) {
        return message.contains("@" + player.getName().toLowerCase());
    }

    /**
     * Toggle this chat mode for the player.
     * @param player The player
     */
    protected abstract void toggleChatMode(Player player);

    /**
     * Send the message from the sender via this chat mode.
     * @param sender The sender of the message
     * @param message The message
     */
    protected abstract void sendMessage(CommandSender sender, String message);
}
