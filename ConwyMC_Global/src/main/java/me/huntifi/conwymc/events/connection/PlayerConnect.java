package me.huntifi.conwymc.events.connection;

import me.huntifi.conwymc.Main;
import me.huntifi.conwymc.util.RankPoints;
import me.huntifi.conwymc.util.PunishmentTime;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.database.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
     * Set a player's join message, assign their permissions, and update their stored name.
     * @param event The event called when a player joins the game
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerData data = ActiveData.getData(uuid);

        setJoinMessage(event, data);
        setPermissions(uuid, data);
        StoreData.updateName(uuid);
    }

    /**
     * Check if the player is allowed to join the game.
     * Load the player's data.
     * @param event The event called when a player attempts to join the server
     * @throws SQLException If something goes wrong executing a query
     */
    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent event) throws SQLException {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;

        Tuple<String, Timestamp> banned = getBan(event.getUniqueId(), event.getAddress());
        if (banned != null) {
            event.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                    ChatColor.DARK_RED + "\n[BAN] " + ChatColor.RED + banned.getFirst()
                            + ChatColor.DARK_RED + "\n[EXPIRES IN] " + ChatColor.RED
                            + PunishmentTime.getExpire(banned.getSecond())
            );
            return;
        }

        // The player is allowed to join, so we can start loading their data
        loadData(event.getUniqueId());
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

        // Set the player's donator rank
        data.setRank(RankPoints.getRank(data.getRankPoints()));
        if (data.getRankPoints() > 0) {
            String rank = RankPoints.getTopRank(uuid);
            if (!rank.isEmpty())
                data.setRank(rank);
        }
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

    /**
     * Overwrite the player's join message if they have a custom one.
     * @param event The player's join event
     * @param data The player's data
     */
    private void setJoinMessage(PlayerJoinEvent event, PlayerData data) {
        if (!data.getJoinMessage().isEmpty())
            event.setJoinMessage(ChatColor.YELLOW + data.getJoinMessage());
    }

    /**
     * Set the player's staff and donator permissions.
     * @param uuid The unique ID of the player
     * @param data The player's data
     */
    private void setPermissions(UUID uuid, PlayerData data) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {
            Permissions.addPlayer(uuid);
            Permissions.setStaffPermission(uuid, data.getStaffRank());
            Permissions.setDonatorPermission(uuid, data.getRank());
        });
    }
}
