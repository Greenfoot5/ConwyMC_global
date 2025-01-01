package me.greenfoot5.conwymc.commands.chat;

import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.database.ActiveData;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL;

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
        return data.getCosmetics().getChatColour();
    }

    /**
     * Plays the tag sound to the tagged player
     * @param tagged The player who's been tagged
     */
    public static void playTagSound(Audience tagged) {
        float volume = 1f; //1 = 100%
        float pitch = 0.5f; //Float between 0.5 and 2.0

        Sound sound = Sound.sound().type(BLOCK_NOTE_BLOCK_BELL)
                .pitch(pitch).volume(volume).build();

        tagged.playSound(sound);
    }
}
