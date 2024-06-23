package me.huntifi.conwymc.commands.info;

import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.database.LoadData;
import me.huntifi.conwymc.util.Messenger;
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
public class TopBoostersCommand implements CommandExecutor {

    /**
     * Print the booster leaderboard to the player.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        boolean monthly;
        switch (cmd.getName()) {
            case "TopBoosters":
                monthly = false;
                break;
            case "MonthlyBoosters":
                monthly = true;
                break;
            default:
                Messenger.sendError("Something went wrong! Please contact an administrator.", sender);
                return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(ConwyMC.plugin, () -> {
            if (args.length == 0) {
                display(sender, 0, monthly);
            } else {
                try {
                    display(sender, Integer.parseInt(args[0]), monthly);
                } catch (NumberFormatException e) {
                    Messenger.sendError("Position must be a number!", sender);
                }
            }
        });

        return true;
    }

    /**
     * Print at 10 most booster users to the player
     * @param sender Source of the command
     * @param requested The requested position
     */
    private void display(CommandSender sender, int requested, boolean isMonthly) {
        try {
            int offset = requested < 7 ? 0 : requested - 5;

            // Prepare data used for the message
            Tuple<PreparedStatement, ResultSet> boosters = isMonthly ? LoadData.getAllTimeBoosters(offset) : LoadData.getMonthlyBoosters(offset);
            DecimalFormat num = new DecimalFormat("0");
            int pos = offset;

            // Create the message
            Component message = Component.text("#. Player Monthly/All-Time Boosters", NamedTextColor.AQUA);
            while (boosters.getSecond().next()) {
                pos++;
                NamedTextColor color = pos == requested ? NamedTextColor.AQUA : NamedTextColor.DARK_AQUA;
                String name = boosters.getSecond().getString("username");
                int allTime = boosters.getSecond().getInt("all_time");
                int monthly = boosters.getSecond().getInt("monthly");

                message = message.append(Component.newline())
                        .append(Component.text(num.format(pos) + ". ")
                                .append(Component.text(name + " ", color))
                                .append(Messenger.mm.deserialize("<transition:#AD68B0:#4466D9:" + (pos - offset) / 10f + ">" + monthly))
                                .color(NamedTextColor.GRAY))
                        .append(Component.text("/", NamedTextColor.WHITE))
                        .append(Messenger.mm.deserialize("<transition:#FC9765:#FB5D61:" + (pos - offset) / 10f + ">" + allTime));
            }
            // Send the message
            Messenger.send(message, sender);
            boosters.getFirst().close();

        } catch (SQLException e) {
            e.printStackTrace();
            Messenger.sendError("Something went wrong!" +
                    " Please contact an administrator if this issue persists.", sender);
        }
    }
}
