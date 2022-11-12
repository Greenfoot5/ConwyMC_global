package me.huntifi.conwymc.database;

import me.huntifi.conwymc.Main;
import me.huntifi.conwymc.data_types.Tuple;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Handles all punishment data related actions
 */
public class Punishments {

    /**
     * Get a player's active punishment from the database.
     * @param uuid The unique ID of the player whose punishment to get
     * @param type The type of punishment to get from the database
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getActive(UUID uuid, String type) throws SQLException {
        PreparedStatement ps = Main.getConnection().prepareStatement(
                "SELECT reason, end FROM punishments WHERE uuid = ? AND type = ? AND end > ?"
                        + " ORDER BY end DESC LIMIT 1");
        ps.setString(1, uuid.toString());
        ps.setString(2, type);
        ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }
}
