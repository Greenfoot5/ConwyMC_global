package me.huntifi.conwymc.util;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_RED;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

/**
 * A class to handle sending messages to make the consistent
 */
public class Messenger {
    public static final MiniMessage mm = MiniMessage.miniMessage();

    /**
     * Cleans a string of any MiniMessage tags
     * @param content The message to clean
     * @return A message without any MiniMessage tags
     */
    public static String clean(String content) {
        // Strip the content of any mm
        Component mmContent = mm.deserialize(content);
        return PlainTextComponentSerializer.plainText().serialize(mmContent);
    }

    /**
     * Turns a component into a string and clears any mm formatting
     * @param content The component to clean
     * @return A string without any mm formatting
     */
    public static String clean(Component content) {
        // Turn it into a string
        String sContent = PlainTextComponentSerializer.plainText().serialize(content);
        return clean(sContent);
    }

    /**
     * Displays an MM string message to the receiver in an action bar
     * @param message The message to send
     * @param receiver The CommandSender to display the message to
     */
    public static void sendAction(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message);
        receiver.sendActionBar(msg);
    }

    /**
     * Displays an MM string message to the receiver in the chat
     * @param message The message to send
     * @param receiver The CommandSender to display the message to
     */
    public static void send(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message);
        receiver.sendMessage(msg);
    }

    /**
     * Displays a Component message to the receiver in the chat
     * @param msg The message to send
     * @param receiver The CommandSender to display the message to
     */
    public static void send(Component msg, @NotNull CommandSender receiver) {
        receiver.sendMessage(msg);
    }

    /**
     * Sends a component chat message to the whole server
     * @param msg The message to send
     */
    public static void broadcast(Component msg) {
        Bukkit.getServer().sendMessage(msg);
    }

    /**
     * Sends an error message to a user
     * @param message The message to send
     * @param receiver Who to send the message to
     */
    public static void sendError(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(DARK_RED);
        receiver.sendMessage(mm.deserialize("<gold>[!]</gold> ").append(msg));
    }

    /**
     * Sends an MM string with error formatting to the whole server
     * @param message The MM string to broadcast
     */
    public static void broadcastError(String message) {
        Component msg = mm.deserialize(message).color(DARK_RED);
        Bukkit.getServer().sendMessage(mm.deserialize("<gold>[!]</gold> ").append(msg));
    }

    public static void sendActionError(String message, @NotNull Player receiver) {
        Component msg = mm.deserialize(message).color(DARK_RED);
        receiver.sendActionBar(mm.deserialize("<gold>[!]</gold> ").append(msg));
    }

    public static void sendWarning(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(RED);
        receiver.sendMessage(mm.deserialize("<gold>[!]</gold> ").append(msg));
    }

    /**
     * Sends an MM string with warning formatting to the whole server
     * @param message The MM string to broadcast
     */
    public static void broadcastWarning(String message) {
        Component msg = mm.deserialize(message).color(RED);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage( mm.deserialize("<gold>[!] </gold>").append(msg));
    }

    public static void sendActionWarning(String message, @NotNull Player receiver) {
        Component msg = mm.deserialize(message).color(RED);
        receiver.sendActionBar(mm.deserialize("<gold>[!]</gold> ").append(msg));
    }

    /**
     * Sends an MM string with tip formatting to the whole server
     * @param message The MM string to broadcast
     */
    public static void broadcastTip(String message) {
        Component msg = mm.deserialize(message).color(AQUA);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[i]</gold> ").append(msg));
    }

    /**
     * Sends a message about a secret to a user
     * @param message The message to send
     * @param receiver Who to send the message to
     */
    public static void sendSecret(String message, CommandSender receiver) {
        Component msg = mm.deserialize(message).color(YELLOW);
        receiver.sendMessage(mm.deserialize("<gold>[\uD83D\uDDDD️]</gold> ").append(msg));
    }

    /**
     * Sends an MM string with secret formatting to the whole server
     * @param message The MM string to broadcast
     */
    public static void broadcastSecret(String message) {
        Component msg = mm.deserialize(message).color(YELLOW);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[\uD83D\uDDDD]</gold> ").append(msg));
    }

    /**
     * Sends a success message to a user
     * @param message The message to send
     * @param receiver Who to send the message to
     */
    public static void sendSuccess(String message, CommandSender receiver) {
        Component msg = mm.deserialize(message).color(GREEN);
        receiver.sendMessage(mm.deserialize("<gold>[+]</gold> ").append(msg));
    }

    /**
     * Sends a success message to a user via action message
     * @param message The message to send
     * @param receiver Who to send the message to
     */
    public static void sendActionSuccess(String message, @NotNull Player receiver) {
        Component msg = mm.deserialize(message).color(GREEN);
        receiver.sendActionBar(msg);
    }

    /**
     * Sends an MM string with success formatting to the whole server
     * @param message The MM string to broadcast
     */
    public static void broadcastSuccess(Component message) {
        message = message.color(GREEN);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[+]</gold> ").append(message));
    }

    public static void sendInfo(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(BLUE);
        receiver.sendMessage(mm.deserialize("<gold>[i]</gold> ").append(msg));
    }

    public static void sendInfo(Component message, @NotNull CommandSender receiver) {
        receiver.sendMessage(mm.deserialize("<gold>[i]</gold> ").append(message.color(BLUE)));
    }

    public static void sendActionInfo(String message, @NotNull Player receiver) {
        receiver.sendActionBar(mm.deserialize(message).color(BLUE));
    }

    /**
     * Sends a Component with info formatting to the whole server
     * @param message The Component to broadcast
     */
    public static void broadcastInfo(Component message) {
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[i]</gold> ").append(message.color(BLUE)));
    }

    /**
     * Sends an MM string with info formatting to the whole server
     * @param message The MM string to broadcast
     */
    public static void broadcastInfo(String message) {
        Component msg = mm.deserialize(message).color(BLUE);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[i]</gold> ").append(msg));
    }

    public static void sendBounty(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(YELLOW);
        receiver.sendMessage(mm.deserialize("<gold>[⌖]</gold> ").append(msg));
    }

    public static void broadcastPaidBounty(String payee, String bountied, int amount, int total) {
        Component msg = mm.deserialize("<gold>[⌖]</gold> <yellow>" + payee + " added " + amount
                + " to " + bountied + "'s bounty! The new total is <GOLD>"
                + total + "</gold>!");
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(msg);
    }

    public static void broadcastKillStreakBounty(String bountied, int kills, int total) {
        Component msg = mm.deserialize("<gold>[⌖]</gold> " + bountied + " has reached a <aqua>"
                + kills + "</aqua> kill streak! Their bounty has increased to <gold>" + total + "</gold>!");
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(msg);
    }

    public static void broadcastBountyClaimed(String bountied, String killer, String assistant, int amount) {
        Component msg = mm.deserialize("<gradient:#F56545:#99201C>[⌖] " + bountied
                + " was killed by " + killer + " and " + assistant
                + "! They shared the <gold>" + amount + "</gold> coin bounty between them.");
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(msg);
    }

    public static void broadcastBountyClaimed(String bountied, String killer, int amount) {
        Component msg = mm.deserialize("<gradient:#F56545:#99201C>[⌖] " + bountied
                + " was killed by " + killer + " and they claimed the <gold>"
                + amount + "</gold> coin bounty!");
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(msg);
    }

    public static void requestInput(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(LIGHT_PURPLE);
        receiver.sendMessage(mm.deserialize("<gold>[_]</gold> ").append(msg));
    }

    /**
     * Sends an MM string with curse formatting to the whole server
     * @param message The MM string to broadcast
     */
    public static void broadcastCurse(String message) {
        Component msg = mm.deserialize(message).color(RED);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[☠]</gold> ").append(msg));
    }

    /**
     * Sends a cursed message to a specific user
     * @param message The message
     * @param receiver Who to send the message to
     */
    public static void sendCurse(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(RED);
        receiver.sendMessage(mm.deserialize("<gold>[☠]</gold> ").append(msg));
    }

    /**
     * Broadcasts a cursed end message to everyone
     * @param message The message to send
     */
    public static void broadcastCurseEnd(String message) {
        Component msg = mm.deserialize(message).color(GREEN);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[☠]</gold> ").append(msg));
    }

    /**
     * Sends a cursed message to a specific user
     * @param message The message
     * @param receiver Who to send the message to
     */
    public static void sendCurseEnd(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(GREEN);
        receiver.sendMessage(mm.deserialize("<gold>[☠]</gold> ").append(msg));
    }

    public static void sendHealing(String message, @NotNull CommandSender receiver) {
        receiver.sendMessage(mm.deserialize("<gradient:#FF1053:#F7ACCF>[❤] " + message));
    }

    public static void sendActionSpawn(String name, NamedTextColor primary, NamedTextColor secondary, @NotNull CommandSender receiver) {
        Component msg = Component.text("Spawning at: ").color(secondary)
                .append(Component.text(name).color(primary));
        receiver.sendActionBar(msg);
    }

    public static void sendActionHit(String playerHit, @NotNull CommandSender receiver) {
        Component msg = Component.text("Hit (" + playerHit + ")").color(AQUA);
        receiver.sendActionBar(msg);
    }

    public static void sendDuel(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize("<gradient:#48A9FE:#0BEEF9>" + message + "</gradient>");
        receiver.sendMessage(mm.deserialize("<gold>[⚔]</gold> ").append(msg));
    }

    /**
     * Sends an MM string with duel formatting to the whole server
     * @param message The MM string to broadcast
     */
    public static void broadcastDuel(String message) {
        Component msg = mm.deserialize(message).color(BLUE);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[⚔]</gold> ").append(msg));
    }

    public static void sendCongrats(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(DARK_GREEN);
        receiver.sendMessage(mm.deserialize("<gold>[+]</gold> ").append(msg));
    }

    /**
     * Sends an MM string with congrats formatting to the whole server
     * @param message The MM string to broadcast
     */
    public static void broadcastCongrats(String message) {
        Component msg = mm.deserialize(message).color(DARK_GREEN);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[+]</gold> ").append(msg));
    }
}
