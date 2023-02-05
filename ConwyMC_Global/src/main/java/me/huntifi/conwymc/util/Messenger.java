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

    /**
     * Send an error message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendError(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + message);
    }

    /**
     * Send an action error message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendActionError(String message, @NotNull Player sender) {
        sender.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + message)
        );
    }

    /**
     * Send a warning message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendWarning(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[!] "+ ChatColor.DARK_RED + "You were warned for: " + ChatColor.RED + message);
    }

    /**
     * Broadcast a warning message to all users
     * @param message The message to broadcast
     */
    public static void broadcastWarning(String message) {
        getServer().broadcastMessage(ChatColor.GOLD + "[!] " + ChatColor.RED + message);
    }

    /**
     * Send a tip message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendTip(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[i] " + ChatColor.AQUA + message);
    }

    /**
     * Broadcasts a tip message to all users
     * @param message The message to broadcast
     */
    public static void broadcastTip(String message) {
        getServer().broadcastMessage(ChatColor.GOLD + "[i] " + ChatColor.AQUA + message);
    }

    /**
     * Send a message about a secret to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendSecret(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[S] " + ChatColor.YELLOW + message);
    }

    /**
     * Broadcast a message about a secret to all users
     * @param message The message to broadcast
     */
    public static void broadcastSecret(String message) {
        getServer().broadcastMessage(ChatColor.GOLD + "[S] " + ChatColor.YELLOW + message);
    }

    /**
     * Send a lore message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendLore(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[l] " + ChatColor.GREEN + message);
    }

    /**
     * Broadcast a lore message to all users
     * @param message The message to broadcast
     */
    public static void broadcastLore(String message) {
        getServer().broadcastMessage(ChatColor.GOLD + "[l] " + ChatColor.GREEN + message);
    }

    /**
     * Send an info message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendInfo(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[i] " + ChatColor.BLUE + message);
    }

    /**
     * Send an action info message to a user
     * @param message The message to send
     * @param sender To whom the message should be sent
     */
    public static void sendActionInfo(String message, @NotNull Player sender) {
        sender.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ChatColor.GOLD + "[i] " + ChatColor.BLUE + message)
        );
    }

    /**
     * Broadcast an info message to all users
     * @param message The message to broadcast
     */
    public static void broadcastInfo(String message) {
        getServer().broadcastMessage(ChatColor.GOLD + "[i] " + ChatColor.BLUE + message);
    }
}
