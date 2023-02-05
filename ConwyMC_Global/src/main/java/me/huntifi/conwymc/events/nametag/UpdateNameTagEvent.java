package me.huntifi.conwymc.events.nametag;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UpdateNameTagEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    private final String rank;

    /**
     * Create a new event that calls for a player's name tag to be updated.
     * @param player The player
     * @param rank The rank string that should be used
     */
    public UpdateNameTagEvent(Player player, String rank) {
        this.player = player;
        this.rank = rank;
    }

    /**
     * Get the player whose name tag should be updated
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the player's new rank representation
     * @return The rank string
     */
    public String getRank() {
        return rank;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
