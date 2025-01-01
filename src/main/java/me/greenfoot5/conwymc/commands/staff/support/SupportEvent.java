package me.greenfoot5.conwymc.commands.staff.support;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An event for when a gate is rammed either by a gate or a ram
 */
public class SupportEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String message;

    /**
     * Create a SupportEvent to request support
     * @param player The player asking for support
     * @param message The message to send
     */
    public SupportEvent(Player player, String message) {
//        super(true);
        this.player = player;
        this.message = message;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * @return The list of handlers
     */
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * @return The player asking for support
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return The support message
     */
    public String getMessage() {
        return message;
    }
}