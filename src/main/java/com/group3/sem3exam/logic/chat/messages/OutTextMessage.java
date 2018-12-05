package com.group3.sem3exam.logic.chat.messages;

import java.util.HashMap;
import java.util.Map;

public class OutTextMessage implements OutMessage
{

    private static final String  TYPE = "text";
    private final        Integer sender;
    private final        String  contents;

    public OutTextMessage(Integer sender, String contents)
    {
        this.sender = sender;
        this.contents = contents;
    }

    /**
     * Returns the type of the message, should be unique across the messages in chat.
     *
     * @return The type of the message.
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
        payload.put("contents", contents);
        payload.put("sender", sender);
        return payload;
    }
}
