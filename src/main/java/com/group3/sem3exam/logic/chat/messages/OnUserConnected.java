package com.group3.sem3exam.logic.chat.messages;

import java.util.HashMap;
import java.util.Map;

public class OnUserConnected implements OutMessage
{

    private static final String  TYPE = "user-connected";
    private final        Integer user;

    public OnUserConnected(Integer user)
    {
        this.user = user;
    }

    /**
     * Returns the TYPE of the message, should be unique across the messages in chat.
     *
     * @return The TYPE of the message.
     */
    @Override
    public String getType()
    {
        return TYPE;
    }

    /**
     * Returns the payload of the message.
     *
     * @return The payload of the message.
     */
    @Override
    public Map<String, ?> getPayload()
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user", user);
        return payload;
    }
}
