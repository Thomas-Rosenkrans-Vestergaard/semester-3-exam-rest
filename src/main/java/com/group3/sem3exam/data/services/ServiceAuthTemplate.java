package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.RepositoryEntity;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

public class ServiceAuthTemplate implements RepositoryEntity<Integer>
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String message;

    @ElementCollection(targetClass = ServicePrivilege.class)
    @CollectionTable(name = "request_privileges")
    @Column(name = "request")
    private List<ServicePrivilege> privileges;

    @ManyToOne
    private Service service;

    public ServiceAuthTemplate()
    {

    }

    public ServiceAuthTemplate(String message, List<ServicePrivilege> privileges, Service service)
    {
        this.message = message;
        this.privileges = privileges;
        this.service = service;
    }

    @Override
    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<ServicePrivilege> getPrivileges()
    {
        return this.privileges;
    }

    public void setPrivileges(List<ServicePrivilege> privileges)
    {
        this.privileges = privileges;
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
