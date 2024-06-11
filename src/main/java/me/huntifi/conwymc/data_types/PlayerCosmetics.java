package me.huntifi.conwymc.data_types;

import me.huntifi.conwymc.events.nametag.UpdateNameTagEvent;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Objects;

import static me.huntifi.conwymc.data_types.Cosmetic.CosmeticType.CHAT_COLOUR;
import static me.huntifi.conwymc.data_types.Cosmetic.CosmeticType.JOIN_COLOUR;
import static me.huntifi.conwymc.data_types.Cosmetic.CosmeticType.LEAVE_COLOUR;
import static me.huntifi.conwymc.data_types.Cosmetic.CosmeticType.TITLE;

public class PlayerCosmetics {

    /** The player's title */
    private Cosmetic title;

    /** The player's chat colour */
    private Cosmetic chatColour;

    /** The player's custom join message */
    private String joinMessage;

    /** The mm prefix to the join message */
    private Cosmetic joinColour;

    /** The player's custom leave message */
    private String leaveMessage;

    /** The mm prefix to the leave message */
    private Cosmetic leaveColour;

    public PlayerCosmetics(HashMap<String, String> settings, PlayerData data) {

        setJoinMessage(settings.get("join_message"), data);
        setJoinColour(settings.get("join_colour"), data);
        setLeaveMessage(settings.get("leave_message"), data);
        setLeaveColour(settings.get("leave_colour"), data);
        setTitle(settings.get("title"), data);
        setChatColour(settings.get("chat_colour"), data);
    }

    public String getRawTitle() {
        return title.getValue();
    }

    public Component getTitle() {
        if (title == null)
            return null;
        return Messenger.mm.deserialize(title.getValue());
    }

    public void setTitle(String title, PlayerData data) {
        if (Objects.equals(title, "reset"))
            this.title = null;
        else
            this.title = data.getCosmetic(title, TITLE);
    }

    public String getChatColour() {
        return chatColour.getValue();
    }

    public void setChatColour(String chatColour, PlayerData data) {
        if (chatColour != null && !chatColour.equals("reset"))
            this.chatColour = data.getCosmetic(chatColour, CHAT_COLOUR);
        else if (data.getStaffRank() == null)
            this.chatColour = new Cosmetic(CHAT_COLOUR, "Default", "<grey>");
        else
            this.chatColour = new Cosmetic(CHAT_COLOUR, "Staff", "<white>");
    }

    public Component getJoinMessage(String username) {
        String message = this.joinMessage.replace("{username}", username);
        return Messenger.mm.deserialize(this.joinColour + message);
    }

    public void setJoinColour(String joinColour, PlayerData data) {
        this.joinColour = joinColour.isEmpty() || joinColour.equals("reset") ?
                new Cosmetic(JOIN_COLOUR, "Default", "<yellow>") :
                data.getCosmetic(joinColour, JOIN_COLOUR);
    }

    public void setJoinMessage(String joinMessage, PlayerData playerData) {
        if (playerData.getRank() == null)
            setJoinMessage(joinMessage, false);
        setJoinMessage(joinMessage, true);
    }

    public void setJoinMessage(String joinMessage, boolean isDonator) {
        if (!isDonator)
            this.joinMessage = "{username} has joined the fight!";
        else
            this.joinMessage = joinMessage;
    }

    public Component getLeaveMessage(String username) {
        String message = this.leaveMessage.replace("{username}", username);
        return Messenger.mm.deserialize(this.leaveColour + message);
    }

    public void setLeaveColour(String leaveColour, PlayerData data) {
        this.leaveColour = leaveColour.isEmpty() || leaveColour.equals("reset") ?
                new Cosmetic(JOIN_COLOUR, "Default", "<yellow>") :
                data.getCosmetic(leaveColour, LEAVE_COLOUR);
    }

    public void setLeaveMessage(String leaveMessage, PlayerData playerData) {
        if (playerData.getRank() == null)
            setLeaveMessage(leaveMessage, false);
        setLeaveMessage(leaveMessage, true);
    }

    public void setLeaveMessage(String leaveMessage, boolean isDonator) {
        if (!isDonator)
            this.leaveMessage = "{username} abandoned their post!";
        else
            this.leaveMessage = leaveMessage;
    }
}
