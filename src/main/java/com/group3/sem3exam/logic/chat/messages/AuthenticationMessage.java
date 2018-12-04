package com.group3.sem3exam.logic.chat.messages;

import com.group3.sem3exam.logic.chat.ChatConnection;

public interface AuthenticationMessage extends InMessage
{

    static InMessage of(String token, ChatConnection sender)
    {
        return new AuthenticationMessage()
        {
            @Override
            public String getAuthToken()
            {
                return token;
            }

            @Override
            public ChatConnection getSender()
            {
                return sender;
            }

            @Override
            public String getType()
            {
                return "authentication";
            }
        };
    }

    String getAuthToken();
}
