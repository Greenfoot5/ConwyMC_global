package me.huntifi.conwymc.database;

import me.huntifi.conwymc.Main;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

/**
 * Store a player's data
 */
public class StoreData {

    /**
     * Update the player name saved in the database.
     * This update is only done on the "player_rank" table and is always executed asynchronously.
     * @param uuid The unique ID of the player
     */
    public static void updateName(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {
            try (PreparedStatement ps = Main.getConnection().prepareStatement(
                    "UPDATE player_rank SET name = ? WHERE uuid = ?"
            )) {
                ps.setString(1, Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName());
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
