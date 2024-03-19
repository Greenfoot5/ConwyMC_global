package me.huntifi.conwymc.commands.staff.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.huntifi.conwymc.commands.chat.GlobalChatCommand;
import me.huntifi.conwymc.commands.chat.ToggleChatCommand;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

/**
 * Toggles staff chat mode or sends a message in staff chat
 */
public class StaffChatCommand extends ToggleChatCommand {

    /** The string representing staff chat mode */
    public static final String CHAT_MODE = "staff";

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncChatEvent e) {

        PlayerData data = ActiveData.getData(e.getPlayer().getUniqueId());
        // This can be overridden by changing the message before
        if (e.originalMessage() == e.message() && data.getChatMode().equalsIgnoreCase(CHAT_MODE))
            e.renderer(this);
    }

    @Override
    protected void toggleChatMode(Player player) {
        PlayerData data = ActiveData.getData(player.getUniqueId());
        if (data.getChatMode().equals(CHAT_MODE)) {
            data.setChatMode(GlobalChatCommand.CHAT_MODE);
            Messenger.sendInfo("You are no longer talking in staff-chat!", player);
        } else {
            data.setChatMode(CHAT_MODE);
            Messenger.sendInfo("You are now talking in staff-chat!", player);
        }
    }

    @Override
    protected ForwardingAudience getReceivers(CommandSender sender) {
        ForwardingAudience receivers = Bukkit.getServer();
        receivers.filterAudience(v -> (v.get(Identity.UUID).isEmpty() || ((Player)v).hasPermission("castlesiege.chatmod")));
        return receivers;
    }

    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        String color = getChatColor(source);
        Component prefix = Messenger.mm
                .deserialize("<white>[<aqua><b>STAFF</b></aqua>] " +
                        source.getName() + "</white>");

        Component content = Messenger.mm.deserialize(color + message);
        return prefix.append(Component.text(": ", NamedTextColor.AQUA))
                .append(content);
    }
}
