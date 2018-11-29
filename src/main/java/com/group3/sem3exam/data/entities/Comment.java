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

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private CommentParent parent;

    public Comment()
    {

    }

    public Comment(String contents, User author, LocalDateTime createdAt, CommentParent parent)
    {
        this.contents = contents;
        this.author = author;
        this.createdAt = createdAt;
        this.parent = parent;
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

    public LocalDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public CommentParent getParent()
    {
        return this.parent;
    }

    public Comment setParent(CommentParent parent)
    {
        this.parent = parent;
        return this;
    }
}
