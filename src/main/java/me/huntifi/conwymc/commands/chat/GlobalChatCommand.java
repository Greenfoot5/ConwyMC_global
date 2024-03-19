package me.huntifi.conwymc.commands.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

/**
 * Toggles global chat mode or sends a message in global chat
 */
public class GlobalChatCommand extends ToggleChatCommand {

    /** The string representing global chat mode */
    public static final String CHAT_MODE = "global";

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncChatEvent e) {

        PlayerData data = ActiveData.getData(e.getPlayer().getUniqueId());
        // This can be overridden by changing the message before
        if (e.originalMessage() == e.message() && data.getChatMode().equalsIgnoreCase(CHAT_MODE))
            e.renderer(this);
    }

    @Override
    protected void toggleChatMode(Player player) {
        PlayerData data = ActiveData.getData(player.getUniqueId());
        data.setChatMode(CHAT_MODE);
        Messenger.sendInfo("You are now talking in global-chat!", player);
    }

    @Override
    protected ForwardingAudience getReceivers(CommandSender sender) {
        return Bukkit.getServer();
    }

    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        String color = getChatColor(source);

        Component content = Messenger.mm.deserialize(color + message);
        return getName(source).append(Component.text(": "))
                .append(content);
    }
}
