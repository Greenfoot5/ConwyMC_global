package me.huntifi.conwymc.data_types;

import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.commands.chat.GlobalChatCommand;
import me.huntifi.conwymc.database.StoreData;
import me.huntifi.conwymc.util.NameTag;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a player's data
 */
public class PlayerData {

    /** The player's coins */
    private double coins;

    /** The player's staff rank */
    private String staffRank;

    /** The player's rank */
    private String rank;

    /** The player's rank points */
    private double rankPoints;

    /** The player's custom join message */
    private String joinMessage;

    /** The player's custom leave message */
    private String leaveMessage;

    /** The player's current chat mode */
    private String chatMode = GlobalChatCommand.CHAT_MODE;

    /** Whether the player's staff rank is hidden */
    private boolean isHiddenStaff = false;

    /** The player's settings */
    private final HashMap<String, String> settings;

    /** The player's mute reason and expiration time */
    private Tuple<String, Timestamp> mute;

    /** The current coin multiplier */
    protected static double coinMultiplier = 1;


    /**
     * Initialize the player's data.
     * @param rankData The data retrieved from player_rank
     * @param mute The player's active mute
     * @throws SQLException If a database access error occurs or an invalid column label is used
     */
    public PlayerData(ResultSet rankData, ResultSet mute, HashMap<String, String> settings) throws SQLException {
        this.coins = rankData.getDouble("coins");
        this.staffRank = rankData.getString("staff_rank").toLowerCase();
        this.rankPoints = rankData.getDouble("rank_points");
        this.joinMessage = rankData.getString("join_message");
        this.leaveMessage = rankData.getString("leave_message");
        this.settings = settings;

        this.mute = mute.next() ? new Tuple<>(mute.getString("reason"), mute.getTimestamp("end")) : null;
    }

    /**
     * Make a clone of a PlayerData
     */
    public PlayerData(PlayerData data) {
        this.coins = data.getCoins();
        this.staffRank = data.getStaffRank();
        this.rankPoints = data.getRankPoints();
        this.joinMessage = data.getJoinMessage();
        this.leaveMessage = data.getLeaveMessage();
        this.settings = data.settings;

        this.rank = data.getRank();
        this.chatMode = data.getChatMode();
        this.isHiddenStaff = data.isHiddenStaff;

        this.mute = this.getMute();
    }

    /**
     * Get the player's coins.
     * @return The player's coins
     */
    public double getCoins() {
        return coins;
    }

    /**
     * Set the player's coins.
     * @param coins The coins to set
     */
    public void setCoins(double coins) {
        this.coins = coins;
    }

    /**
     * Add to the player's coins.
     * @param coins The amount of coins to add
     */
    public void addCoins(double coins) {
        this.coins += coins * coinMultiplier;
    }

    /**
     * Add to the player's coins without taking the multiplier into account.
     * @param coins The amount of coins to add
     */
    public void addCoinsClean(double coins) {
        this.coins += coins;
    }

    /**
     * Take from the player's coins if they have enough.
     * @param amount The amount of coins to take
     * @return Whether the player had enough coins to take
     */
    public boolean takeCoins(double amount) {
        if (this.coins < amount) {
            return false;
        }
        this.coins -= amount;
        return true;
    }

    /**
     * Take from the player's coins regardless of their current balance.
     * @param amount The amount of coins to take
     */
    public void takeCoinsForce(double amount) {
        this.coins -= amount;
    }

    /**
     * Get the player's display rank.
     * @return The pretty representation of the player's staff or donator rank
     */
    public Component getDisplayRank() {
        if (!staffRank.isEmpty() && !isHiddenStaff)
            return NameTag.convertRank(staffRank);

        return NameTag.convertRank(rank);
    }

    /**
     * Get the player's staff rank.
     * @return The player's staff rank
     */
    public String getStaffRank() {
        return staffRank;
    }

    /**
     * Set the player's staff rank.
     * @param staffRank The rank to set
     */
    public void setStaffRank(String staffRank) {
        this.staffRank = staffRank;
    }

    /**
     * Get the player's rank.
     * @return The player's rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * Set the player's rank.
     * @param rank The rank to set
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * Get the player's rank points.
     * @return The player's rank points
     */
    public double getRankPoints() {
        return rankPoints;
    }

    /**
     * Set the player's rank points.
     * @param rankPoints The rank points to set
     */
    public void setRankPoints(double rankPoints) {
        this.rankPoints = rankPoints;
    }

    /**
     * Get the player's custom join message.
     * @return The player's custom join message
     */
    public String getJoinMessage() {
        return joinMessage;
    }

    /**
     * Set the player's custom join message.
     * @param joinMessage The custom join message
     */
    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    /**
     * Get the player's custom leave message.
     * @return The player's custom leave message
     */
    public String getLeaveMessage() {
        return leaveMessage;
    }

    /**
     * Set the player's custom leave message.
     * @param leaveMessage The custom leave message
     */
    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    /**
     * Get the player's current chat mode.
     * @return The player's chat mode
     */
    public String getChatMode() {
        return chatMode;
    }

    /**
     * Set the player's current chat mode
     * @param chatMode The chat mode to set
     */
    public void setChatMode(String chatMode) {
        this.chatMode = chatMode;
    }

    /**
     * Toggle whether the player's staff rank or donator rank is shown.
     * @return Whether the player's staff rank is shown
     */
    public boolean toggleRank() {
        isHiddenStaff = !isHiddenStaff;
        return !isHiddenStaff;
    }

    public String getMMChatColor(String name) {
        if (staffRank.isEmpty() || isHiddenStaff || name == null)
            return "<gray>";

        switch (name) {
            case "Larrydabbles":
                return "<gradient:#58D9FF:#AFDFFF>";
            case "Huntifi":
            case "Greenfoot5":
                return "<gradient:#1FD1F9:#B621FE>";
            default:
                return "<white>";
        }
    }

    /**
     * Get the player's current mute.
     * @return The player's mute reason and expiration timestamp, null if not muted
     */
    public Tuple<String, Timestamp> getMute() {
        return mute;
    }

    /**
     * Set the player's mute.
     * @param reason The reason for the mute
     * @param end The end of the mute
     */
    public void setMute(String reason, Timestamp end) {
        if (reason == null || end == null)
            mute = null;
        else
            mute = new Tuple<>(reason, end);
    }

    /**
     * Get the active coin multiplier.
     * @return The active coin multiplier
     */
    public static double getCoinMultiplier() {
        return coinMultiplier;
    }

    /**
     * Set the coin multiplier.
     * @param multiplier The multiplier to set
     */
    public static void setCoinMultiplier(double multiplier) {
        coinMultiplier = multiplier;
    }

    /**
     * Get the player's value of a setting
     * @param name The name or key of a setting
     * @return The player's value of a setting or default
     */
    public String getSetting(String name) {
        return settings.get(name) == null ? Setting.getDefault(name) : settings.get(name);
    }

    /**
     * Set the player's value of a setting
     * @param uuid The player's unique ID
     * @param setting The setting
     * @param value The value
     */
    public void setSetting(UUID uuid, String setting, String value) {
        boolean isNewSetting = settings.get(setting) == null;
        settings.put(setting, value);

        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> {
            if (isNewSetting) {
                StoreData.addSetting(uuid, setting, value);
            } else {
                StoreData.updateSetting(uuid, setting, value);
            }
        });
    }
}
