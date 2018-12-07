package com.group3.sem3exam.data.services.entities;


import com.group3.sem3exam.data.repositories.base.RepositoryEntity;
import com.group3.sem3exam.data.services.SecureRandomGenerator;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "auth_request")
public class AuthRequest implements RepositoryEntity<String>
{

    @Id
    @GeneratedValue(generator = SecureRandomGenerator.name)
    @GenericGenerator(name = SecureRandomGenerator.name, strategy = "com.group3.sem3exam.data.services.SecureRandomGenerator")
    private String id;

    @Column(nullable = false)
    private String callback;

    @ManyToOne(optional = false)
    private Service service;

    @Column
    private Status status;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private PermissionTemplate template;

    public AuthRequest()
    {

    }

    public AuthRequest(Service service, String callback, PermissionTemplate template)
    {
        this.service = service;
        this.callback = callback;
        this.status = Status.READY;
        this.template = template;
        this.createdAt = LocalDateTime.now(); // Must be set because of the chosen @GeneratedValue strategy
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

    public PermissionTemplate getTemplate()
    {
        return this.template;
    }

    public void setTemplate(PermissionTemplate template)
    {
        this.template = template;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthRequest that = (AuthRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }
}
