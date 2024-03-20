package me.huntifi.conwymc.commands.info;

import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
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
        Component c = Messenger.mm.deserialize("<green>Support the server here: </green>" +
                "<color:#13C3FF><click:open_url:https://ko-fi.com/conwymc>☕ https://ko-fi.com/conwymc ☕</click></color>");
        Messenger.send(c, sender);

        return true;
    }
}
