package com.group3.sem3exam.logic.services;

import com.group3.sem3exam.data.services.AuthRequest;

import java.time.LocalDateTime;

public class AuthRequestTransfer
{
    public final String                     id;
    public final String                     callback;
    public final ServiceTransfer            service;
    public final AuthRequest.Status         status;
    public final LocalDateTime              createdAt;
    public final PermissionTemplateTransfer template;

    public AuthRequestTransfer(AuthRequest request, boolean withService, boolean withTemplate)
    {
        this.id = request.getId();
        this.callback = request.getCallback();
        this.service = withService ? new ServiceTransfer(request.getService()) : null;
        this.status = request.getStatus();
        this.createdAt = request.getCreatedAt();
        this.template = withTemplate ? new PermissionTemplateTransfer(request.getTemplate(), false) : null;
    }
}
