package com.group3.sem3exam.data.repositories;

import javax.persistence.*;

@Entity
@Table(name = "jwt")
public final class JwtEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "secret", nullable = false)
    private String secret;

    public JwtEntity()
    {

    }

    public JwtEntity(String secret)
    {
        this.secret = secret;
    }

    public Integer getId()
    {
        return this.id;
    }

    public JwtEntity setId(Integer id)
    {
        this.id = id;
        return this;
    }

    public String getSecret()
    {
        return this.secret;
    }

    public JwtEntity setSecret(String secret)
    {
        this.secret = secret;
        return this;
    }
}
