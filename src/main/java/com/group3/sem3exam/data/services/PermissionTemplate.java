package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.RepositoryEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "auth_request_perms", joinColumns = @JoinColumn(name = "template_id"))
    private List<Permission> permissions;

    @ManyToOne
    private Service service;

    public PermissionTemplate()
    {

    }

    public PermissionTemplate(String name, String message, List<Permission> permissions, Service service)
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
        return this.permissions;
    }

    public void setPermissions(List<Permission> permissions)
    {
        this.permissions = permissions;
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
