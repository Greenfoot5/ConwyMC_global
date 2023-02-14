package me.huntifi.conwymc.commands.chat;

import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

/**
 * Toggles global chat mode or sends a message in global chat
 */
public class GlobalChatCommand extends ToggleChatCommand {

    @Override
    protected void toggleChatMode(Player player) {
        PlayerData data = ActiveData.getData(player.getUniqueId());
        data.setChatMode("global");
        Messenger.sendInfo("You are now talking in global-chat!", player);
    }

    @Override
    protected void sendMessage(CommandSender sender, String message) {
        getServer().broadcastMessage(String.format("%s: %s%s", getName(sender), getChatColor(sender), message));
    }
}
