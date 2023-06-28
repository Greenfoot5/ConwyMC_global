package me.huntifi.conwymc.commands.chat;

import me.huntifi.conwymc.util.Messenger;
import org.bukkit.command.CommandSender;

/**
 * Sends a private message to the last player from whom the sender has received a private message
 */
public class ReplyCommand extends PrivateChatCommand {

    /**
     * Set the minimum amount of arguments required to use this command.
     */
    public ReplyCommand() {
        this.requiredArguments = 1;
    }

    @Override
    protected CommandSender getTarget(CommandSender sender, String[] args) {
        return lastSender.get(sender);
    }

    @Override
    protected String getMessage(String[] args) {
        return String.join(" ", args);
    }

    @Override
    protected void sendTargetNotFoundMessage(CommandSender sender, String[] args) {
        Messenger.sendError("Nobody has messaged you!", sender);
    }
}
