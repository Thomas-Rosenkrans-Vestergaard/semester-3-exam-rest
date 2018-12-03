package com.group3.sem3exam.logic.chat;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.chat.messages.InMessage;

public interface ChatTransportInput
{

    /**
     * Called when a message is received by the chat server.
     *
     * @param authenticationContext The authentication context of the closed connection.
     * @param receiver              The receiver of the message.
     * @param message               The message sent by the client.
     */
    void onMessage(AuthenticationContext authenticationContext, User receiver, InMessage message);
}
