package me.greenfoot5.conwymc.commands.staff.currencies;

import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Sets a player's coins
 */
public class SetCoinsCommand extends ChangePermanentCurrencyCommand {

    @Override
    protected String getCommandUsage() {
        return "Use: /setcoins <player> <amount>";
    }

    @Override
    protected String getQuery() {
        return "UPDATE player_rank SET coins = ? WHERE UUID = ?";
    }

    @Override
    protected void changeCurrencyOnline(PlayerData data, double amount) {
        data.setCoins(amount);
    }

    @Override
    protected void sendConfirmMessage(CommandSender sender, String playerName, double amount) {
        Messenger.sendInfo(String.format("%s's coins have been set to %.0f", playerName, amount), sender);
    }

    @Override
    protected void sendTargetMessage(Player target, double amount) {
        Messenger.sendInfo(String.format("Your coins have been set to %.0f by staff", amount), target);
    }
}
