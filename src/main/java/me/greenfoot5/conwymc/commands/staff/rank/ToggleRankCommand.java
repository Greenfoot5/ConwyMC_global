package me.greenfoot5.conwymc.commands.staff.rank;

import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.database.ActiveData;
import me.greenfoot5.conwymc.events.nametag.UpdateNameTagEvent;
import me.greenfoot5.conwymc.gui.Gui;
import me.greenfoot5.conwymc.util.Messenger;
import me.greenfoot5.conwymc.util.NameTag;
import me.greenfoot5.conwymc.util.RankPoints.RankDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Configures a player's shown rank
 */
public class ToggleRankCommand implements TabExecutor {

    /**
     * Switch between displaying staff and donator rank.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console cannot toggle their rank!", sender);
            return true;
        }

        Player player = (Player) sender;
        PlayerData data = ActiveData.getData(player.getUniqueId());
        if (args.length == 0) {
            int location = 0;
            Gui options = new Gui(Component.text("Ranks"), 1);

            options.addItem(Component.text("None", NamedTextColor.GRAY), Material.GRAY_CONCRETE,
                    List.of(Component.text("Removes any display rank")), location, "togglerank none", true);
            location++;

            if (data.getStaffRank() != null && !data.getStaffRank().isEmpty()) {
                options.addItem(NameTag.convertRank(data.getStaffRank()), Material.NETHERITE_SWORD,
                        List.of(Component.text("Displays your staff rank")), location, "togglerank staff", true);
                location++;
            }

            if (data.getRank() != null && !data.getRank().isEmpty()) {
                options.addItem(NameTag.convertRank(data.getRank()), Material.SUNFLOWER,
                        List.of(Component.text("Displays your donator rank")), location, "togglerank donator", true);
                location++;
            }

            if (data.getTopRank() != null && !data.getTopRank().isEmpty()) {
                options.addItem(NameTag.convertRank(data.getTopRank()), Material.DIAMOND,
                        List.of(Component.text("Displays your top donator rank")), location, "togglerank top", true);
            }
            options.open(player);
            return true;
        }

        RankDisplay rank;
        switch (args[0]) {
            case "staff":
                rank = RankDisplay.STAFF;
                break;
            case "rank":
            case "donator":
                rank = RankDisplay.DONATOR;
                break;
            case "top":
            case "toprank":
            case "topdonator":
                rank = RankDisplay.TOP_DONATOR;
                break;
            default:
                rank = RankDisplay.NONE;
        }

        toggleRank(rank, data, (Player) sender);
        return true;
    }

    public static void toggleRank(RankDisplay rank, PlayerData data, Player sender) {
        switch (rank) {
            case NONE:
                data.setDisplayRank(RankDisplay.NONE);
                data.setSetting(sender.getUniqueId(), "displayRank", "none");
                break;
            case STAFF:
                if (data.setDisplayRank(RankDisplay.STAFF)) {
                    Messenger.sendError("You do not have a staff rank!", sender);
                    return;
                }
                data.setSetting(sender.getUniqueId(), "displayRank", "staff");
                break;
            case DONATOR:
                if (data.setDisplayRank(RankDisplay.DONATOR)) {
                    Messenger.sendError("You do not have a donator rank!", sender);
                    return;
                }
                data.setSetting(sender.getUniqueId(), "displayRank", "donator");
                break;
            case TOP_DONATOR:
                if (data.setDisplayRank(RankDisplay.TOP_DONATOR)) {
                    Messenger.sendError("You do not have a top donator rank!", sender);
                    return;
                }
                data.setSetting(sender.getUniqueId(), "displayRank", "top");
                break;
            default:
                Messenger.sendError("Invalid display type. Possible values: <red>[none, staff, donator, top]", sender);
                return;
        }

        Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(sender, data));
    }

    /**
     * Displays tab complete options for displaying rank
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return The list of tab complete options
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return List.of("none", "staff", "donator", "top");
    }
}
