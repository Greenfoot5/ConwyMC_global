package me.huntifi.conwymc.events.chat;

import me.huntifi.conwymc.commands.staff.punishments.MuteCommand;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Customises a player's chat message
 */
public class PlayerChat implements Listener {

    /**
     * Cancel chat messages sent by muted players.
     * @param event The event called when a player sends a message
     */
    @EventHandler (priority = EventPriority.LOWEST)
    public void onMutedChat(AsyncPlayerChatEvent event) {
        if (MuteCommand.isMuted(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    /**
     * Send chat messages in global chat when global chat mode is enabled.
     * @param event The event called when a player sends a message
     */
    @EventHandler (ignoreCancelled = true)
    public void onGlobalChat(AsyncPlayerChatEvent event) {
        PlayerData data = ActiveData.getData(event.getPlayer().getUniqueId());
        if (data.getChatMode().equalsIgnoreCase("global")) {
            event.setCancelled(true);
            event.getPlayer().performCommand("GlobalChat " + event.getMessage());
        }
    }

    /**
     * Send chat messages in staff chat when staff chat mode is enabled.
     * @param event The event called when a player sends a message
     */
    @EventHandler (ignoreCancelled = true)
    public void onStaffChat(AsyncPlayerChatEvent event) {
        PlayerData data = ActiveData.getData(event.getPlayer().getUniqueId());
        if (data.getChatMode().equalsIgnoreCase("staff")) {
            event.setCancelled(true);
            event.getPlayer().performCommand("StaffChat " + event.getMessage());
        }
    }
}
