package me.greenfoot5.conwymc.commands.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.greenfoot5.conwymc.ConwyMC;
import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.database.ActiveData;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
    protected Audience getReceivers(CommandSender sender) {
        return Bukkit.getServer();
    }

    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        String color = getChatColor(source);
        String clean = Messenger.clean(message);

        String name = Messenger.clean(viewer.getOrDefault(Identity.NAME, "No name!"));
        if (clean.contains(name + "@")) {
            playTagSound(viewer);
            clean = clean.replace(name + "@", "<aqua>" + name + "@</aqua>");
        }

        Component content = Messenger.mm.deserialize(color + clean);
        return sourceDisplayName.append(Component.text(": ", NamedTextColor.WHITE))
                .append(content);
    }
}
