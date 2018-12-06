package com.group3.sem3exam.logic.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.services.entities.AuthRequest;
import com.group3.sem3exam.data.services.entities.Permission;

import java.util.Set;

public class AuthResponseTransfer
{
    public final AuthRequestTransfer request;
    public final UserTransfer        user;
    public final String              token;
    public final Set<Permission>     permissions;

    public AuthResponseTransfer(AuthRequest request, User user, String token, Set<Permission> permissions)
    {
        this.request = new AuthRequestTransfer(request, false, false);
        this.user = new UserTransfer(user);
        this.token = token;
        this.permissions = permissions;
    }
}
