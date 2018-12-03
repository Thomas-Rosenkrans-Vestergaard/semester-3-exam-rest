package com.group3.sem3exam.logic.chat.messages;

import com.group3.sem3exam.data.entities.User;

public interface InTextMessage extends InMessage
{

    @Override
    default String getType()
    {
        return "text";
    }

    User getReceiver();

    String getContents();

    static InTextMessage of(User sender, User receiver, String contents)
    {
        return new InTextMessage()
        {
            @Override
            public String getContents()
            {
                return contents;
            }

            @Override
            public User getSender()
            {
                return sender;
            }

            @Override
            public User getReceiver()
            {
                return receiver;
            }
        };
    }
}
