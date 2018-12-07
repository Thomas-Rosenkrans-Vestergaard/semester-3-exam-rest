package com.group3.sem3exam.rest.services;

import com.group3.sem3exam.data.services.entities.Permission;
import com.group3.sem3exam.data.services.entities.PermissionTemplate;

import java.util.List;

public class PermissionTemplateDTO
{
    private String           id;
    private String           name;
    private String           message;
    private List<Permission> permissions;
    private ServiceDTO       service;

    public PermissionTemplateDTO(PermissionTemplate template)
    {
        this.id = template.getId();
        this.name = template.getName();
        this.message = template.getMessage();
        this.permissions = template.getPermissions();
        this.service = ServiceDTO.basic(template.getService());
    }

    public static PermissionTemplateDTO basic(PermissionTemplate template)
    {
        return new PermissionTemplateDTO(template);
    }
}
