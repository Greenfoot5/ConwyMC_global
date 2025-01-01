package me.greenfoot5.conwymc.gui;

import java.util.HashMap;

/**
 * Manages GUIs that should remain persistent
 */
public class GuiController {

    /** Maps key strings to the corresponding GUIs */
    private static final HashMap<String, Gui> keyToGui = new HashMap<>();

    /**
     * Save a GUI to the map of GUIs.
     * @param key The key with which the GUI can be retrieved
     * @param gui The GUI to save
     */
    public static void add(String key, Gui gui) {
        keyToGui.put(key, gui);
    }

    /**
     * Get a GUI from the map of GUIs.
     * @param key The key with which the GUI is stored
     * @return The requested GUI
     */
    public static Gui get(String key) {
        return keyToGui.get(key);
    }

    /**
     * Get a GUI from the map of GUIs if it exists.
     * Return the GUI corresponding to the default key otherwise.
     * @param key The key with which the GUI is stored
     * @param defaultKey The key of with which the default GUI
     * @return The requested GUI or the default GUI
     */
    public static Gui getOrDefault(String key, String defaultKey) {
        return keyToGui.getOrDefault(key, keyToGui.get(defaultKey));
    }
}
