package com.group3.sem3exam.logic.chat.messages;

import java.util.Map;

public interface OutMessage extends Message
{

    /**
     * Returns the payload of the message.
     *
     * @return The payload of the message.
     */
    Map<String, ?> getPayload();
}
