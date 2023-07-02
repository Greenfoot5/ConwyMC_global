package me.huntifi.conwymc.events.nametag;

import com.nametagedit.plugin.NametagEdit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Handles updating a player's name tag
 */
public class UpdateNameTag implements Listener {

    /**
     * Update a player's name tag
     * @param event The event called when a player's name tag should be updated
     */
    @EventHandler (priority = EventPriority.LOWEST)
    public void onUpdateNameTag(UpdateNameTagEvent event) {
        Player player = event.getPlayer();
        String rank = event.getDisplayRank();

        player.setDisplayName(rank + ChatColor.GRAY + player.getName());
        NametagEdit.getApi().setPrefix(player, rank + ChatColor.GRAY);
    }
}
