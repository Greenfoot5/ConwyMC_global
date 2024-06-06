package me.huntifi.conwymc.data_types;

import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;

import java.util.HashMap;

public class PlayerCosmetics {

    /** The player's title */
    private String title;

    /** The player's chat colour */
    private String chatColour;

    /** The player's custom join message */
    private String joinMessage;

    /** The mm prefix to the join message */
    private String joinColour;

    /** The player's custom leave message */
    private String leaveMessage;

    /** The mm prefix to the leave message */
    private String leaveColour;

    public PlayerCosmetics( HashMap<String, String> settings, PlayerData data) {
        setJoinMessage(settings.get("join_message"), data);
        setJoinColour(settings.get("join_colour"));
        setLeaveMessage(settings.get("leave_message"), data);
        setLeaveColour(settings.get("leave_colour"));
        setTitle(settings.get("title"));
        setChatColour(settings.get("chat_colour"), data);
    }

    public String getRawTitle() {
        return title;
    }

    public Component getTitle() {
        if (title == null)
            return null;
        return Messenger.mm.deserialize(title);
    }

    public String getCleanTitle() {
        return Messenger.clean(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChatColour() {
        return chatColour;
    }

    public void setChatColour(String chatColour, PlayerData playerData) {
        if (playerData.getStaffRank() == null)
            setChatColour(chatColour, true);
        setChatColour(chatColour, false);
    }

    public void setChatColour(String chatColour, boolean isStaff) {
        if (chatColour != null) {
            this.chatColour = chatColour;
            return;
        }

        this.chatColour = isStaff ? "<white>" : "<grey>";
    }

    public Component getJoinMessage(String username) {
        String message = this.joinMessage.replace("{username}", username);
        return Messenger.mm.deserialize(this.joinColour + message);
    }

    public void setJoinColour(String joinColour) {
        this.joinColour = joinColour.isEmpty() ? "<yellow>" : joinColour;
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

    public void setLeaveColour(String leaveColour) {
        this.leaveColour = leaveColour.isEmpty() ? "<yellow>" : leaveColour;
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
