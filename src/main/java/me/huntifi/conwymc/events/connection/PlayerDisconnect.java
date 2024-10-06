package me.huntifi.conwymc.events.connection;

import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.database.Permissions;
import me.huntifi.conwymc.database.StoreData;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;
import java.util.UUID;

/**
 * Handles what happens when someone logs off
 */
public class PlayerDisconnect implements Listener {

    /**
     * Set a player's quit message, remove them from active storage, and store their data in the database.
     * @param event The event called when a player quits the game
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerData data = ActiveData.getData(uuid);

        setLeaveMessage(event, data);
        Permissions.removePlayer(uuid);
        StoreData.storeAsync(uuid, data);
    }

    /**
     * Overwrite the player's quit message if they have a custom one.
     * @param event The player's quit event
     * @param data The player's data
     */
    private void setLeaveMessage(PlayerQuitEvent event, PlayerData data) {

        TextComponent component = (TextComponent) data.getCosmetics().getLeaveMessage(event.getPlayer().getName());
        if (component.content().equals("null")) {
            event.quitMessage(Messenger.mm.deserialize(event.getPlayer().getName() + " has left the battle.").color(TextColor.color(255, 255, 85)));
            return;
        }
        // Set the leave message
        event.quitMessage(data.getCosmetics().getLeaveMessage(event.getPlayer().getName()));
    }
}
