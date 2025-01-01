package me.greenfoot5.conwymc.commands;

import me.greenfoot5.conwymc.data_types.Cosmetic;
import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.database.ActiveData;
import me.greenfoot5.conwymc.database.StoreData;
import me.greenfoot5.conwymc.events.nametag.UpdateNameTagEvent;
import me.greenfoot5.conwymc.gui.Gui;
import me.greenfoot5.conwymc.gui.PaginatedGui;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Configures a player's shown rank
 */
public class CosmeticsCommand implements TabExecutor {

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
            Messenger.sendError("Console cannot equip cosmetics!", sender);
            return true;
        }

        Player player = (Player) sender;
        PlayerData data = ActiveData.getData(player.getUniqueId());
        if (args.length == 0) {
            Gui categories = new Gui(Component.text("Cosmetics"), 1);

            categories.addItem(Messenger.mm.deserialize("<b><gold>Titles"),
                    Material.NAME_TAG,
                    List.of(Component.empty(),
                            Messenger.mm.deserialize("<yellow><i>Titles are unique cosmetic player prefixes"),
                            Messenger.mm.deserialize("<yellow><i>displayed in chat")),
                    1, "cosmetics titles", true);

            categories.addItem(Messenger.mm.deserialize("<b><gold>Chat Colours"),
                    Material.BLUE_DYE,
                    List.of(Component.empty(),
                            Messenger.mm.deserialize("<yellow><i>Chat colours change the appearance of"),
                            Messenger.mm.deserialize("<yellow><i>your messages in chat")),
                    3, "cosmetics chat", true);

            categories.addItem(Messenger.mm.deserialize("<b><gold>Join Colours"),
                    Material.GREEN_DYE,
                    List.of(Component.empty(),
                            Messenger.mm.deserialize("<yellow><i>Join colours change the appearance of"),
                            Messenger.mm.deserialize("<yellow><i>your join message")),
                    5, "cosmetics join", true);

            categories.addItem(Messenger.mm.deserialize("<b><gold>Leave Colours"),
                    Material.YELLOW_DYE,
                    List.of(Component.empty(),
                            Messenger.mm.deserialize("<yellow><i>Leave colours change the appearance of"),
                            Messenger.mm.deserialize("<yellow><i>your leave message")),
                    7, "cosmetics leave", true);

            categories.open(player);
            return true;
        }

        switch (args[0]) {
            case "titles":
            case "title":
                generateGui(data, Cosmetic.CosmeticType.TITLE).open(player);
                break;
            case "chatcolour":
            case "chat":
            case "chatcolor":
            case "chat_colour":
            case "chat_color":
            case "chat-colour":
            case "chat-color":
                generateGui(data, Cosmetic.CosmeticType.CHAT_COLOUR).open(player);
                break;
            case "joincolour":
            case "joincolor":
            case "join":
            case "join_colour":
            case "join_color":
            case "join-colour":
            case "join-color":
                generateGui(data, Cosmetic.CosmeticType.JOIN_COLOUR).open(player);
                break;
            case "leavecolour":
            case "leavecolor":
            case "leave":
            case "leave_colour":
            case "leave_color":
            case "leave-colour":
            case "leave-color":
                generateGui(data, Cosmetic.CosmeticType.LEAVE_COLOUR).open(player);
                break;
            case "equip":
                if (args.length < 3) {
                    Messenger.sendError("Invalid use of equip command. Please use the GUI.", sender);
                    return true;
                }
                String cosmeticName = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                switch (args[1]) {
                    case "title":
                        data.getCosmetics().setTitle(cosmeticName, data);
                        Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(player, data));
                        StoreData.updateSetting(player.getUniqueId(), "title", cosmeticName);
                        break;
                    case "chatcolour":
                        data.getCosmetics().setChatColour(cosmeticName, data);
                        StoreData.updateSetting(player.getUniqueId(), "chat_colour", cosmeticName);
                        break;
                    case "joincolour":
                        data.getCosmetics().setJoinColour(cosmeticName, data);
                        StoreData.updateSetting(player.getUniqueId(), "join_colour", cosmeticName);
                        break;
                    case "leavecolour":
                        data.getCosmetics().setLeaveColour(cosmeticName, data);
                        StoreData.updateSetting(player.getUniqueId(), "leave_colour", cosmeticName);
                        break;
                    default:
                        Messenger.sendError(args[1] + " is an invalid equip category. Please use the GUI.", sender);
                }
                return true;
            default:
                Messenger.sendError("Invalid display type. Possible values: <red>[titles, chatcolour, joincolour, leavecolour]", sender);
                return true;
        }

        Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(player, data));
        return true;
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
        return List.of("titles", "chat_colour", "join_colour", "leave_colour");
    }

    private PaginatedGui generateGui(PlayerData data, Cosmetic.CosmeticType type) {
        Component title;
        Material material;
        String command = "cosmetics equip ";
        String previewSuffix;
        ArrayList<Component> lorePrefix = new ArrayList<>();
        String defaultText = "";
        switch (type) {
            case TITLE:
                title = Component.text("Titles");
                material = Material.NAME_TAG;
                command += "title ";
                previewSuffix = "";
                lorePrefix.add(Messenger.mm.deserialize("<grey><i>Titles are unique cosmetic player prefixes"));
                lorePrefix.add(Messenger.mm.deserialize("<grey><i>displayed in chat"));
                break;
            case CHAT_COLOUR:
                title = Component.text("Chat Colour");
                material = Material.BLUE_DYE;
                command += "chatcolour ";
                previewSuffix = "Am I a fool who sits alone talking to the moon?";
                defaultText = "<grey>";
                lorePrefix.add(Messenger.mm.deserialize("<grey><i>Chat colours change the appearance of"));
                lorePrefix.add(Messenger.mm.deserialize("<grey><i>your messages in chat"));
                break;
            case JOIN_COLOUR:
                title = Component.text("Join Colour");
                material = Material.GREEN_DYE;
                command += "joincolour ";
                previewSuffix = "When I walk in the club";
                defaultText = "<yellow>";
                lorePrefix.add(Messenger.mm.deserialize("<grey><i>Join colours change the appearance of"));
                lorePrefix.add(Messenger.mm.deserialize("<grey><i>your join message"));
                break;
            case LEAVE_COLOUR:
                title = Component.text("Leave Colour");
                material = Material.YELLOW_DYE;
                command += "leavecolour ";
                previewSuffix = "Humphrey, we're leaving";
                defaultText = "<yellow>";
                lorePrefix.add(Messenger.mm.deserialize("<grey><i>Leave colours change the appearance of"));
                lorePrefix.add(Messenger.mm.deserialize("<grey><i>your leave message"));
                break;
            default:
                title = Component.text("Cosmetics");
                material = Material.GRAY_DYE;
                command += "cosmetic ";
                previewSuffix = "I shouldn't be here \uD83D\uDE43";
                defaultText = "<red>";
        }
        lorePrefix.add(Component.empty());
        PaginatedGui gui = new PaginatedGui(title, true);

        List<Cosmetic> owned = data.getCosmetics(type);

        // Reset item
        ArrayList<Component> defaultLore = new ArrayList<>(lorePrefix);
        defaultLore.add(Messenger.mm.deserialize("<yellow>⁎ <b><gold>Preview:</yellow> " + defaultText + previewSuffix));
        defaultLore.add(Component.empty());
        defaultLore.add(Messenger.mm.deserialize("<blue>Click here to reset your cosmetic!"));
        gui.addItem(Messenger.mm.deserialize("<b><gold>Default"),
                Material.GRAY_DYE, defaultLore, command + "reset", true);

        for (Cosmetic cosmetic : owned) {
            ArrayList<Component> lore = new ArrayList<>(lorePrefix);
            lore.add(Messenger.mm.deserialize("<yellow>⁎ <b><gold>Preview:</yellow> " + cosmetic.getValue() + previewSuffix));
            lore.add(Component.empty());
            lore.add(Messenger.mm.deserialize("<blue>Click here to select this cosmetic!"));
            gui.addItem(Messenger.mm.deserialize("<b><gold>Title: </gold><yellow>" + cosmetic.getName()),
                    material, lore, command + cosmetic.getName(), true);
        }

        gui.setBackItemCommand("cosmetics");
        return gui;
    }
}
