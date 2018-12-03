package com.group3.sem3exam.logic.chat.messages;

import com.group3.sem3exam.data.entities.User;

public interface InMessage extends Message
{
    User getSender();
}
