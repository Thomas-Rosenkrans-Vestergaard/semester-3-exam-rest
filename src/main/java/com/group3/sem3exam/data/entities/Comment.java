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

    @Column(nullable = false, length = 1024)
    private String contents;

    @ManyToOne
    private User author;

    @Column
    private String emoji;

    @Column
    private int count;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Must be fetch type LAZY.
    // The derby query generated from the above code fails, since derby performs a UNION operation
    //  to retrieve the results.
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentParent parent;

    public Comment()
    {

    }

    public String getEmoji()
    {
        return emoji;
    }

    public void setEmoji(String emoji)
    {
        this.emoji = emoji;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
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
