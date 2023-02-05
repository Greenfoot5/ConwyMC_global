package me.huntifi.conwymc.events.nametag;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UpdateNameTagEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    /**
     * Create a new event that calls for a player's name tag to be updated.
     * @param player The player
     */
    public UpdateNameTagEvent(Player player) {
        this.player = player;
    }

    /**
     * Get the player whose name tag should be updated
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
