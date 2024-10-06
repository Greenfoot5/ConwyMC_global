package me.huntifi.conwymc.commands.donator;

import me.huntifi.conwymc.data_types.PlayerData;

import java.util.UUID;

/**
 * Manages custom leave messages
 */
public class LeaveMessageCommand extends CustomMessageCommand {

    @Override
    protected void setMessageData(PlayerData data, String message, UUID uuid) {
        data.setSetting(uuid, "leave_message", message);
    }

    @Override
    protected String getMessageType() {
        return "leave";
    }
}
