package me.greenfoot5.conwymc.data_types;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A data holder for a Setting
 */
public class Setting {
    public final TextComponent displayName;
    public final String key;
    /* Index 0 is default */
    public String[] values = new String[]{"false", "true"};
    public final List<Component> itemLore;
    public final Material material;

    /**
     * @param displayName The display name for the setting
     * @param key The key to use in the database
     * @param lore The item lore to use in GUIs
     * @param material The material to display in GUIs
     */
    public Setting(@NotNull String key, Material material, TextComponent displayName, List<Component> lore) {
        this.displayName = displayName.colorIfAbsent(NamedTextColor.GOLD);
        this.key = key;
        this.itemLore = setLoreBlue(lore);
        this.material = material;
    }

    /**
     * @param displayName The display name for the setting
     * @param key The key to use in the database
     * @param lore The item lore to use in GUIs
     * @param values The values the setting can have. Index 0 is default
     * @param material The material to display in GUIs
     */
    public Setting(@NotNull String key, Material material, TextComponent displayName, List<Component> lore, String[] values) {
        this.displayName = displayName.colorIfAbsent(NamedTextColor.GOLD);
        this.key = key;
        this.itemLore = setLoreBlue(lore);
        this.material = material;
        this.values = values;
    }

    /**
     * Creates a clone of a Setting
     * @param setting The setting to clone
     */
    public Setting(@NotNull Setting setting) {
        this.displayName = setting.displayName;
        this.key = setting.key;
        this.values = setting.values;
        this.itemLore = setting.itemLore;
        this.material = setting.material;
    }

    public static Setting[] generateSettings() {
        return new Setting[] {
                new Setting("joinPing", Material.BELL,
                        Component.text("Join Notification"),
                        Collections.singletonList(Component.text("Get a ping sound when another player joins the server"))),
        };
    }

    private List<Component> setLoreBlue(List<Component> lore) {
        if (lore == null) {
            return null;
        }

        ArrayList<Component> list = new ArrayList<>();
        for (Component c : lore) {
            if (c != null) {
                c = c.colorIfAbsent(NamedTextColor.BLUE);
                list.add(c);
            }
        }
        return list;
    }

    /**
     * Gets the default value for setting
     * @param setting The key of the setting
     * @return The default value for that setting or null if one isn't found
     */
    public static String getDefault(String setting) {
        Setting[] settings = generateSettings();
        for (Setting s : settings) {
            if (Objects.equals(s.key, setting)) {
                return s.values[0];
            }
        }
        return null;
    }
}
