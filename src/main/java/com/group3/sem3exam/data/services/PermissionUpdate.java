package com.group3.sem3exam.data.services;

import com.group3.sem3exam.data.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PermissionUpdate
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Service service;

    private LocalDateTime time;

    public PermissionUpdate()
    {

    }

    public PermissionUpdate(User user, Service service, LocalDateTime time)
    {
        this.user = user;
        this.service = service;
        this.time = time;
    }

    public Integer getId()
    {
        return this.id;
    }

    public PermissionUpdate setId(Integer id)
    {
        this.id = id;
        return this;
    }

    public User getUser()
    {
        return this.user;
    }

    public PermissionUpdate setUser(User user)
    {
        this.user = user;
        return this;
    }

    public Service getService()
    {
        return this.service;
    }

    public PermissionUpdate setService(Service service)
    {
        this.service = service;
        return this;
    }

    public LocalDateTime getTime()
    {
        return this.time;
    }

    public PermissionUpdate setTime(LocalDateTime time)
    {
        this.time = time;
        return this;
    }
}
