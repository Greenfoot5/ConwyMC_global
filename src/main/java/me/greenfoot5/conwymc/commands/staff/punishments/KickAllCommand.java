package me.greenfoot5.conwymc.commands.staff.punishments;

import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Kicks all players from the server
 */
public class KickAllCommand implements CommandExecutor {

    /**
     * Kick all players.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Component message = Messenger.mm.deserialize("<red>You were kicked by <white>" + sender.getName());
        if (args.length > 0)
            message = message.append(Messenger.mm.deserialize("<red> for: " + String.join(" ", args)));

        for (Player player : Bukkit.getOnlinePlayers())
            player.kick(message);

        return true;
    }
}