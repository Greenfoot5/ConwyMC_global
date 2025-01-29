package me.greenfoot5.conwymc.commands.donator;

import me.greenfoot5.conwymc.data_types.PlayerData;

/**
 * Manages custom join messages
 */
public class JoinMessageCommand extends CustomMessageCommand {

    @Override
    protected void setMessageData(PlayerData data, String message) {
        data.getCosmetics().setJoinMessage(message, data);
    }

    @Override
    protected String getMessageType() {
        return "join";
    }
}
