package me.huntifi.conwymc.commands.chat;

import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Sends a message
 */
public abstract class ChatCommand implements CommandExecutor {

    /**
     * Get the sender's name.
     * @param sender The sender of the message
     * @return The sender's name
     */
    protected Component getName(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).displayName() : Component.text(sender.getName());
    }

    /**
     * Get the sender's chat color.
     * @param sender The sender of the message
     * @return The sender's chat color
     */
    protected String getChatColor(CommandSender sender) {
        if (!(sender instanceof Player))
            return "<white>";

        Player player = (Player) sender;
        PlayerData data = ActiveData.getData(player.getUniqueId());
        return data.getMMChatColor(player.getName());
    }

    /**
     * Play the tag sound for a tagged player.
     * @param player The tagged player
     */
    protected void playTagSound(Player player) {
        Location location = player.getLocation();
        Sound sound = Sound.BLOCK_NOTE_BLOCK_BELL;
        float volume = 1f; // 1 = 100%
        float pitch = 0.5f; // Float between 0.5 and 2.0

        player.playSound(location, sound, volume, pitch);
    }
}
