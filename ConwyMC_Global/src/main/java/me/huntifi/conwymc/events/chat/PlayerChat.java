package me.huntifi.conwymc.events.chat;

import me.huntifi.conwymc.Main;
import me.huntifi.conwymc.commands.staff.punishments.Mute;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

/**
 * Customises a player's chat message
 */
public class PlayerChat implements Listener {

    /**
     * Set message color to white for staff and gray otherwise
     * Send the message in a specific mode if applicable
     *
     * @param event The event called when a player sends a message
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Muted players are not allowed to send messages
        if (Mute.isMuted(uuid)) {
            event.setCancelled(true);
            return;
        }

        PlayerData data = ActiveData.getData(uuid);
        String message = event.getMessage();
        event.setMessage(data.getChatColor() + message);
        event.setFormat("%s: %s");

        // TODO: Send message in staff-chat
        /*if (StaffChat.isStaffChatter(player.getUniqueId())) {
            StaffChat.sendMessage(event.getPlayer(), event.getMessage());
            event.setCancelled(true);
            return;
        }*/

        tagPlayersAsync(message);
    }

    /**
     * Asynchronously play a tag sound for all tagged players
     * @param message The message sent
     */
    private void tagPlayersAsync(String message) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {
            String lowerCaseMessage = message.toLowerCase();
            for (Player tagged : Bukkit.getOnlinePlayers()) {
                if (lowerCaseMessage.contains("@" + tagged.getName().toLowerCase()))
                    playTagSound(tagged);
            }
        });
    }

    /**
     * Play the tag sound for a tagged player.
     * @param player The tagged player
     */
    private void playTagSound(Player player) {
        Location location = player.getLocation();
        Sound sound = Sound.BLOCK_NOTE_BLOCK_BELL;
        float volume = 1f; // 1 = 100%
        float pitch = 0.5f; // Float between 0.5 and 2.0

        player.playSound(location, sound, volume, pitch);
    }
}
