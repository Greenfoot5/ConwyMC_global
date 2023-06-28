package me.huntifi.conwymc.commands.info;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Shows the player the rules
 */
public class RulesCommand implements CommandExecutor {

    private final static String[] rules = {
            "Hacks and Mods are not allowed, if you attempt to hack you will get banned without warning!",
            "Xray is not allowed, that includes transparent blocks!",
            "No abusing bugs. Report a bug immediately. Do not tell others how to use the bug.",
            "No spamming. Do not say the same thing more than once or twice. Do not spam chat with arguments.",
            "No trolling. We have a zero-tolerance policy for trolling. We know what trolling is and it will not be tolerated.",
            "Use English in the server chat.",
            "Do not advertise other servers, unless they are a sub-community of TheDarkAge/Conwy.",
            "Do not use hacked clients. Zero-tolerance policy. You will be banned without warning!",
            "Do not insult other players and don't be a racist, no hate-speech or bullying.",
            "Staff have a final say. Do not attempt to argue against punishment decisions once a final decision is given.",
            "Do not pvp log.",
            "Do not evade punishments.",
            "Do not post NSFW content."
    };

    private final static String border = "-----------------------------------------------------";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        StringBuilder message = new StringBuilder();
        message.append("The list of rules everyone must follow! ").append(ChatColor.BOLD).append("Don't break them.\n");
        message.append(ChatColor.WHITE).append(border).append("\n");

        for (int i = 0; i < rules.length; i++) {
            ChatColor color = (i % 2 == 0) ? ChatColor.WHITE: ChatColor.GRAY;
            message.append(ChatColor.YELLOW).append(i + 1).append(color).append(") ").append(rules[i]).append("\n");
        }

        message.append(ChatColor.WHITE).append(border);
        sender.sendMessage(message.toString());
        return true;
    }
}