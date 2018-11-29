package com.group3.sem3exam.logic.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.services.AuthRequest;
import com.group3.sem3exam.data.services.Permission;

import java.util.List;

public class AuthResponseTransfer
{
    public final AuthRequestTransfer request;
    public final UserTransfer        user;
    public final String              token;
    public final List<Permission>    permissions;

    public AuthResponseTransfer(AuthRequest request, User user, String token, List<Permission> permissions)
    {
        this.request = new AuthRequestTransfer(request, false, false);
        this.user = new UserTransfer(user);
        this.token = token;
        this.permissions = permissions;
    }
}
