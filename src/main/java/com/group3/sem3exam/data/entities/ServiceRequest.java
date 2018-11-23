package com.group3.sem3exam.data.entities;

import com.group3.sem3exam.data.repositories.RepositoryEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

public class ServiceRequest implements RepositoryEntity<Integer>
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private ServiceRequestTemplate declaration;

    @Column(nullable = false)
    private Status status;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    public ServiceRequest()
    {

    }

    public ServiceRequest(User user, ServiceRequestTemplate declaration)
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

    public ServiceRequestTemplate getDeclaration()
    {
        return this.declaration;
    }

    public void setDeclaration(ServiceRequestTemplate declaration)
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
