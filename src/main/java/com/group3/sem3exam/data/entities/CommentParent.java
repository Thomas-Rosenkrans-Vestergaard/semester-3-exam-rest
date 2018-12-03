package com.group3.sem3exam.data.entities;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CommentParent
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

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
}
