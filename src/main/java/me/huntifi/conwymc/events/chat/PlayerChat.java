package me.huntifi.conwymc.events.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.huntifi.conwymc.commands.chat.GlobalChatCommand;
import me.huntifi.conwymc.commands.staff.chat.StaffChatCommand;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Customises a player's chat message
 */
public class PlayerChat implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncChatEvent e) {

        PlayerData data = ActiveData.getData(e.getPlayer().getUniqueId());
        // This can be overridden by changing the message before
        if (e.originalMessage() == e.message() && data.getChatMode().equalsIgnoreCase(GlobalChatCommand.CHAT_MODE))
            e.renderer(new GlobalChatCommand());
        if (data.getChatMode().equalsIgnoreCase(StaffChatCommand.CHAT_MODE))
            e.renderer(new StaffChatCommand());
    }
}
