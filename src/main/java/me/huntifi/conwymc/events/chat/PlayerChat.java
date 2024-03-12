package me.huntifi.conwymc.events.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.commands.chat.GlobalChatCommand;
import me.huntifi.conwymc.commands.staff.chat.StaffChatCommand;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL;

/**
 * Customises a player's chat message
 */
public class PlayerChat implements Listener, ChatRenderer {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncChatEvent e) {

        PlayerData data = ActiveData.getData(e.getPlayer().getUniqueId());
        if (e.originalMessage() == e.message() && data.getChatMode().equalsIgnoreCase(GlobalChatCommand.CHAT_MODE))
            e.renderer(this);
    }

    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        String color = getChatColor(source);

        String content = PlainTextComponentSerializer.plainText().serialize(message);
        if (content.contains("@" + viewer.get(Identity.NAME))) {
            playTagSound(viewer);
        }
        message = MiniMessage.miniMessage().deserialize(color + content);

        // Console
        if (viewer.get(Identity.NAME).isEmpty()) {
            return sourceDisplayName.append(Component.text(": ")).append(message);
        }

        return sourceDisplayName.color(NamedTextColor.WHITE).append(Component.text(": "))
                .append(message);
    }

    public static void playTagSound(Audience viewer) {
        float volume = 1f; //1 = 100%
        float pitch = 0.5f; //Float between 0.5 and 2.0

        Sound sound = Sound.sound().type(BLOCK_NOTE_BLOCK_BELL)
                .pitch(pitch).volume(volume).build();

        viewer.playSound(sound);
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
     * Send chat messages in staff chat when staff chat mode is enabled.
     * @param event The event called when a player sends a message
     */
    @EventHandler (ignoreCancelled = true)
    public void onStaffChat(AsyncPlayerChatEvent event) {
        PlayerData data = ActiveData.getData(event.getPlayer().getUniqueId());
        if (data.getChatMode().equalsIgnoreCase(StaffChatCommand.CHAT_MODE)) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(ConwyMC.plugin, () ->
                    event.getPlayer().performCommand("StaffChat " + event.getMessage())
            );
        }
    }
}
