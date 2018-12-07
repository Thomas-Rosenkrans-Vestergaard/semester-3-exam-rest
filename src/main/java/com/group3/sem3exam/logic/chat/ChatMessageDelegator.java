package com.group3.sem3exam.logic.chat;

import com.group3.sem3exam.logic.chat.messages.InMessage;

import java.util.HashMap;
import java.util.Map;

public class ChatMessageDelegator
{

    private final Map<String, Accept> routes = new HashMap<>();

    interface Accept<T>
    {
        public void accept(T t) throws Exception;
    }

    public <T extends InMessage> void register(String type, Class<T> t, Accept<T> consumer)
    {
        routes.put(type, consumer);
    }

    public void handle(InMessage message) throws Exception
    {
        Accept consumer = routes.get(message.getType());
        if (consumer != null)
            consumer.accept(message);
    }
}
