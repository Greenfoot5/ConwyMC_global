package me.greenfoot5.conwymc.database;

import me.greenfoot5.conwymc.data_types.PlayerData;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Stores the data of online players
 */
public class ActiveData {

    /** A map that stores player data by their unique ID */
    protected static final HashMap<UUID, PlayerData> playerData = new HashMap<>();

    /**
     * Add a player's data to the active storage.
     * @param uuid The unique ID of the player
     * @param data The player's data
     */
    public static void addPlayer(UUID uuid, PlayerData data) {
        playerData.put(uuid, data);
    }

    /**
     * Remove a player from the active storage.
     * @param uuid The unique ID of the player
     */
    public static void removePlayer(UUID uuid) {
        playerData.remove(uuid);
    }

    /**
     * Get the unique ID of all players whose stats are actively stored.
     * @return All keys in playerData
     */
    public static Collection<UUID> getPlayers() {
        return playerData.keySet();
    }

    /**
     * Get the data of a player.
     * @param uuid The unique ID of the player
     * @return The player's data
     */
    public static PlayerData getData(UUID uuid) {
        return playerData.get(uuid);
    }
}
