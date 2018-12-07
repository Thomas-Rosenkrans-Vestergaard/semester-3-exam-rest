package com.group3.sem3exam.logic.chat;

import com.group3.sem3exam.logic.chat.messages.InMessage;

public interface ChatTransportInput
{

    /**
     * Called when a message is received by the chat server.
     *
     * @param message The message sent by the client.
     */
    void onMessage(InMessage message);

    /**
     * Called when the provided connection is opened.
     *
     * @param connection The connection that was opened.
     */
    void onConnect(ChatConnection connection);

    /**
     * Called when the provided connection is closed.
     *
     * @param connection The connection that was closed.
     */
    void onClose(ChatConnection connection);
}
