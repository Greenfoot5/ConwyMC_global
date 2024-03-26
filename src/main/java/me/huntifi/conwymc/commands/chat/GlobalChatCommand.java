package me.huntifi.conwymc.commands.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

/**
 * Toggles global chat mode or sends a message in global chat
 */
public class GlobalChatCommand extends ToggleChatCommand {

    /**
     * Disables the option to send a message
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return True
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) {
            Messenger.sendInfo("You can no longer send global messages individually. Please change your chat mode to global instead.", sender);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> {
            if (sender instanceof Player)
                toggleChatMode((Player) sender);
            else
                Messenger.sendError("Console cannot toggle chat modes", sender);
        });

        return true;
    }

    /** The string representing global chat mode */
    public static final String CHAT_MODE = "global";

    /**
     * Used to set the renderer for when a player chats in global
     * @param e The player chat event
     */
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
        data.setChatMode(CHAT_MODE);
        Messenger.sendInfo("You are now talking in global-chat!", player);
    }

    @Override
    protected ForwardingAudience getReceivers(CommandSender sender) {
        return Bukkit.getServer();
    }

    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        String color = getChatColor(source);

        Component content = Messenger.mm.deserialize(color + Messenger.clean(message));
        return sourceDisplayName.append(Component.text(": "))
                .append(content);
    }
}
