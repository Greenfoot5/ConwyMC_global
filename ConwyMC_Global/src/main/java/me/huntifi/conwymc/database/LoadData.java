package me.huntifi.conwymc.database;

import me.huntifi.conwymc.Main;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.data_types.Tuple;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * This class provides methods to retrieve data from the database.
 * These methods should only be called asynchronously.
 */
public class LoadData {

    /**
     * Load all data for a specific player.
     * @param uuid The unique ID of the player
     * @return The player data
     */
    public static PlayerData load(UUID uuid) {
        try {
            // Rank data
            createRankEntry(uuid);
            Tuple<PreparedStatement, ResultSet> prRank = getRankData(uuid);

            // Collect data and release resources
            PlayerData data = new PlayerData(prRank.getSecond());
            prRank.getFirst().close();

            return data;
        } catch (SQLException e) {
            Main.getInstance().getLogger().warning("Something went wrong while loading player data!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create an entry in the player rank table if none exists yet.
     * @param uuid The player for whom to create an entry
     * @throws SQLException If a database access error occurs
     */
    private static void createRankEntry(UUID uuid) throws SQLException {
        PreparedStatement ps = Main.getConnection().prepareStatement(
                "INSERT IGNORE INTO player_rank (uuid) VALUES (?)");
        ps.setString(1, uuid.toString());
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Get a player's rank data from the database.
     * @param uuid The unique id of the player whose data to get
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    private static Tuple<PreparedStatement, ResultSet> getRankData(UUID uuid) throws SQLException {
        // Get player stats from the database
        PreparedStatement ps = Main.getConnection().prepareStatement("SELECT * FROM player_rank WHERE uuid=?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();

        // Return result set with pointer on first (and only) row
        rs.next();
        return new Tuple<>(ps, rs);
    }
}
