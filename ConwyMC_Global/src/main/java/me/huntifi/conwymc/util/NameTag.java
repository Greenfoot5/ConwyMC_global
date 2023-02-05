package me.huntifi.conwymc.util;

public class NameTag {

    /**
     * Get the pretty representation of the player's staff or donator tag
     * @param rank The rank to convert
     * @return A formatted rank or an empty string if invalid
     */
    public static String convertRank(String rank) {
        switch (rank) {
            // Staff Ranks
            case "builder":
                return "§b§lBuilder ";
            case "chatmod":
                return "§9§lChatMod ";
            case "chatmod+":
                return "§1§lChatMod+ ";
            case "moderator":
                return "§a§lMod ";
            case "developer":
                return "§2§lDev ";
            case "communitymanager":
                return "§5§lComm Man ";
            case "admin":
                return "§c§lAdmin ";
            case "owner":
                return "§6§lOwner ";
            // Donator Ranks
            case "esquire":
                return "§3Esquire ";
            case "noble":
                return "§aNoble ";
            case "baron":
                return "§5Baron ";
            case "count":
                return "§6Count ";
            case "duke":
                return "§4Duke ";
            case "viceroy":
                return "§dViceroy ";
            case "king":
                return "§eKing ";
            case "high_king":
                return "§eHigh King ";
            default:
                return "";
        }
    }
}
