package me.greenfoot5.conwymc.commands.info;

import me.greenfoot5.conwymc.ConwyMC;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.database.LoadData;
import me.greenfoot5.conwymc.util.Messenger;
import me.greenfoot5.conwymc.util.NameTag;
import me.greenfoot5.conwymc.util.RankPoints;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * Shows the player the donator leaderboard
 */
public class TopDonatorsCommand implements CommandExecutor {

    /**
     * Print the donator leaderboard to the player.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> {
            if (args.length == 0) {
                display(sender, 0);
            } else {
                try {
                    display(sender, Integer.parseInt(args[0]));
                } catch (NumberFormatException e) {
                    // TODO - player specified
                    Messenger.sendError("Searching by player is currently not supported!", sender);
                }
            }
        });

        return true;
    }

    /**
     * Print at most 10 donators to the player, sorted by rank points in descending order.
     * @param sender Source of the command
     * @param requested The requested position
     */
    private void display(CommandSender sender, int requested) {
        try {
            int offset = requested < 7 ? 0 : requested - 5;

            // Prepare data used for the message
            Tuple<PreparedStatement, ResultSet> donators = LoadData.getDonators(offset);
            DecimalFormat num = new DecimalFormat("0");
            int pos = offset;

            // Create the message
            Component message = Component.text("#. Player Rank Points", NamedTextColor.AQUA);
            while (donators.getSecond().next()) {
                pos++;
                NamedTextColor color = pos == requested ? NamedTextColor.AQUA : NamedTextColor.DARK_AQUA;
                String name = donators.getSecond().getString("username");
                int points = donators.getSecond().getInt("rank_points");

                message = message.append(Component.newline())
                        .append(Component.text(num.format(pos) + ". ")
                                .append(Component.text(name + " ", color))
                                .append(getRank(pos, points))
                                .color(NamedTextColor.GRAY))
                        .append(Component.text(num.format(points), NamedTextColor.WHITE));
            }
            // Send the message
            Messenger.send(message, sender);
            donators.getFirst().close();

        } catch (SQLException e) {
            e.printStackTrace();
            Messenger.sendError("Something went wrong!" +
                    " Please contact an administrator if this issue persists.", sender);
        }
    }

    /**
     * Get the donator rank that corresponds to the position.
     * @param position The position on the donator leaderboard
     * @return The corresponding pretty donator rank
     */
    private Component getRank(int position, int points) {
        String rank;
        if (position <= 10) {
            rank = RankPoints.getTopRank(position);
        } else {
            rank = RankPoints.getRank(points);
        }
        return NameTag.convertRank(rank);
    }
}
