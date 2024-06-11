package me.huntifi.conwymc.gui;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.huntifi.conwymc.ConwyMC;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.ItemCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static net.kyori.adventure.text.format.TextDecoration.State.FALSE;

public class PaginatedGui implements Listener {

    protected ArrayList<Tuple<ItemStack, GuiItem>> items;
    protected HashMap<Integer, GuiItem> locationToItem;

    protected Inventory inventory;
    protected int page;
    protected int pages;
    protected Component name;

    /** Whether this GUI should stop listening for events after being closed */
    protected final boolean shouldUnregister;

    /**
     * Create an inventory.
     * @param name The name of the inventory
     */
    public PaginatedGui(Component name) {
        this(name, false);
    }

    /**
     * Create an inventory.
     * @param name The name of the inventory
     * @param shouldUnregister Whether this GUI should stop listening for events after being closed
     */
    public PaginatedGui(Component name, boolean shouldUnregister) {
        //inventory = Bukkit.getServer().createInventory(null, name);
        items = new ArrayList<>();
        this.shouldUnregister = shouldUnregister;
        this.name = name;

        Bukkit.getServer().getPluginManager().registerEvents(this, ConwyMC.plugin);
    }

    /**
     * Add an item to the inventory.
     * @param name The name of the item
     * @param material The material of the item
     * @param lore The lore of the item
     * @param command The command to execute when clicking the item
     * @param shouldClose Whether the GUI should close after performing the command
     */
    public void addItem(Component name, Material material, List<Component> lore, String command, boolean shouldClose) {
        List<Component> loreI = Gui.removeItalics(lore);
        items.add(new Tuple<>(ItemCreator.item(new ItemStack(material),
                name.decorationIfAbsent(TextDecoration.ITALIC, FALSE), loreI, null), new GuiItem(command, shouldClose)));
    }

    /**
     * @param location The location of the item
     */
    public void addBottomRow(int location, boolean showLeft, boolean showRight) {
        for (int i = 0; i < 9; i++) {
            ItemStack frame = ItemCreator.item(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1),
                    Component.empty(), null, null);
            inventory.setItem(location + i, frame);
            locationToItem.put(location + i, new GuiItem("", false));
        }

        if (showLeft) {
            ItemStack left = ItemCreator.item(new ItemStack(Material.PLAYER_HEAD, 1),
                    Component.text("Previous", NamedTextColor.GOLD), null, null);
            SkullMeta skullMeta = (SkullMeta) left.getItemMeta();
            PlayerProfile profile = Bukkit.getServer().createProfile("Previous");
            try {
                PlayerTextures textures = profile.getTextures();
                textures.setSkin(new URL("https://textures.minecraft.net/texture/841dd127595a25c2439c5db31ccb4914507ae164921aafec2b979aad1cfe7"));
                profile.setTextures(textures);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            skullMeta.setPlayerProfile(profile);
            left.setItemMeta(skullMeta);
            inventory.setItem(location, left);
            locationToItem.put(location, new GuiItem("PREVIOUS_PAGE", false));
        }

        if (showRight) {
            ItemStack right = ItemCreator.item(new ItemStack(Material.PLAYER_HEAD, 1),
                    Component.text("Next", NamedTextColor.GOLD), null, null);
            SkullMeta skullMeta = (SkullMeta) right.getItemMeta();
            PlayerProfile profile = Bukkit.getServer().createProfile("Next");
            try {
                PlayerTextures textures = profile.getTextures();
                textures.setSkin(new URL("https://textures.minecraft.net/texture/d2d0313b6680141286396e71c361e5962a39baf596d7e54771775d5fa3d"));
                profile.setTextures(textures);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            skullMeta.setPlayerProfile(profile);
            right.setItemMeta(skullMeta);
            inventory.setItem(location + 8, right);
            locationToItem.put(location + 8, new GuiItem("NEXT_PAGE", false));
        }
    }

    /**
     * Open this GUI for the player
     * @param player The player
     */
    public boolean open(Player player) {
        return openPage(player, 1);
    }

    private boolean openPage(Player player, int page) {
        pages = (items.size() / 45) + 1;
        if (page < 1 || page > pages || items.isEmpty()) {
            return false;
        }
        this.page = page;

        locationToItem = new HashMap<>();
        int start = (page - 1) * 45;
        int end = page == pages ? items.size() : start + 45;
        int height = items.size() < 45 ? end - start : 45;
        // Confirm height is a multiple of 9
        height = height / 9 * 9 + (height % 9 == 0 ? 0 : 9);

        if (inventory == null)
            inventory = Bukkit.getServer().createInventory(null, height + 9, name);
        else
            inventory.clear();

        for (int i = start; i < end; i++) {
            inventory.addItem(items.get(i).getFirst());
            locationToItem.put(i - start, items.get(i).getSecond());
        }

        addBottomRow(height, page > 1, page < pages);

        player.openInventory(inventory);
        return true;
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

                if (Objects.equals(item.command, "PREVIOUS_PAGE")) {
                    openPage(player, page - 1);
                } else if (Objects.equals(item.command, "NEXT_PAGE")) {
                    openPage(player, page + 1);
                } else {
                    player.performCommand(item.command);
                }
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
}
