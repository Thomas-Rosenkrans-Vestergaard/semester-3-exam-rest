package com.group3.sem3exam.logic.chat;

import com.group3.sem3exam.data.entities.User;

public interface ChatMember
{

    /**
     * Returns the user.
     *
     * @return The user.
     */
    User getUser();

    /**
     * Returns true if the user is currently online.
     *
     * @return {@code true} if the user is currently online.
     */
    boolean isOnline();

    /**
     * Returns the number of unread messages by the user.
     *
     * @return The number of unread messages by the user.
     */
    int unreadMessages();
}
