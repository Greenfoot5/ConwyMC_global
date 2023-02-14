package me.huntifi.conwymc.commands.chat;

import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Sends a private message to the supplied target
 */
public class MessageCommand extends PrivateChatCommand {

    /**
     * Set the minimum amount of arguments required to use this command.
     */
    public MessageCommand() {
        this.requiredArguments = 2;
    }

    @Override
    protected CommandSender getTarget(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("console"))
            return Bukkit.getServer().getConsoleSender();

        return Bukkit.getPlayer(args[0]);
    }

    @Override
    protected String getMessage(String[] args) {
        return String.join(" ", args).split(" ", 2)[1];
    }

    @Override
    protected void sendTargetNotFoundMessage(CommandSender sender, String[] args) {
        Messenger.sendError("Could not find player: " + ChatColor.RED + args[0], sender);
    }
}
