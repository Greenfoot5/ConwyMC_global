package me.huntifi.conwymc.commands.staff.currencies;

import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Takes from a player's coins
 */
public class TakeCoinsCommand extends ChangePermanentCurrencyCommand {

    @Override
    protected String getCommandUsage() {
        return "Use: /takecoins <player> <amount>";
    }

    @Override
    protected String getQuery() {
        return "UPDATE player_stats SET coins = coins - ? WHERE uuid = ?";
    }

    @Override
    protected void changeCurrencyOnline(PlayerData data, double amount) {
        data.takeCoinsForce(amount);
    }

    @Override
    protected void sendConfirmMessage(CommandSender sender, String playerName, double amount) {
        Messenger.sendInfo(String.format("%.0f coins have been taken from %s", amount, playerName), sender);
    }

    @Override
    protected void sendTargetMessage(Player target, double amount) {
        Messenger.sendInfo(String.format("%.0f coins have been taken from you by staff", amount), target);
    }
}
