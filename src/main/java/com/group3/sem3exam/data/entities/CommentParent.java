package com.group3.sem3exam.data.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CommentParent
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "parent")
    private List<Comment> comments = new ArrayList<>();

    public CommentParent()
    {

    }

    public Integer getId()
    {
        return this.id;
    }

    public CommentParent setId(Integer id)
    {
        this.id = id;
        return this;
    }

    public List<Comment> getComments()
    {
        return this.comments;
    }

    public CommentParent setComments(List<Comment> comments)
    {
        this.comments = comments;
        return this;
    }
}
