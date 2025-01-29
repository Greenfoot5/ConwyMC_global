package me.greenfoot5.conwymc.commands.donator;

import me.greenfoot5.conwymc.data_types.PlayerData;

/**
 * Manages custom leave messages
 */
public class LeaveMessageCommand extends CustomMessageCommand {

    @Override
    protected void setMessageData(PlayerData data, String message) {
        data.getCosmetics().setLeaveMessage(message, data);
    }

    @Override
    protected String getMessageType() {
        return "leave";
    }
}
