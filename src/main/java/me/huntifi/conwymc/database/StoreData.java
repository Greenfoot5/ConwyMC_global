package me.huntifi.conwymc.database;

import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.data_types.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * Store a player's data
 */
public class StoreData {

    /**
     * Store a player's data in the database.
     * @param uuid The unique ID of the player
     * @param data The player's data
     */
    private static void store(UUID uuid, PlayerData data) {
        try (PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                "UPDATE player_rank SET staff_rank = ?, rank_points = ?, coins = ? WHERE UUID = ?"
        )) {
            ps.setString(1, data.getStaffRank());
            ps.setDouble(2, data.getRankPoints());
            ps.setDouble(3, data.getCoins());
            ps.setString(4, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asynchronously store a player's data in the database.
     * @param uuid The unique ID of the player
     * @param data The player's data
     */
    public static void storeAsync(UUID uuid, PlayerData data) {
        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> store(uuid, data));
    }

    /**
     * Store the data of all players who are in active storage.
     * This is <strong>not</strong> done asynchronously, so should only be called when shutting down the server.
     */
    public static void storeAll() {
        Collection<UUID> players = ActiveData.getPlayers();
        for (UUID uuid : players) {
            store(uuid, ActiveData.getData(uuid));
        }
    }

    /**
     * Update the player name saved in the database.
     * This update is only done on the "player_rank" table and is always executed asynchronously.
     * @param uuid The unique ID of the player
     */
    public static void updateName(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> {
            try (PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                    "UPDATE player_rank SET username = ? WHERE UUID = ?"
            )) {
                ps.setString(1, Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName());
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Update the player's donator rank saved in the database
     * @param name The name of the player
     * @param rp The player's rank points
     */
    public static void updateRank(String name, double rp) {
        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> {
            try (PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                        "UPDATE player_rank SET rank_points = ? WHERE username = ?"
            )) {
                ps.setDouble(1, rp);
                ps.setString(2, name);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Used to update a player's existing setting
     * @param uuid The uuid of the player
     * @param setting The setting to set
     * @param value The value of the setting
     */
    public static void updateSetting(UUID uuid, String setting, String value) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                            "UPDATE player_settings SET value = ? WHERE UUID = ? AND setting = ?");
                    ps.setString(1, value);
                    ps.setString(2, uuid.toString());
                    ps.setString(3, setting);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(ConwyMC.plugin);
    }

    /**
     * Used to add a player's setting to the database
     * @param uuid The uuid of the player
     * @param setting The setting to set
     * @param value The value of the setting
     */
    public static void addSetting(UUID uuid, String setting, String value) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                            "INSERT INTO player_settings VALUES (?, ?, ?)");
                    ps.setString(1, uuid.toString());
                    ps.setString(2, setting);
                    ps.setString(3, value);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(ConwyMC.plugin);
    }

    /**
     * Used to grant a player a cosmetic
     * @param uuid The player's UUID
     * @param type The cosmetic type
     * @param content The cosmetic's value
     * @param name The cosmetic's name
     */
    public static void grantCosmetic(UUID uuid, String type, String name, String content) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                            "INSERT INTO player_cosmetics VALUES (?, ?, ?, ?)");
                    ps.setString(1, uuid.toString());
                    ps.setString(2, type);
                    ps.setString(3, content);
                    ps.setString(4, name);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(ConwyMC.plugin);
    }

    /**
     * Used to remove a player's cosmetic
     * @param uuid The player's UUID
     * @param type The cosmetic type
     * @param name The cosmetic's name
     */
    public static void removeCosmetic(UUID uuid, String type, String name) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                            "DELETE FROM player_cosmetics WHERE UUID = ? AND TYPE = ? AND name = ?");
                    ps.setString(1, uuid.toString());
                    ps.setString(2, type);
                    ps.setString(3, name);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(ConwyMC.plugin);
    }
}
