package me.greenfoot5.conwymc.commands.staff.support;

import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SupportCommand implements CommandExecutor {
    /**
     * Requests support from staff
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return False if too few arguments were provided, true otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1)
            return false;

        String message = String.join(" ", args);
        SupportEvent supportEvent = new SupportEvent((Player)sender, message);
        Messenger.sendSupportRequest(message, sender);
        Bukkit.getPluginManager().callEvent(supportEvent);

        Messenger.sendSuccess("Your message has been sent to staff!", sender);

        return true;
    }
}
