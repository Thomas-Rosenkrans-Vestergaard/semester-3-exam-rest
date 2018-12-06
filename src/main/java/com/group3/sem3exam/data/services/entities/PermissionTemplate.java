package com.group3.sem3exam.data.services.entities;

import com.group3.sem3exam.data.repositories.base.RepositoryEntity;
import com.group3.sem3exam.data.services.SecureRandomGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
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

    @OneToMany(mappedBy = "template", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PermissionMapping> permissionMappings;

    @ManyToOne
    private Service service;

    public PermissionTemplate()
    {

    }

    public PermissionTemplate(String name, String message, List<PermissionMapping> permissionMappings, Service service)
    {
        this.name = name;
        this.message = message;
        this.permissionMappings = permissionMappings;
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
        return this.permissionMappings.stream().map(PermissionMapping::getPermission).collect(Collectors.toList());
    }

    public List<PermissionMapping> getPermissionMappings()
    {
        return this.permissionMappings;
    }

    public PermissionTemplate setPermissionMappings(List<PermissionMapping> permissionMappings)
    {
        this.permissionMappings = permissionMappings;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionTemplate template = (PermissionTemplate) o;
        return Objects.equals(id, template.id) &&
               Objects.equals(name, template.name) &&
               Objects.equals(message, template.message) &&
               Objects.equals(permissionMappings, template.permissionMappings) &&
               Objects.equals(service, template.service);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, message, permissionMappings, service);
    }
}
