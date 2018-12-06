package com.group3.sem3exam.data.entities;

import com.group3.sem3exam.data.repositories.base.RepositoryEntity;

import javax.persistence.*;

@Entity
public class ChatMessage implements RepositoryEntity<Integer>
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    public ChatMessage()
    {

    }

    public ChatMessage(User sender, User receiver, String contents)
    {
        this.contents = contents;
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getContents()
    {
        return this.contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public User getSender()
    {
        return this.sender;
    }

    public void setSender(User sender)
    {
        this.sender = sender;
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
