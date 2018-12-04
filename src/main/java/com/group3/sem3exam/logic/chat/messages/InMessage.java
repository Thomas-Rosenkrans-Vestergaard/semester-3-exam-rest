package com.group3.sem3exam.logic.chat.messages;

import com.group3.sem3exam.logic.chat.ChatConnection;

public interface InMessage extends Message
{

    /**
     * Returns the user id of the sender.
     *
     * @return The user id of the sender.
     */
    ChatConnection getSender();

    /**
     * Returns the authentication token sent by the sender.
     *
     * @return The authentication token sent by the sender.
     */
    String getAuthToken();
}
