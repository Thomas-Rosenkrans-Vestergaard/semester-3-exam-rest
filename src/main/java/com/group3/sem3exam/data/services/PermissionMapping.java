package com.group3.sem3exam.data.services;

import javax.persistence.*;

@Entity
public class PermissionMapping
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private PermissionTemplate template;
    private Permission         permission;

    public PermissionMapping()
    {

    }

    public PermissionMapping(PermissionTemplate template, Permission permission)
    {
        this.template = template;
        this.permission = permission;
    }

    public Integer getId()
    {
        return this.id;
    }

    public PermissionMapping setId(Integer id)
    {
        this.id = id;
        return this;
    }

    public PermissionTemplate getTemplate()
    {
        return this.template;
    }

    public PermissionMapping setTemplate(PermissionTemplate template)
    {
        this.template = template;
        return this;
    }

    public Permission getPermission()
    {
        return this.permission;
    }

    public PermissionMapping setPermission(Permission permission)
    {
        this.permission = permission;
        return this;
    }
}
