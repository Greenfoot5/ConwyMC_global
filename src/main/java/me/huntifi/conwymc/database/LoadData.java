package me.huntifi.conwymc.database;

import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.data_types.Cosmetic;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.data_types.Tuple;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This class provides methods to retrieve data from the database.
 * These methods should only be called asynchronously.
 */
public class LoadData {

    /**
     * Load all data for a specific player.
     * @param uuid The unique ID of the player
     * @return The player data
     */
    public static PlayerData load(UUID uuid) {
        try {
            // Rank data
            // Also creates CS stats as coins are stored there
            createRankEntry(uuid);
            Tuple<PreparedStatement, ResultSet> prRank = getRankData(uuid);

            // Mute data
            Tuple<PreparedStatement, ResultSet> prMute = Punishments.getActive(uuid, "mute");

            // Cosmetics
            List<Cosmetic> cosmetics = getCosmetics(uuid);

            // Settings
            HashMap<String, String> settings = getSettings(uuid);

            // Collect data and release resources
            PlayerData data = new PlayerData(prRank.getSecond(), prMute.getSecond(), cosmetics, settings);
            prRank.getFirst().close();
            prMute.getFirst().close();

            return data;
        } catch (SQLException e) {
            ConwyMC.instance.getLogger().warning("Something went wrong while loading player data!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create an entry in the player rank table if none exists yet.
     * @param uuid The player for whom to create an entry
     * @throws SQLException If a database access error occurs
     */
    private static void createRankEntry(UUID uuid) throws SQLException {
        PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                "INSERT IGNORE INTO player_rank (uuid) VALUES (?)");
        ps.setString(1, uuid.toString());
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Get a player's rank data from the database.
     * @param uuid The unique id of the player whose data to get
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    private static Tuple<PreparedStatement, ResultSet> getRankData(UUID uuid) throws SQLException {
        // Get player stats from the database
        PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement("SELECT * FROM player_rank WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();

        // Return result set with pointer on first (and only) row
        rs.next();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get the rank points of a player from our database.
     * @param name The name of the player
     * @return The player's rank points, or -1 if the name is not in the database
     * @throws SQLException If something goes wrong executing the query
     */
    public static double getRankPoints(String name) throws SQLException {
        PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                "SELECT rank_points FROM player_rank WHERE username = ?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        double rankPoints;
        if (rs.next()) {
            rankPoints = rs.getDouble(1);
        } else {
            rankPoints = -1;
        }

        ps.close();
        return rankPoints;
    }

    /**
     * Get the top 10 donators.
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getTopDonators() throws SQLException {
        PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                "SELECT * FROM vw_top_donator LIMIT 10");

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get 10 donators from the database.
     * @param offset The amount of donators to skip
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getDonators(int offset) throws SQLException {
        PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                "SELECT * FROM vw_top_donator LIMIT 10 OFFSET ?");
        ps.setInt(1, offset);

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get 10 donators from the database.
     * @param offset The amount of donators to skip
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getAllTimeBoosters(int offset) throws SQLException {
        PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                "SELECT * FROM vw_top_boosters ORDER BY all_time DESC LIMIT 10 OFFSET ?");
        ps.setInt(1, offset);

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get 10 donators from the database.
     * @param offset The amount of donators to skip
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getMonthlyBoosters(int offset) throws SQLException {
        PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                "SELECT * FROM vw_top_boosters WHERE monthly > 0 ORDER BY monthly DESC LIMIT 10 OFFSET ?");
        ps.setInt(1, offset);

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get the UUID of a player from the database.
     * @param name The name of the player
     * @return The player's UUID, or null if the name is not in the database
     * @throws SQLException If something goes wrong executing the query
     */
    public static UUID getUUID(String name) throws SQLException {
        try (PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                "SELECT UUID FROM player_rank WHERE username = ?"
        )) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return UUID.fromString(rs.getString(1));
            else
                return null;
        }
    }

    /**
     * @param uuid The player to get the settings for
     * @return All the settings the user has changed the valur for at some point
     */
    private static HashMap<String, String> getSettings(UUID uuid) {
        HashMap<String, String> loadedSettings = new HashMap<>();

        try (PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                "SELECT setting, value FROM player_settings WHERE UUID = ?")) {
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                loadedSettings.put(rs.getString("setting"), rs.getString("value"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return loadedSettings;
    }

    /**
     * @param uuid The player to get the settings for
     * @return All the settings the user has changed the valur for at some point
     */
    private static List<Cosmetic> getCosmetics(UUID uuid) {
        ArrayList<Cosmetic> ownedCosmetics = new ArrayList<>();

        try (PreparedStatement ps = ConwyMC.SQL.getConnection().prepareStatement(
                "SELECT TYPE, VALUE, name FROM player_cosmetics WHERE UUID = ?")) {
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cosmetic.CosmeticType type;
                switch (rs.getString("type").toLowerCase()) {
                    case "chat colour":
                    case "chat color":
                    case "chatcolor":
                    case "chatcolour":
                        type = Cosmetic.CosmeticType.CHAT_COLOUR;
                        break;
                    case "join colour":
                    case "join color":
                    case "joincolor":
                    case "joincolour":
                        type = Cosmetic.CosmeticType.JOIN_COLOUR;
                        break;
                    case "leave colour":
                    case "leave color":
                    case "leavecolor":
                    case "leavecolour":
                        type = Cosmetic.CosmeticType.LEAVE_COLOUR;
                        break;
                    // We'll assume title if nothing else as it's easiest to debug
                    default:
                        type = Cosmetic.CosmeticType.TITLE;
                }

                ownedCosmetics.add(new Cosmetic(type, rs.getString("name"), rs.getString("VALUE")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ownedCosmetics;
    }
}
