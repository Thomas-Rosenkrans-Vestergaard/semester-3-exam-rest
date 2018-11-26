package com.group3.sem3exam.data.entities;

import com.group3.sem3exam.data.repositories.base.RepositoryEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "comment")
public class Comment implements RepositoryEntity<Integer>
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

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Comment()
    {

    }

    public Comment(String contents, User author, Post post, LocalDateTime createdAt)
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

    public LocalDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}
