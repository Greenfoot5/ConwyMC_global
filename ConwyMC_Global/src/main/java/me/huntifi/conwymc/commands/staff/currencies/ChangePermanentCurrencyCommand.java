package me.huntifi.conwymc.commands.staff.currencies;

import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.database.LoadData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Changes a player's permanent currency
 */
public abstract class ChangePermanentCurrencyCommand extends ChangeCurrencyCommand {

    /**
     * Change a player's permanent currency.
     * @param uuid The unique ID of the player
     * @param amount The amount used in the currency change
     */
    @Override
    protected void changeCurrency(UUID uuid, double amount) {
        PlayerData data = ActiveData.getData(uuid);
        if (data != null)
            changeCurrencyOnline(data, amount);
        else
            updateDatabase(uuid, amount);
    }

    /**
     * Get the player's UUID directly from the online players or from the database.
     * @param playerName The name of the player
     * @return The player's UUID if found, null otherwise
     */
    @Override
    protected UUID getUUID(String playerName) {
        // Player is online
        UUID uuid = super.getUUID(playerName);
        if (uuid != null)
            return uuid;

        // Player is offline or doesn't exist in our database
        try {
            return LoadData.getUUID(playerName);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Update the player's currency in the database.
     * @param uuid The UUID of the player
     * @param amount The amount of currency to set
     */
    private void updateDatabase(UUID uuid, double amount) {
        try (PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(getQuery())) {
            ps.setDouble(1, amount);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the query used to update the player's coins in the database.
     * @return The SQL query
     */
    protected abstract String getQuery();
}
