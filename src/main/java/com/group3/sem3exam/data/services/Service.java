package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.repositories.base.RepositoryEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

public class Service implements RepositoryEntity<Integer>
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String secret;

    @Column(nullable = false)
    private String onAuth;

    public Service()
    {
    }

    public Service(String name, String passwordHash, Status status, String secret, String onAuth)
    {
        this.name = name;
        this.passwordHash = passwordHash;
        this.status = status;
        this.secret = secret;
        this.onAuth = onAuth;
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPasswordHash()
    {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = passwordHash;
    }

    public Status getStatus()
    {
        return this.status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public String getSecret()
    {
        return this.secret;
    }

    public void setSecret(String secret)
    {
        this.secret = secret;
    }

    public String getOnAuth()
    {
        return this.onAuth;
    }

    public void setOnAuth(String onAuth)
    {
        this.onAuth = onAuth;
    }

    public enum Status
    {
        PENDING,
        ENABLED,
        DISABLED
    }
}
