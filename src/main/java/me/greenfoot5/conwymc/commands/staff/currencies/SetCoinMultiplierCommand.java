package me.greenfoot5.conwymc.commands.staff.currencies;

import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Sets the active coin multiplier
 */
public class SetCoinMultiplierCommand implements CommandExecutor {

    /**
     * Set the active coin multiplier, if a positive number is supplied.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return Whether a single argument is supplied
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1)
            return false;

        try {
            double multiplier = Double.parseDouble(args[0]);
            if (multiplier < 0) {
                throw new NumberFormatException();
            }
            PlayerData.setCoinMultiplier(multiplier);
            Messenger.broadcastInfo("The coin multiplier has been set to: <yellow>" + args[0]);
        } catch (NumberFormatException e) {
            Messenger.sendError(String.format("The argument <red>%s</red> is not a positive number!", args[0]), sender);
        }

        return true;
    }
}
