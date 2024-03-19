package me.huntifi.conwymc.gui;

import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.util.ItemCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static net.kyori.adventure.text.format.TextDecoration.State.FALSE;

/**
 * A GUI made with a minecraft inventory
 */
public class Gui implements Listener {

    protected final Inventory inventory;
    protected final HashMap<Integer, GuiItem> locationToItem = new HashMap<>();

    /** Whether this GUI should stop listening for events after being closed */
    private final boolean shouldUnregister;

    /**
     * Create an inventory.
     * @param name The name of the inventory
     * @param rows The amount of rows of the inventory
     */
    public Gui(Component name, int rows) {
        this(name, rows, false);
    }

    /**
     * Create an inventory.
     * @param name The name of the inventory
     * @param rows The amount of rows of the inventory
     * @param shouldUnregister Whether this GUI should stop listening for events after being closed
     */
    public Gui(Component name, int rows, boolean shouldUnregister) {
        inventory = Bukkit.getServer().createInventory(null, 9 * rows, name);
        this.shouldUnregister = shouldUnregister;

        Bukkit.getServer().getPluginManager().registerEvents(this, ConwyMC.plugin);
    }

    /**
     * Add an item to the inventory.
     * @param name The name of the item
     * @param material The material of the item
     * @param lore The lore of the item
     * @param location The location of the item
     * @param command The command to execute when clicking the item
     * @param shouldClose Whether the GUI should close after performing the command
     */
    public void addItem(Component name, Material material, List<Component> lore, int location, String command, boolean shouldClose) {
        List<Component> loreI = removeItalics(lore);
        inventory.setItem(location, ItemCreator.item(new ItemStack(material),
                name.decorationIfAbsent(TextDecoration.ITALIC, FALSE), loreI, null));
        locationToItem.put(location, new GuiItem(command, shouldClose));
    }

    /**
     * @param location The location of the item
     * @param command The command to execute when clicking the item
     */
    public void addBackItem(int location, String command) {
        ItemStack item = ItemCreator.item(new ItemStack(Material.TIPPED_ARROW),
                Component.text("Go back", NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD),
                Collections.singletonList(
                        Component.text("Return to the previous interface.", NamedTextColor.RED)
                                .decorationIfAbsent(TextDecoration.ITALIC, FALSE)), null);
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        assert potionMeta != null;
        potionMeta.setColor(Color.RED);
        item.setItemMeta(potionMeta);
        inventory.setItem(location, item);
        locationToItem.put(location, new GuiItem(command, true));
    }

    /**
     * Open this GUI for the player
     * @param player The player
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }

    /**
     * Performs an action corresponding to the clicked item
     * @param event A click event while in the GUI
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (Objects.equals(event.getClickedInventory(), inventory)) {
            GuiItem item = locationToItem.get(event.getSlot());
            if (item != null) {
                event.setCancelled(false);
                event.setResult(Event.Result.DENY);
                Player player = (Player) event.getWhoClicked();
                if (item.shouldClose)
                    player.closeInventory();
                player.performCommand(item.command);
            }
        }
    }

    /**
     * Unregister this GUI when it is closed.
     * @param event The event called when an inventory is closed.
     */
    @EventHandler
    public void onCloseGui(InventoryCloseEvent event) {
        if (Objects.equals(event.getInventory(), inventory) && shouldUnregister)
            HandlerList.unregisterAll(this);
    }

    /**
     * @param components The components to affect
     * @return The list with each root component removing Italics if not already set
     */
    public static List<Component> removeItalics(List<Component> components) {
        if (components == null) {
            return null;
        }

        ArrayList<Component> list = new ArrayList<>();
        for (Component c : components) {
            if (c != null) {
                c = c.decorationIfAbsent(TextDecoration.ITALIC, FALSE);
                list.add(c);
            }
        }
        return list;
    }
}
