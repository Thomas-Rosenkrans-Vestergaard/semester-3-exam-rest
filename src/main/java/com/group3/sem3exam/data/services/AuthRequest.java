package com.group3.sem3exam.data.services;


import com.group3.sem3exam.data.repositories.base.RepositoryEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "auth_request")
public class AuthRequest implements RepositoryEntity<String>
{

    @Id
    @GeneratedValue(generator = SecureRandomGenerator.name)
    @GenericGenerator(name = SecureRandomGenerator.name, strategy = "com.group3.sem3exam.data.services.SecureRandomGenerator")
    private String id;

    @Column(nullable = false)
    private Integer timeout;

    @Column(nullable = false)
    private String callback;

    @ManyToOne(optional = false)
    private Service service;

    private Status status;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    public AuthRequest()
    {

    }

    public AuthRequest(Service service, String callback, Integer timeout)
    {
        this.service = service;
        this.callback = callback;
        this.timeout = timeout;
        this.status = Status.READY;
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    public AuthRequest setId(String id)
    {
        this.id = id;
        return this;
    }

    public Service getService()
    {
        return this.service;
    }

    public AuthRequest setService(Service service)
    {
        this.service = service;
        return this;
    }

    public String getCallback()
    {
        return this.callback;
    }

    public AuthRequest setCallback(String callback)
    {
        this.callback = callback;
        return this;
    }

    public LocalDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public AuthRequest setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
        return this;
    }

    public Integer getTimeout()
    {
        return this.timeout;
    }

    public AuthRequest setTimeout(Integer timeout)
    {
        this.timeout = timeout;
        return this;
    }

    public enum Status
    {
        READY,
        COMPLETED
    }

    public Status getStatus()
    {
        return this.status;
    }

    public AuthRequest setStatus(Status status)
    {
        this.status = status;
        return this;
    }
}
