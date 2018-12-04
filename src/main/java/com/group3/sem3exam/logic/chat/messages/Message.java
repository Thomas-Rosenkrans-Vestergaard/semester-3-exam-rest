package com.group3.sem3exam.logic.chat.messages;

public interface Message
{

    /**
     * Returns the type of the message, should be unique across the messages in chat.
     *
     * @return The type of the message.
     */
    String getType();
}
