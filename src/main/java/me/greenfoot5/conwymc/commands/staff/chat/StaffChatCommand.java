package me.greenfoot5.conwymc.commands.staff.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.greenfoot5.conwymc.commands.chat.GlobalChatCommand;
import me.greenfoot5.conwymc.commands.chat.ToggleChatCommand;
import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.database.ActiveData;
import me.greenfoot5.conwymc.util.Messenger;
import me.greenfoot5.conwymc.util.NameTag;
import net.kyori.adventure.audience.Audience;
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
    public Audience getReceivers(CommandSender sender) {
        Audience receivers = Bukkit.getServer();
        receivers = receivers.filterAudience(v -> (v.get(Identity.UUID).isEmpty() || ((Player)v).hasPermission("conwymc.chatmod")));


        return receivers;
    }

    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        PlayerData data = ActiveData.getData(source.getUniqueId());
        Component prefix = Messenger.mm
                .deserialize("<white>[<aqua><b>STAFF</b></aqua>] </white>")
                .append(NameTag.convertRank(data.getStaffRank()))
                .append(Messenger.mm.deserialize(" <white>" + source.getName() + "</white>"));

        Component content = Messenger.mm.deserialize(Messenger.clean(message)).color(NamedTextColor.AQUA);
        return prefix.append(Component.text(": ", NamedTextColor.WHITE))
                .append(content);
    }
}
