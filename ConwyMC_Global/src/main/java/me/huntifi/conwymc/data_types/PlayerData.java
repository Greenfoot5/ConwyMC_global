package me.huntifi.conwymc.data_types;

import me.huntifi.conwymc.util.NameTag;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Represents a player's data
 */
public class PlayerData {

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
    private String chatMode = "global";

    /** Whether the player's staff rank is hidden */
    private boolean isHiddenStaff = false;

    /** The player's mute reason and expiration time */
    private Tuple<String, Timestamp> mute;

    /**
     * Initialize the player's data.
     * @param rankData The data retrieved from player_rank
     * @param mute The player's active mute
     * @throws SQLException If a database access error occurs or an invalid column label is used
     */
    public PlayerData(ResultSet rankData, ResultSet mute) throws SQLException {
        this.staffRank = rankData.getString("staff_rank").toLowerCase();
        this.rankPoints = rankData.getDouble("rank_points");
        this.joinMessage = rankData.getString("join_message");
        this.leaveMessage = rankData.getString("leave_message");

        this.mute = mute.next() ? new Tuple<>(mute.getString("reason"), mute.getTimestamp("end")) : null;
    }

    /**
     * Get the player's display rank.
     * @return The pretty representation of the player's staff or donator rank
     */
    public String getDisplayRank() {
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

    /**
     * Get the player's chat color.
     * @return The player's chat color
     */
    public ChatColor getChatColor() {
        if (staffRank.isEmpty() || isHiddenStaff)
            return ChatColor.GRAY;
        if (staffRank.equals("owner"))
            return ChatColor.GREEN;
        return ChatColor.WHITE;
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
}
