package me.huntifi.conwymc.events;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TabComplete implements Listener {
    public List<String> allowedCommands = List.of(
            "discordsrv", // TODO - Replace with CS Link
            "list",
            "vote"
            // TODO - /votegui with CS
    );

    public List<String> allowedPlugins = List.of("ConwyMC", "CastleSiege", "Fishing");

    @EventHandler
    public void onTabComplete(PlayerCommandSendEvent e) {
        if (e.getPlayer().hasPermission("conwymc.developer")) {
            return;
        }

        Collection<String> commands = new ArrayList<>(e.getCommands());
        e.getCommands().clear();

        for (String command : commands) {
            PluginCommand pluginCommand = Bukkit.getServer().getPluginCommand(command);
            if (allowedCommands.contains(command) ||
                    (pluginCommand != null && allowedPlugins.contains(pluginCommand.getPlugin().getName())))
            {
                e.getCommands().add(command);
            }
        }
    }
}
