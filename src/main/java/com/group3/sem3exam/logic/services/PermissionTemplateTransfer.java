package com.group3.sem3exam.logic.services;

import com.group3.sem3exam.data.services.Permission;
import com.group3.sem3exam.data.services.PermissionTemplate;

import java.util.List;

public class PermissionTemplateTransfer
{

    public final String           id;
    public final String           message;
    public final List<Permission> permissions;
    public final ServiceTransfer  service;

    public PermissionTemplateTransfer(PermissionTemplate template, boolean withService)
    {
        this.id = template.getId();
        this.message = template.getMessage();
        this.permissions = template.getPermissions();
        this.service = withService ? new ServiceTransfer(template.getService()) : null;
    }
}
