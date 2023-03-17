package me.huntifi.conwymc.gui;

import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A GUI made with a minecraft inventory
 */
public class Gui implements Listener {

    /** The GUI's minecraft inventory */
    protected final Inventory inventory;

    /** Maps the inventory locations to the corresponding items */
    protected final HashMap<Integer, GuiItem> locationToItem = new HashMap<>();

    /** Whether this GUI should stop listening for events after being closed */
    private final boolean shouldUnregister;

    /**
     * Create an inventory.
     * @param name The name of the inventory
     * @param rows The amount of rows of the inventory
     */
    public Gui(String name, int rows) {
        this(name, rows, false);
    }

    /**
     * Create an inventory.
     * @param name The name of the inventory
     * @param rows The amount of rows of the inventory
     * @param shouldUnregister Whether this GUI should stop listening for events after being closed
     */
    public Gui(String name, int rows, boolean shouldUnregister) {
        inventory = ConwyMC.plugin.getServer().createInventory(null, 9 * rows, name);
        this.shouldUnregister = shouldUnregister;

        ConwyMC.plugin.getServer().getPluginManager().registerEvents(this, ConwyMC.plugin);
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
    public void addItem(String name, Material material, List<String> lore, int location, String command, boolean shouldClose) {
        inventory.setItem(location, ItemCreator.item(material, name, lore, null));
        locationToItem.put(location, new GuiItem(command, shouldClose));
    }

    /**
     * Open this GUI for the player.
     * @param player The player
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }

    /**
     * Perform the command corresponding to the clicked item.
     * @param event A click event while in the GUI
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (Objects.equals(event.getClickedInventory(), inventory)) {
            GuiItem item = locationToItem.get(event.getSlot());
            if (item != null) {
                Player player = (Player) event.getWhoClicked();
                if (item.shouldClose)
                    player.closeInventory();
                player.performCommand(item.command);
            }
        }
    }

    /**
     * Unregister this GUI when it is closed.
     * @param event The event called when an inventory is closed
     */
    @EventHandler
    public void onCloseGui(InventoryCloseEvent event) {
        if (Objects.equals(event.getInventory(), inventory) && shouldUnregister)
            HandlerList.unregisterAll(this);
    }
}
