package me.huntifi.conwymc.database;

import me.huntifi.conwymc.Main;

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
        try (PreparedStatement ps = Main.getConnection().prepareStatement(
                "SELECT * FROM player_rank LIMIT 0"
        )) {
            ps.execute();
        } catch (SQLException e) {
            Main.getInstance().getLogger().warning("Could not keep the database connection alive!");
        }
    }
}
