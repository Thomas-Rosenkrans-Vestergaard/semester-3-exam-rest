package com.group3.sem3exam.data.entities;

import javax.persistence.*;

@Entity
@Table(name = "third_party")
public class ThirdParty
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String passwordHash;

    public ThirdParty()
    {

    }

    public ThirdParty(String name, String passwordHash)
    {
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPasswordHash()
    {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = passwordHash;
    }
}
