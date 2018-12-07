package com.group3.sem3exam.logic.services;

import com.group3.sem3exam.data.entities.User;

import java.time.LocalDateTime;

/**
 * The view of the authenticated user sent to the service, when the user
 * successfully authenticates as a response to a authentication request.
 */
public class UserTransfer
{
    Integer       id;
    String        name;
    String        email;
    LocalDateTime createdAt;

    public UserTransfer(User user)
    {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
    }
}