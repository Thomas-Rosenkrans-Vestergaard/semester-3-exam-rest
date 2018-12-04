package com.group3.sem3exam.logic.chat.messages;

import com.group3.sem3exam.logic.chat.ChatConnection;

import java.util.Map;

public interface OutMessage extends Message
{

    /**
     * Returns the user id of the receiver.
     *
     * @return The user id of the receiver.
     */
    ChatConnection getReceiver();

    /**
     * Returns the payload of the message.
     *
     * @return The payload of the message.
     */
    Map<String, ?> getPayload();
}
