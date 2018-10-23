package com.group3.sem3exam.data.entities;

import com.group3.sem3exam.data.repositories.RepositoryEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "post")
public class Post implements RepositoryEntity<Integer>
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Post()
    {

    }

    public Post(String contents, User author)
    {
        this.contents = contents;
        this.author = author;
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

    public List<Comment> getComments()
    {
        return this.comments;
    }

    public void setComments(List<Comment> comments)
    {
        this.comments = comments;
        for (Comment comment : comments)
            comment.setPost(this);
    }

    public void addComment(Comment comment)
    {
        this.comments.add(comment);
        comment.setPost(this);
    }
}
