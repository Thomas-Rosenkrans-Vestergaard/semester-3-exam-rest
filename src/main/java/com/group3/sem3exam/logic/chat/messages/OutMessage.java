package com.group3.sem3exam.logic.chat.messages;

import com.group3.sem3exam.logic.chat.ChatConnection;

public interface OutMessage extends Message
{

    /**
     * Returns the user id of the receiver.
     *
     * @return The user id of the receiver.
     */
    ChatConnection getReceiver();
}
