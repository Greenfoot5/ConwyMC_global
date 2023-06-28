package me.huntifi.conwymc.events.connection;

import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.database.Permissions;
import me.huntifi.conwymc.database.StoreData;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * Handles what happens when someone logs off
 */
public class PlayerDisconnect implements Listener {

    /**
     * Set a player's quit message, remove them from active storage, and store their data in the database.
     * @param event The event called when a player quits the game
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerData data = ActiveData.getData(uuid);

        setLeaveMessage(event, data);
        Permissions.removePlayer(uuid);
        ActiveData.removePlayer(uuid);
        StoreData.storeAsync(uuid, data);
    }

    /**
     * Overwrite the player's quit message if they have a custom one.
     * @param event The player's quit event
     * @param data The player's data
     */
    private void setLeaveMessage(PlayerQuitEvent event, PlayerData data) {
        if (!data.getLeaveMessage().isEmpty())
            event.setQuitMessage(ChatColor.YELLOW + data.getLeaveMessage());
    }
}
