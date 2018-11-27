package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.RepositoryEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "permission_request")
public class PermissionRequest implements RepositoryEntity<String>
{

    @Id
    @GeneratedValue(generator = SecureRandomGenerator.name)
    @GenericGenerator(name = SecureRandomGenerator.name, strategy = "com.group3.sem3exam.data.services.SecureRandomGenerator")
    private String id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private PermissionTemplate declaration;

    @Column(nullable = false)
    private Status status;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    public PermissionRequest()
    {

    }

    public PermissionRequest(User user, PermissionTemplate declaration)
    {
        this.user = user;
        this.declaration = declaration;
        this.status = Status.PENDING;
    }

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
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

    public PermissionTemplate getDeclaration()
    {
        return this.declaration;
    }

    public void setDeclaration(PermissionTemplate declaration)
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
