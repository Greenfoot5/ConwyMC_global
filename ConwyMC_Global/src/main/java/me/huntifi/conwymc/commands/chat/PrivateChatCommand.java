package me.huntifi.conwymc.commands.chat;

import me.huntifi.conwymc.Main;
import me.huntifi.conwymc.commands.staff.punishments.MuteCommand;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

/**
 * Sends a private message
 */
public abstract class PrivateChatCommand extends ChatCommand {

    /** Maps command senders to the command sender from whom they last received a private message */
    protected static final HashMap<CommandSender, CommandSender> lastSender = new HashMap<>();

    /** The minimum amount of arguments required to use this command */
    protected int requiredArguments;

    /**
     * Send the provided arguments as a private message.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return False if too few arguments were provided, true otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length < requiredArguments)
            return false;

        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {
            if (sender instanceof Player && MuteCommand.isMuted(((Player) sender).getUniqueId()))
                return;

            CommandSender target = getTarget(sender, args);
            if (target == null)
                sendTargetNotFoundMessage(sender, args);
            else if (Objects.equals(sender, target))
                Messenger.sendError("You are not a clown. You are the entire circus.", sender);
            else if (target instanceof Player && !((Player) target).isOnline())
                Messenger.sendError("This player is no longer online!", sender);
            else
                sendMessage(sender, target, getMessage(args));
        });

        return true;
    }

    /**
     * Privately send the message from the sender to the target.
     * @param sender The sender of the message
     * @param target The recipient of the message
     * @param message The message
     */
    private void sendMessage(CommandSender sender, CommandSender target, String message) {
        sender.sendMessage(String.format("%sTo %s%s: %s%s",
                ChatColor.GOLD, getName(target), ChatColor.GOLD, ChatColor.DARK_AQUA, message));
        target.sendMessage(String.format("%sFrom %s%s: %s%s",
                ChatColor.GOLD, getName(sender), ChatColor.GOLD, ChatColor.DARK_AQUA, message));

        lastSender.put(target, sender);
        if (target instanceof Player)
            playTagSound((Player) target);
    }

    /**
     * Get the target of this command.
     * @param sender The sender of the message
     * @param args The command arguments
     * @return The recipient of the message, or null if not found
     */
    protected abstract CommandSender getTarget(CommandSender sender, String[] args);

    /**
     * Get the message that is to be sent via this command.
     * @param args The command arguments
     * @return The message
     */
    protected abstract String getMessage(String[] args);

    /**
     * Notify the sender that the target could not be found.
     * @param sender The sender of the message
     * @param args The command arguments
     */
    protected abstract void sendTargetNotFoundMessage(CommandSender sender, String[] args);
}
