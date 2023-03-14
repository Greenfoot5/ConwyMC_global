package me.huntifi.conwymc.database;

import me.huntifi.conwymc.ConwyMC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Configures player permissions
 */
public class Permissions {

    private static final String prefix = "conwymc.";

    private static final HashMap<UUID, PermissionAttachment> perms = new HashMap<>();

    /**
     * Store the player's permission attachment to enable permission manipulations
     * @param uuid The unique ID of the player
     */
    public static void addPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;

        PermissionAttachment attachment = player.addAttachment(ConwyMC.getPlugin());
        perms.put(uuid, attachment);
    }

    /**
     * Remove the player's permission attachment from storage
     * @param uuid The unique ID of the player
     */
    public static void removePlayer(UUID uuid) {
        perms.remove(uuid);
    }

    /**
     * Set the player's staff rank permission
     * @param uuid The unique ID of the player
     * @param permission The permission to set
     */
    public static boolean setStaffPermission(UUID uuid, String permission) {
        Collection<String> staffPerms = Arrays.asList("owner", "admin", "communitymanager", "developer",
                "moderator", "chatmod+", "chatmod", "builder", "");
        if (staffPerms.contains(permission.toLowerCase())) {
            setPermission(uuid, ActiveData.getData(uuid).getStaffRank(), permission.toLowerCase());
            return true;
        }

        return false;
    }

    /**
     * Set the player's donator rank permission
     * @param uuid The unique ID of the player
     * @param permission The permission to set
     */
    public static void setDonatorPermission(UUID uuid, String permission) {
        Collection<String> donatorPerms = Arrays.asList("high_king", "king", "viceroy", "duke",
                "count", "baron", "noble", "esquire", "");
        if (donatorPerms.contains(permission))
            setPermission(uuid, ActiveData.getData(uuid).getRank(), permission);
    }

    /**
     * Set the player's rank permission
     * @param uuid The unique ID of the player
     * @param currentPerm The current permission to remove
     * @param newPerm The permission to set
     */
    private static void setPermission(UUID uuid, String currentPerm, String newPerm) {
        // Remove old permission and set new one
        PermissionAttachment attachment = perms.get(uuid);
        if (!currentPerm.isEmpty())
            attachment.unsetPermission(prefix + currentPerm);
        if (!newPerm.isEmpty())
            attachment.setPermission(prefix + newPerm, true);

        // Update commands list
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        player.updateCommands();
    }
}
