package com.group3.sem3exam.logic.chat.messages;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.logic.chat.ChatConnection;

import java.util.HashMap;
import java.util.Map;

public interface OutTextMessage extends OutMessage
{
    @Override
    default String getType()
    {
        return "text";
    }

    static OutTextMessage of(ChatConnection receiver, User sender, String contents)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", contents);
        payload.put("sender", sender.getId());

        return new OutTextMessage()
        {
            /**
             * Returns the payload of the message.
             *
             * @return The payload of the message.
             */
            @Override
            public Map<String, ?> getPayload()
            {
                return payload;
            }

            @Override
            public ChatConnection getReceiver()
            {
                return receiver;
            }
        };
    }
}
