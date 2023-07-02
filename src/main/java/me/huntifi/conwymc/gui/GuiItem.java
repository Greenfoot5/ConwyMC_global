package me.huntifi.conwymc.gui;

/**
 * An item within a GUI
 */
public class GuiItem {

    /** The command that should be executed when this item is clicked */
    public final String command;

    /** Whether the GUI should be closed when this item is clicked */
    public final boolean shouldClose;

    /**
     * Create a GUI item.
     * @param command The command that should be executed when this item is clicked
     * @param shouldClose Whether the GUI should be closed when this item is clicked
     */
    public GuiItem(String command, boolean shouldClose) {
        this.command = command;
        this.shouldClose = shouldClose;
    }
}
