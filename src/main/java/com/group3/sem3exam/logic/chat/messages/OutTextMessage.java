package com.group3.sem3exam.logic.chat.messages;

import com.group3.sem3exam.logic.chat.ChatConnection;

public interface OutTextMessage extends OutMessage
{
    @Override
    default String getType()
    {
        return "text";
    }

    ChatConnection getSender();

    String getContents();

    static OutTextMessage of(ChatConnection receiver, ChatConnection sender, String contents)
    {
        return new OutTextMessage()
        {
            @Override
            public ChatConnection getSender()
            {
                return sender;
            }

            @Override
            public String getContents()
            {
                return contents;
            }

            @Override
            public ChatConnection getReceiver()
            {
                return receiver;
            }
        };
    }
}
