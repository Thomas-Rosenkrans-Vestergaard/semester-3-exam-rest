package com.group3.sem3exam.data.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "friend_request")
public class FriendRequest
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    private User requester;

    @ManyToOne(optional = false)
    private User receiver;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    public FriendRequest()
    {

    }

    public FriendRequest(User requester, User receiver)
    {
        this.requester = requester;
        this.receiver = receiver;
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public User getRequester()
    {
        return this.requester;
    }

    public void setRequester(User requester)
    {
        this.requester = requester;
    }

    public User getReceiver()
    {
        return this.receiver;
    }

    public void setReceiver(User receiver)
    {
        this.receiver = receiver;
    }

    public LocalDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}
