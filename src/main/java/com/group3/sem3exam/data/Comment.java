package com.group3.sem3exam.data;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "comment")
public class Comment
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(length = 65535, columnDefinition = "TEXT")
    private String contents;

    @ManyToOne
    private User author;

    @ManyToOne
    private Post post;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(nullable = false)
    private Calendar createdAt;

    public Comment()
    {

    }

    public Comment(String contents, User author, Post post, Calendar createdAt)
    {
        this.contents = contents;
        this.author = author;
        this.post = post;
        this.createdAt = createdAt;
    }

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

    public User getAuthor()
    {
        return this.author;
    }

    public void setAuthor(User author)
    {
        this.author = author;
    }

    public Post getPost()
    {
        return this.post;
    }

    public void setPost(Post post)
    {
        this.post = post;
    }

    public Calendar getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(Calendar createdAt)
    {
        this.createdAt = createdAt;
    }
}
