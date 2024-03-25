package me.huntifi.conwymc.events.nametag;

import me.huntifi.conwymc.database.ActiveData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * The event called when a player's nametag should be updated
 */
public class UpdateNameTagEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    private final Component displayRank;

    /**
     * Create a new event that calls for a refresh to a player's name tag
     * @param player The player
     */
    public UpdateNameTagEvent(Player player) {
        this.player = player;
        displayRank = ActiveData.getData(player.getUniqueId()).getDisplayRank();
    }

    /**
     * Create a new event that calls for a player's name tag to be updated.
     * @param player The player
     * @param displayRank The pretty rank string that should be used
     */
    public UpdateNameTagEvent(Player player, Component displayRank) {
        this.player = player;
        this.displayRank = displayRank;
    }

    /**
     * Get the player whose name tag should be updated.
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the player's new display rank.
     * @return The pretty rank string
     */
    public Component getDisplayRank() {
        return displayRank;
    }

    /**
     * Get this event's handlers.
     * @return This event's handlers
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
