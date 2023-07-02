package me.huntifi.conwymc.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getServer;

/**
 * A class to handle sending messages to make them consistent
 */
public class Messenger {

    /** Icon color */
    private static final ChatColor ICON = ChatColor.GOLD;
    /** Primary error color */
    public static final ChatColor ERROR_PRIMARY = ChatColor.DARK_RED;
    /** Secondary error color */
    public static final ChatColor ERROR_SECONDARY = ChatColor.RED;
    /** Primary warning color */
    public static final ChatColor WARNING_PRIMARY = ChatColor.RED;
    /** Secondary warning color */
    public static final ChatColor WARNING_SECONDARY = ChatColor.LIGHT_PURPLE;
    /** Primary tip color */
    public static final ChatColor TIP_PRIMARY = ChatColor.AQUA;
    /** Secondary tip color */
    public static final ChatColor TIP_SECONDARY = ChatColor.DARK_AQUA;
    /** Primary secret color */
    public static final ChatColor SECRET_PRIMARY = ChatColor.YELLOW;
    /** Secondary secret color */
    public static final ChatColor SECRET_SECONDARY = ChatColor.WHITE;
    /** Primary success color */
    public static final ChatColor SUCCESS_PRIMARY = ChatColor.GREEN;
    /** Secondary success color */
    public static final ChatColor SUCCESS_SECONDARY = ChatColor.YELLOW;
    /** Primary info color */
    public static final ChatColor INFO_PRIMARY = ChatColor.BLUE;
    /** Secondary info color */
    public static final ChatColor INFO_SECONDARY = ChatColor.DARK_AQUA;

    /**
     * Send an error message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendError(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ICON + "[!] " + ERROR_PRIMARY + message);
    }

    /**
     * Send an action error message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendActionError(String message, @NotNull Player sender) {
        sender.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ICON + "[!] " + ERROR_PRIMARY + message)
        );
    }

    /**
     * Send a warning message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendWarning(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ICON + "[!] "+ WARNING_PRIMARY + message);
    }

    /**
     * Broadcast a warning message to all users
     * @param message The message to broadcast
     */
    public static void broadcastWarning(String message) {
        getServer().broadcastMessage(ICON + "[!] " + WARNING_PRIMARY + message);
    }

    /**
     * Send a tip message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendTip(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ICON + "[i] " + TIP_PRIMARY + message);
    }

    /**
     * Broadcasts a tip message to all users
     * @param message The message to broadcast
     */
    public static void broadcastTip(String message) {
        getServer().broadcastMessage(ICON + "[i] " + TIP_PRIMARY + message);
    }

    /**
     * Send a message about a secret to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendSecret(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ICON + "[S] " + SECRET_PRIMARY + message);
    }

    /**
     * Broadcast a message about a secret to all users
     * @param message The message to broadcast
     */
    public static void broadcastSecret(String message) {
        getServer().broadcastMessage(ICON + "[S] " + SECRET_PRIMARY + message);
    }

    /**
     * Send a success message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendSuccess(String message, CommandSender sender) {
        sender.sendMessage(ICON + "[+] " + SUCCESS_PRIMARY + message);
    }

    /**
     * Send an action success message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendActionSuccess(String message, @NotNull Player sender) {
        sender.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ICON + "[+] " + SUCCESS_PRIMARY + message));
    }

    /**
     * Broadcast a success message to everyone
     * @param message The message to send
     */
    public static void broadcastSuccess(String message) {
        getServer().broadcastMessage(ICON + "[+] " + SUCCESS_PRIMARY + message);
    }

    /**
     * Send an info message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendInfo(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ICON + "[i] " + INFO_PRIMARY + message);
    }

    /**
     * Send an action info message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendActionInfo(String message, @NotNull Player sender) {
        sender.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ICON + "[i] " + INFO_PRIMARY + message)
        );
    }

    /**
     * Broadcast an info message to all users
     * @param message The message to broadcast
     */
    public static void broadcastInfo(String message) {
        getServer().broadcastMessage(ICON + "[i] " + INFO_PRIMARY + message);
    }
}
