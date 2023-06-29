package me.huntifi.conwymc.commands.chat;

import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Toggles global chat mode or sends a message in global chat
 */
public class GlobalChatCommand extends ToggleChatCommand {

    /** The string representing global chat mode */
    public static final String CHAT_MODE = "global";

    @Override
    protected void toggleChatMode(Player player) {
        PlayerData data = ActiveData.getData(player.getUniqueId());
        data.setChatMode(CHAT_MODE);
        Messenger.sendInfo("You are now talking in global-chat!", player);
    }

    @Override
    protected Collection<CommandSender> getReceivers() {
        Collection<CommandSender> receivers = new ArrayList<>(Bukkit.getOnlinePlayers());
        receivers.add(Bukkit.getConsoleSender());
        return receivers;
    }

    @Override
    protected String getFormattedMessage(CommandSender sender, String message) {
        return String.format("%s: %s%s", getName(sender), getChatColor(sender), message);
    }
}
