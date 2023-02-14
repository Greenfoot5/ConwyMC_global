package me.huntifi.conwymc.commands.staff.chat;

import me.huntifi.conwymc.commands.chat.ToggleChatCommand;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Toggles staff chat mode or sends a message in staff chat
 */
public class StaffChatCommand extends ToggleChatCommand {

    @Override
    protected boolean shouldTagPlayer(Player player, String message) {
        return super.shouldTagPlayer(player, message) && player.hasPermission("conwymc.chatmod");
    }

    @Override
    protected void toggleChatMode(Player player) {
        PlayerData data = ActiveData.getData(player.getUniqueId());
        if (data.getChatMode().equalsIgnoreCase("staff")) {
            data.setChatMode("global");
            Messenger.sendInfo("You are no longer talking in staff-chat!", player);
        } else {
            data.setChatMode("staff");
            Messenger.sendInfo("You are now talking in staff-chat!", player);
        }
    }

    @Override
    protected void sendMessage(CommandSender sender, String message) {
        String staffMessage = String.format("%s %sSTAFF: %s%s", getName(sender), ChatColor.AQUA, getChatColor(sender), message);
        System.out.println(staffMessage);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("conwymc.chatmod"))
                player.sendMessage(staffMessage);
        }
    }
}
