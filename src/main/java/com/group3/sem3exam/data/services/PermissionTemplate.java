package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.RepositoryEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "permission_template")
public class PermissionTemplate implements RepositoryEntity<String>
{

    @Id
    @GeneratedValue(generator = SecureRandomGenerator.name)
    @GenericGenerator(name = SecureRandomGenerator.name, strategy = "com.group3.sem3exam.data.services.SecureRandomGenerator")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String message;

    @OneToMany(mappedBy = "template", fetch = FetchType.EAGER)
    private List<PermissionMapping> permissions;

    @ManyToOne
    private Service service;

    public PermissionTemplate()
    {

    }

    public PermissionTemplate(String name, String message, List<PermissionMapping> permissions, Service service)
    {
        this.name = name;
        this.message = message;
        this.permissions = permissions;
        this.service = service;
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public PermissionTemplate setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<Permission> getPermissions()
    {
        return this.permissions.stream().map(PermissionMapping::getPermission).collect(Collectors.toList());
    }

    public PermissionTemplate setPermissions(List<PermissionMapping> permissions)
    {
        this.permissions = permissions;
        return this;
    }

    public Service getService()
    {
        return this.service;
    }

    public void setService(Service service)
    {
        this.service = service;
    }
}
