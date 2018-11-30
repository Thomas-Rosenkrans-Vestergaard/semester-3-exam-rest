package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PermissionUpdated
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Service service;

    private LocalDateTime time;

    public PermissionUpdated()
    {

    }

    public PermissionUpdated(User user, Service service, LocalDateTime time)
    {
        this.user = user;
        this.service = service;
        this.time = time;
    }

    public Integer getId()
    {
        return this.id;
    }

    public PermissionUpdated setId(Integer id)
    {
        this.id = id;
        return this;
    }

    public User getUser()
    {
        return this.user;
    }

    public PermissionUpdated setUser(User user)
    {
        this.user = user;
        return this;
    }

    public Service getService()
    {
        return this.service;
    }

    public PermissionUpdated setService(Service service)
    {
        this.service = service;
        return this;
    }

    public LocalDateTime getTime()
    {
        return this.time;
    }

    public PermissionUpdated setTime(LocalDateTime time)
    {
        this.time = time;
        return this;
    }
}
