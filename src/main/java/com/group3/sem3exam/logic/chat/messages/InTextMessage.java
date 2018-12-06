package com.group3.sem3exam.logic.chat.messages;

import com.group3.sem3exam.logic.chat.ChatConnection;

public interface InTextMessage extends InMessage
{

    @Override
    default String getType()
    {
        return "text";
    }

    Integer getReceiver();

    String getContents();

    static InTextMessage of(ChatConnection sender, Integer receiver, String contents)
    {
        return new InTextMessage()
        {
            @Override
            public String getContents()
            {
                return contents;
            }

            @Override
            public ChatConnection getSender()
            {
                return sender;
            }

            @Override
            public Integer getReceiver()
            {
                return receiver;
            }
        };
    }
}
