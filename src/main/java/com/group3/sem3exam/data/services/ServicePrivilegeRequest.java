package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.RepositoryEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

public class ServicePrivilegeRequest implements RepositoryEntity<Integer>
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private ServiceAuthTemplate declaration;

    @Column(nullable = false)
    private Status status;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    public ServicePrivilegeRequest()
    {

    }

    public ServicePrivilegeRequest(User user, ServiceAuthTemplate declaration)
    {
        this.user = user;
        this.declaration = declaration;
        this.status = Status.PENDING;
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public ServiceAuthTemplate getDeclaration()
    {
        return this.declaration;
    }

    public void setDeclaration(ServiceAuthTemplate declaration)
    {
        this.declaration = declaration;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public Status getStatus()
    {
        return this.status;
    }

    public LocalDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public enum Status
    {
        PENDING,
        ACCEPTED,
        REJECTED,
    }
}
