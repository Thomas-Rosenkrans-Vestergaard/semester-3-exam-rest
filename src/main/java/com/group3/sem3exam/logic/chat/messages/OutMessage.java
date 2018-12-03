package com.group3.sem3exam.logic.chat.messages;

import com.group3.sem3exam.data.entities.User;

public interface OutMessage extends Message
{

    User getReceiver();
}
