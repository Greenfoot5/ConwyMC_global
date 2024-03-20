package me.huntifi.conwymc.util;

import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.gui.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Serves as a tool to easily create items for kits
 */
public class ItemCreator {

    /**
     * Create a specified item
     * @param item The item to apply all flags and parameters to
     * @param name The name for the item
     * @param lore The lore for the item
     * @param enchants The enchantments for the item
     * @return The item with all flags and parameters applied
     */
    public static ItemStack item(ItemStack item, Component name, List<Component> lore,
                                 List<Tuple<Enchantment, Integer>> enchants) {
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setUnbreakable(true);
        itemMeta.displayName(name.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        itemMeta.lore(Gui.removeItalics(lore));
        if (enchants != null) {
            for (Tuple<Enchantment, Integer> enchant : enchants) {
                itemMeta.addEnchant(enchant.getFirst(), enchant.getSecond(), true);
            }
        }
        item.setItemMeta(itemMeta);
        return item;
    }
}
