package me.huntifi.conwymc.database;

import me.huntifi.conwymc.ConwyMC;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Cheap solution to keep the database connected
 */
public class KeepAlive implements Runnable {

    /**
     * Get 0 rows from the player_rank table.
     */
    @Override
    public void run() {
        try (PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                "SELECT * FROM player_rank LIMIT 0"
        )) {
            ps.execute();
        } catch (SQLException e) {
            ConwyMC.instance.getLogger().warning("Could not keep the database connection alive!");
        }
    }
}
