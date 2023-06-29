package me.huntifi.conwymc.commands.staff.chat;

import me.huntifi.conwymc.commands.chat.GlobalChatCommand;
import me.huntifi.conwymc.commands.chat.ToggleChatCommand;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Toggles staff chat mode or sends a message in staff chat
 */
public class StaffChatCommand extends ToggleChatCommand {

    /** The string representing staff chat mode */
    public static final String CHAT_MODE = "staff";

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
    protected Collection<CommandSender> getReceivers() {
        Collection<CommandSender> receivers = new ArrayList<>(Bukkit.getOnlinePlayers());
        receivers.removeIf(receiver -> !receiver.hasPermission("conwymc.chatmod"));
        receivers.add(Bukkit.getConsoleSender());
        return receivers;
    }

    @Override
    protected String getFormattedMessage(CommandSender sender, String message) {
        return String.format("%s %sSTAFF: %s%s", getName(sender), ChatColor.AQUA, getChatColor(sender), message);
    }
}
