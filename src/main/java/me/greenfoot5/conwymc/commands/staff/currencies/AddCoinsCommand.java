package me.greenfoot5.conwymc.commands.staff.currencies;

import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Adds to a player's coins
 */
public class AddCoinsCommand extends ChangePermanentCurrencyCommand {

    @Override
    protected String getCommandUsage() {
        return "Use: /addCoins <player> <amount>";
    }

    @Override
    protected String getQuery() {
        return "UPDATE player_rank SET coins = coins + ? WHERE UUID = ?";
    }

    @Override
    protected void changeCurrencyOnline(PlayerData data, double amount) {
        data.addCoinsClean(amount);
    }

    @Override
    protected void sendConfirmMessage(CommandSender sender, String playerName, double amount) {
        Messenger.sendInfo(String.format("%.0f coins have been given to %s", amount, playerName), sender);
    }

    @Override
    protected void sendTargetMessage(Player target, double amount) {
        Messenger.sendInfo(String.format("You have been given %.0f coins by staff", amount), target);
    }
}
