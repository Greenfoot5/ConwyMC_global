package me.huntifi.conwymc.commands.donator;

import me.huntifi.conwymc.data_types.PlayerData;

/**
 * Manages custom leave messages
 */
public class JoinMessageCommand extends CustomMessageCommand {

    @Override
    protected void setMessageData(PlayerData data, String message) {
        data.setJoinMessage(message);
    }

    @Override
    protected String getMessageType() {
        return "join";
    }
}
