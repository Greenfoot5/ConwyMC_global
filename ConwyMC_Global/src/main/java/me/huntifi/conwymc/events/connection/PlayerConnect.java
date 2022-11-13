package me.huntifi.conwymc.events.connection;

import me.huntifi.conwymc.commands.staff.punishments.PunishmentTime;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.database.LoadData;
import me.huntifi.conwymc.database.Punishments;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
            Tuple<String, Timestamp> banned = getBan(e.getUniqueId(), e.getAddress());
            if (banned != null) {
                e.disallow(
                        AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                        ChatColor.DARK_RED + "\n[BAN] " + ChatColor.RED + banned.getFirst()
                                + ChatColor.DARK_RED + "\n[EXPIRES IN] " + ChatColor.RED
                                + PunishmentTime.getExpire(banned.getSecond())
                );
                return;
            }

            // The player is allowed to join, so we can start loading their data
            loadData(e.getUniqueId());
        }
    }

    /**
     * Load the player's data.
     * Actively store the loaded data.
     * @param uuid The unique ID of the player
     */
    private void loadData(UUID uuid) {
        // Load the player's data
        PlayerData data = LoadData.load(uuid);
        assert data != null;

        // Actively store data
        ActiveData.addPlayer(uuid, data);

        // TODO: Set the player's donator top rank
    }

    /**
     * Get the player's active ban.
     * @param uuid The unique ID of the player
     * @param ip The IP-address of the player
     * @return The reason and end of an active ban, null if no active ban was found
     * @throws SQLException If something goes wrong executing the query
     */
    private Tuple<String, Timestamp> getBan(UUID uuid, InetAddress ip) throws SQLException {
        // Check all ban records for this uuid to see if one is still active
        Tuple<PreparedStatement, ResultSet> prUUID = Punishments.getActive(uuid, "ban");
        Tuple<String, Timestamp> uuidBan = checkBan(prUUID.getSecond());
        prUUID.getFirst().close();
        if (uuidBan != null)
            return uuidBan;

        // Check all ban records for this IP to see if one is still active
        Tuple<PreparedStatement, ResultSet> prIP = Punishments.getIPBan(ip);
        Tuple<String, Timestamp> ipBan = checkBan(prIP.getSecond());
        prIP.getFirst().close();
        return ipBan;
    }

    /**
     * Check if the query result contains an active ban.
     * @param rs The result of a query
     * @return The reason and end of an active ban, null if no active ban was found
     * @throws SQLException If something goes wrong getting data from the query
     */
    private Tuple<String, Timestamp> checkBan(ResultSet rs) throws SQLException {
        if (rs.next())
            return new Tuple<>(rs.getString("reason"), rs.getTimestamp("end"));
        return null;
    }
}
