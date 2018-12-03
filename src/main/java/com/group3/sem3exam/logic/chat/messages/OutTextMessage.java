package com.group3.sem3exam.logic.chat.messages;

import com.group3.sem3exam.data.entities.User;

public interface OutTextMessage extends OutMessage
{
    @Override
    default String getType()
    {
        return "text";
    }

    User getSender();

    String getContents();

    static OutTextMessage of(User receiver, User sender, String contents)
    {
        return new OutTextMessage()
        {
            @Override
            public String getContents()
            {
                return contents;
            }

            @Override
            public User getReceiver()
            {
                return receiver;
            }

            @Override
            public User getSender()
            {
                return sender;
            }
        };
    }
}
