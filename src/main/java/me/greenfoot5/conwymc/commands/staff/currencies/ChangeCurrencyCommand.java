package me.greenfoot5.conwymc.commands.staff.currencies;

import me.greenfoot5.conwymc.ConwyMC;
import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.database.ActiveData;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Changes a player's currency
 */
public abstract class ChangeCurrencyCommand implements CommandExecutor {

    /**
     * Change a player's currency asynchronously.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return True
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> {
            if (hasIncorrectArgs(sender, args))
                return;

            UUID uuid = getUUID(args[0]);
            double amount = Double.parseDouble(args[1]);
            changeCurrency(uuid, amount);

            sendConfirmMessage(sender, args[0], amount);
            Player player = Bukkit.getPlayer(uuid);
            boolean verbose = args.length < 3 || Boolean.parseBoolean(args[2]);
            if (player != null && verbose)
                sendTargetMessage(player, amount);
        });
        return true;
    }

    /**
     * Change a player's currency.
     * @param uuid The unique ID of the player
     * @param amount The amount used in the currency change
     */
    protected void changeCurrency(UUID uuid, double amount) {
        PlayerData data = ActiveData.getData(uuid);
        assert data != null;
        changeCurrencyOnline(data, amount);
    }

    /**
     * Check if correct arguments were provided for this command.
     * @param sender Source of the command
     * @param args Passed command arguments
     * @return Whether invalid arguments were provided
     */
    protected boolean hasIncorrectArgs(CommandSender sender, String[] args) {
        // Command format is not followed
        if (args.length != 2 && args.length != 3) {
            Messenger.sendError(getCommandUsage(), sender);
            return true;
        }

        // Player not found
        String playerName = args[0];
        if (getUUID(playerName) == null) {
            Messenger.sendError("Could not find player: " + playerName, sender);
            return true;
        }

        // Amount not a number
        String amount = args[1];
        try {
            Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            Messenger.sendError(amount + " is not a number!", sender);
            return true;
        }

        // verbose is not a boolean
        if (args.length == 3) {
            String verbose = args[2];
            if (!verbose.equalsIgnoreCase("true") && !verbose.equalsIgnoreCase("false")) {
                Messenger.sendError(verbose + " is not a boolean!", sender);
                return true;
            }
        }

        return false;
    }

    /**
     * Get the player's UUID directly from the online players.
     * @param playerName The name of the player
     * @return The player's UUID if found, null otherwise
     */
    protected UUID getUUID(String playerName) {
        // Player is online
        Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            return player.getUniqueId();
        return null;
    }

    /**
     * Get the message describing how to use the command.
     * @return The command usage message
     */
    protected abstract String getCommandUsage();

    /**
     * Change the currency for an online player.
     * @param data The actively stored data of the online player
     * @param amount The amount used in the currency change
     */
    protected abstract void changeCurrencyOnline(PlayerData data, double amount);

    /**
     * Send a confirmation message to the source of the command.
     * @param sender Source of the command
     * @param playerName The name of the player whose currency was changed
     * @param amount The amount used in the currency change
     */
    protected abstract void sendConfirmMessage(CommandSender sender, String playerName, double amount);

    /**
     * Send a message to the target of the command to inform them of the change.
     * @param target The player whose currency is affected
     * @param amount The amount used in the currency change
     */
    protected abstract void sendTargetMessage(Player target, double amount);
}
