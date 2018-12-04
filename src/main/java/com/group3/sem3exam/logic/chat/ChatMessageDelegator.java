package com.group3.sem3exam.logic.chat;

import com.group3.sem3exam.logic.chat.messages.InMessage;

import java.util.HashMap;
import java.util.Map;

public class ChatMessageDelegator
{

    interface Accept<T>
    {
        void accept(T message) throws Exception;
    }

    private final Map<Class<? extends InMessage>, Accept> routes = new HashMap<>();

    public <T extends InMessage> void register(Class<T> c, Accept<T> consumer)
    {
        routes.put(c, consumer);
    }

    public void handle(InMessage message) throws Exception
    {
        Class<? extends InMessage> c = message.getClass();
        if (routes.containsKey(c))
            routes.get(c).accept(message);
    }
}
