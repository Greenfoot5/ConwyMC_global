package me.huntifi.conwymc.util;

import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.database.LoadData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Manages a player's rank points
 */
public class RankPoints {

    /**
     * Get the player's donator top rank.
     * @param uuid The unique ID of the player
     * @return The player's donator top rank, empty string if not applicable
     */
    public static String getTopRank(UUID uuid) {
        try {
            Tuple<PreparedStatement, ResultSet> top = LoadData.getTopDonators();
            int i = 1;

            while (top.getSecond().next() && i <= 10) {
                if (top.getSecond().getString("uuid").equalsIgnoreCase(uuid.toString())) {
                    top.getFirst().close();
                    return getTopRank(i);
                }
                i++;
            }

            top.getFirst().close();
            return "";

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Get the donator rank that corresponds to the position.
     * @param position The position on the donator leaderboard
     * @return The corresponding non-pretty donator rank
     */
    public static String getTopRank(int position) {
        if (position == 1) {
            return "high_king";
        } else if (position <= 3) {
            return "king";
        } else if (position <= 10) {
            return "viceroy";
        } else {
            return "";
        }
    }

    /**
     * Get the donator rank that corresponds to the rank points.
     * @param rp The amount of rank points
     * @return The corresponding non-pretty donator rank
     */
    public static String getRank(double rp) {
        if (rp > 80) {
            return "duke";
        } else if (rp > 60) {
            return "count";
        } else if (rp > 40) {
            return "baron";
        } else if (rp > 20) {
            return "noble";
        } else if (rp >= 1) {
            return "esquire";
        } else {
            return "";
        }
    }
}
