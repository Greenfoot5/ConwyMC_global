package me.greenfoot5.conwymc.events.nametag;

import com.nametagedit.plugin.NametagEdit;
import me.greenfoot5.conwymc.ConwyMC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
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
    @EventHandler(priority = EventPriority.LOWEST)
    public void onUpdateNameTag(UpdateNameTagEvent event) {
        Player player = event.getPlayer();
        Component title = event.getTitle() == null ? Component.empty() : event.getTitle().append(Component.text(" "));
        Component rank = event.getDisplayRank();
        String serialized = LegacyComponentSerializer.legacySection().serialize(rank);

        Bukkit.getScheduler().runTask(ConwyMC.plugin, () -> {
            player.displayName(title.append(rank).append(Component.text(player.getName(), NamedTextColor.WHITE)));
            if (serialized.isEmpty()) {
                NametagEdit.getApi().setPrefix(player, String.valueOf(ChatColor.WHITE));
            } else {
                NametagEdit.getApi().setPrefix(player, serialized.substring(0, serialized.length() - 1) + " " + ChatColor.WHITE);
            }
        });
    }
}
