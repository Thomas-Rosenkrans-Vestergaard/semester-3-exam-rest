package com.group3.sem3exam.logic.chat;

import com.group3.sem3exam.logic.chat.messages.OutMessage;

public interface ChatTransportOutput
{

    /**
     * Sends the provided message to the provided user.
     * .
     *
     * @param message The message to send to the user.
     */
    void send(OutMessage message);
}
