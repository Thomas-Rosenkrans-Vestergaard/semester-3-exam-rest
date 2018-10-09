package com.group3.sem3exam.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "friend_request")
public class FriendRequest
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @OneToOne(optional = false)
    private User requester;

    @OneToOne(optional = false)
    private User receiver;

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
}
