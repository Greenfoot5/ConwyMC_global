package me.huntifi.conwymc.events.connection;

import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.LoadData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Handles what happens when someone logs in
 */
public class PlayerConnect implements Listener {

    /**
     * Assign the player's data and join a team
     * @param e The event called when a player join the game
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // TODO: Login stuff
    }

    /**
     * Check if the player is allowed to join the game.
     * Load the player's data.
     * @param e The event called when a player attempts to join the server
     * @throws SQLException If something goes wrong executing a query
     */
    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent e) throws SQLException {
        if (e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            // TODO: Check if player is banned => disallow login if so

            // The player is allowed to join, so we can start loading their data
            loadData(e.getUniqueId());
        }
    }

    /**
     * Load the player's data
     * Actively store the loaded data
     * @param uuid The unique ID of the player
     */
    private void loadData(UUID uuid) {
        // Load the player's data
        PlayerData data = LoadData.load(uuid);
        assert data != null;

        // TODO: Actively store the data
        // TODO: Set the player's donator top rank
    }
}
