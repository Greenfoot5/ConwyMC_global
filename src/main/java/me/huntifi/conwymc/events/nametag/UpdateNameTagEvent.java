package me.huntifi.conwymc.events.nametag;

import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The event called when a player's nametag should be updated
 */
public class UpdateNameTagEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    @Nullable
    private final Component title;
    private final Component displayRank;

    /**
     * Create a new event that calls for a refresh to a player's name tag
     * @param player The player
     */
    public UpdateNameTagEvent(Player player) {
        this.player = player;
        title = ActiveData.getData(player.getUniqueId()).getCosmetics().getTitle();
        displayRank = ActiveData.getData(player.getUniqueId()).getDisplayRank();
    }

    /**
     * Create a new event that calls for a player's name tag to be updated.
     * @param player The player
     * @param title The player's title
     * @param displayRank The pretty rank string that should be used
     */
    public UpdateNameTagEvent(Player player, Component displayRank, Component title) {
        this.player = player;
        this.title = title;
        this.displayRank = displayRank;
    }

    /**
     * Create a new event that calls for a player's name tag to be updated.
     * @param player The player
     * @param data The player's data
     */
    public UpdateNameTagEvent(Player player, PlayerData data) {
        this.player = player;
        this.title = data.getCosmetics().getTitle();
        this.displayRank = data.getDisplayRank();
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
     * @return The pretty rank component
     */
    public Component getDisplayRank() {
        return displayRank;
    }

    /**
     * Get the player's new title
     * @return The player's title
     */
    public Component getTitle() {
        return title;
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
