package me.greenfoot5.conwymc.data_types;

import me.greenfoot5.conwymc.ConwyMC;
import me.greenfoot5.conwymc.database.LoadData;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static me.greenfoot5.conwymc.data_types.Cosmetic.CosmeticType.CHAT_COLOUR;
import static me.greenfoot5.conwymc.data_types.Cosmetic.CosmeticType.JOIN_COLOUR;
import static me.greenfoot5.conwymc.data_types.Cosmetic.CosmeticType.LEAVE_COLOUR;
import static me.greenfoot5.conwymc.data_types.Cosmetic.CosmeticType.TITLE;

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
        updateCosmetics(settings, data);
    }

    public void updateCosmetics(HashMap<String, String> settings, PlayerData data) {
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
        if (chatColour != null && !chatColour.equals("reset")) {
            Cosmetic newColour = data.getCosmetic(chatColour, CHAT_COLOUR);
            if (newColour != null) {
                this.chatColour = newColour;
                return;
            }
        }

        if (data.getStaffRank() == null)
            this.chatColour = new Cosmetic(CHAT_COLOUR, "Default", "<grey>");
        else
            this.chatColour = new Cosmetic(CHAT_COLOUR, "Staff", "<white>");
    }

    public Component getJoinMessage(String username) {
        String message = this.joinMessage.replace("{username}", username);
        return Messenger.mm.deserialize(this.joinColour + message);
    }

    public static Component getStaticJoinMessage(String username) {
        String message = "{username} has joined the fight!".replace("{username}", username);
        return Messenger.mm.deserialize("<yellow>" + message);
    }


    public void setJoinColour(String joinColour, PlayerData data) {
        if (joinColour == null || joinColour.isEmpty() || joinColour.equals("reset")) {
            this.joinColour = new Cosmetic(JOIN_COLOUR, "Default", "<yellow>");
            return;
        }

        Cosmetic newColour = data.getCosmetic(joinColour, JOIN_COLOUR);
        this.joinColour = Objects.requireNonNullElseGet(newColour, () -> new Cosmetic(JOIN_COLOUR, "Default", "<yellow>"));
    }

    public void setJoinMessage(String joinMessage, PlayerData playerData) {
        if (playerData.getRank() == null)
            setJoinMessage(joinMessage, false);
        setJoinMessage(joinMessage, true);
    }

    public void setJoinMessage(String joinMessage, boolean isDonator) {
        if (!isDonator || joinMessage == null || joinMessage.isEmpty())
            this.joinMessage = "{username} has joined the fight!";
        else
            this.joinMessage = "<hover:show_text:'<yellow>{username} has joined the fight!</yellow>'>" + joinMessage;
    }

    public Component getLeaveMessage(String username) {
        String message = this.leaveMessage.replace("{username}", username);
        return Messenger.mm.deserialize(this.leaveColour + message);
    }

    public void setLeaveColour(String leaveColour, PlayerData data) {
        if (leaveColour == null || leaveColour.isEmpty() || leaveColour.equals("reset")) {
            this.leaveColour = new Cosmetic(LEAVE_COLOUR, "Default", "<yellow>");
            return;
        }

        Cosmetic newColour = data.getCosmetic(leaveColour, JOIN_COLOUR);
        this.leaveColour = Objects.requireNonNullElseGet(newColour, () -> new Cosmetic(LEAVE_COLOUR, "Default", "<yellow>"));
    }

    public void setLeaveMessage(String leaveMessage, PlayerData playerData) {
        if (playerData.getRank() == null)
            setLeaveMessage(leaveMessage, false);
        setLeaveMessage(leaveMessage, true);
    }

    public void setLeaveMessage(String leaveMessage, boolean isDonator) {
        if (!isDonator || leaveMessage == null || leaveMessage.isEmpty())
            this.leaveMessage = "{username} abandoned their post!";
        else
            this.leaveMessage = "<hover:show_text:'<yellow>{username} abandoned their post!</yellow>'>" + leaveMessage;
    }

    public static void checkTopCosmetics(PlayerData data, UUID uuid) {
        try {
            Cosmetic benevolent = new Cosmetic(TITLE, "Benevolent", "<gradient:#DA4453:#89216B>❤<b>Benevolent</b>❤");
            Tuple<PreparedStatement, ResultSet> allTimeBoosters = LoadData.getAllTimeBoosters(0);
            isTop(data, uuid, allTimeBoosters.getSecond(), 3, benevolent);
            allTimeBoosters.getFirst().close();
        } catch (SQLException e) {
            ConwyMC.plugin.getLogger().severe("Error in checkTopCosmetics for " + uuid);
            ConwyMC.plugin.getLogger().severe(e.getMessage());
        }
    }

    public static void checkMonthlyCosmetics(PlayerData data, UUID uuid) {
        try {
            Cosmetic magnificent = new Cosmetic(TITLE, "Magnificent", "<gradient:#C6FFDD:#FBD786:#f7797d>☀<b>Magnificent</b>♥");
            Tuple<PreparedStatement, ResultSet> monthlyBoosters = LoadData.getMonthlyBoosters(0);
            isTop(data, uuid, monthlyBoosters.getSecond(), 5, magnificent);
            monthlyBoosters.getFirst().close();
        } catch (SQLException e) {
            ConwyMC.plugin.getLogger().severe("Error in checkTopCosmetics for " + uuid);
            ConwyMC.plugin.getLogger().severe(e.getMessage());
        }
    }

    public static void isTop(PlayerData data, UUID uuid, ResultSet results, int maximumPosition, Cosmetic cosmetic) {
        try {
            for (int i = 0; i < maximumPosition; i++) {
                results.next();
                if (results.getString("UUID").equals(uuid.toString())) {
                    if (data.getCosmetic(cosmetic.getName(), TITLE) == null) {
                        data.addCosmetic(cosmetic);
                    }
                }
            }
        } catch (SQLException e) {
            ConwyMC.plugin.getLogger().severe("Error in isTop for " + uuid);
            ConwyMC.plugin.getLogger().severe(e.getMessage());
        }
    }

    public static void checkRankCosmetics(PlayerData data, UUID uuid) {
        switch (data.getTopRank())
        {
            case "high_king":
                Cosmetic highKingChat = new Cosmetic(CHAT_COLOUR, "High King", "<gradient:#FFED00:#FF0000>");
                data.addCosmetic(highKingChat);
            case "king":
                Cosmetic kingChat = new Cosmetic(CHAT_COLOUR, "King", "<gradient:#F07654:#F5DF2E:#F07654>");
                data.addCosmetic(kingChat);
            case "viceroy":
                Cosmetic viceroyChat = new Cosmetic(CHAT_COLOUR, "Viceroy", "<gradient:#A91BB6:#D46ED4>");
                data.addCosmetic(viceroyChat);
        }
    }
}
