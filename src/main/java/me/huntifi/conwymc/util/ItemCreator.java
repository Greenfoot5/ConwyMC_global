package me.huntifi.conwymc.util;

import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.gui.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;
import java.util.UUID;

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

    public static ItemStack weapon(ItemStack item, Component name, List<Component> lore,
                                   List<Tuple<Enchantment, Integer>> enchants, double damage) {
        return setDamage(item(item, name, lore, enchants), damage);

    }

    /**
     * Create a specified piece of leather armor
     * @param item The item to apply all flags and parameters to
     * @param name The name for the item
     * @param lore The lore for the item
     * @param enchants The enchantments for the item
     * @param color The color for the item
     * @return The item with all flags and parameters applied
     */
    public static ItemStack leatherArmor(ItemStack item, Component name, List<Component> lore,
                                         List<Tuple<Enchantment, Integer>> enchants, Color color) {
        ItemStack leatherItem = item(item, name, lore, enchants);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) leatherItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setColor(color);
        leatherItem.setItemMeta(itemMeta);
        return leatherItem;
    }

    /**
     * Set the damage for an item.
     * @param item The item
     * @param damage The damage
     * @return The item with the damage applied
     */
    private static ItemStack setDamage(ItemStack item, double damage) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(), "SetHandDamage", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(), "SetOffHandDamage", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND));
        item.setItemMeta(meta);
        return item;
    }
}
