package me.huntifi.conwymc.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * Utility functions related to player name tags
 */
public class NameTag {

    /**
     * Get the pretty representation of the player's staff or donator tag
     * @param rank The rank to convert
     * @return A formatted rank or an empty string if invalid
     */
    public static Component convertRank(String rank) {
        switch (rank) {
            // Staff Ranks
            case "designer":
                return MiniMessage.miniMessage().deserialize("<b><gradient:#905ea9:#eaaded>Design</gradient></b> ");
            case "builder":
                return MiniMessage.miniMessage().deserialize("<b><gradient:#80CED7:#007EA7>Builder</gradient></b> ");
            case "chatmod":
                return MiniMessage.miniMessage().deserialize("<b><gradient:#08C8F6:#4D5DFB>ChatMod</gradient></b> ");
            case "chatmod+":
                return MiniMessage.miniMessage().deserialize("<b><gradient:#2876F9:#6D17CB>ChatMod+</gradient></b>  ");
            case "moderator":
                return MiniMessage.miniMessage().deserialize("<b><gradient:#2db379:#1c8c70>Mod</gradient></b> ");
            case "developer":
                return MiniMessage.miniMessage().deserialize("<b><gradient:#00BF00:#1A913E>Dev</gradient></b> ");
            case "communitymanager":
                return MiniMessage.miniMessage().deserialize("<b><gradient:#a422e6:#7830bf>Comm Man</gradient></b> ");
            case "admin":
                return MiniMessage.miniMessage().deserialize("<b><gradient:#A82533:#EF4848>Admin</gradient></b> ");
            case "owner":
                return MiniMessage.miniMessage().deserialize("<b><dark_red><obf><st>!</dark_red><gradient:#FFAA00:#FF5500>Owner</gradient><dark_red><obf><st>!</dark_red></b> ");
            // Donator Ranks
            case "esquire":
                return MiniMessage.miniMessage().deserialize("<dark_aqua>Esquire</dark_aqua> ");
            case "noble":
                return MiniMessage.miniMessage().deserialize("<green>Noble</green> ");
            case "baron":
                return MiniMessage.miniMessage().deserialize("<dark_purple>Baron</dark_purple> ");
            case "count":
                return MiniMessage.miniMessage().deserialize("<gold>Count</gold> ");
            case "duke":
                return MiniMessage.miniMessage().deserialize("<dark_red>Duke</dark_red> ");
            case "viceroy":
                return MiniMessage.miniMessage().deserialize("<gradient:#be1fcc:#d94cd9>Viceroy</gradient> ");
            case "king":
                return MiniMessage.miniMessage().deserialize("<gradient:#F07654:#F5DF2E:#F07654>⚜King⚜</gradient> ");
            case "high_king":
                return MiniMessage.miniMessage().deserialize("<gradient:#FFED00:#FF0000>👑High King👑</gradient> ");
            default:
                return Component.empty();
        }
    }
}
