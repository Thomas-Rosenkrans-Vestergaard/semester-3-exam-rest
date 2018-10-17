package com.group3.sem3exam.data.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "country")
public class Country
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @OneToMany(fetch = LAZY, mappedBy = "country")
    private List<Region> regions = new ArrayList();

    public Country(String name, String code)
    {
        this.name = name;
        this.code = code;
    }

    public Country()
    {

    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return this.code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public List<Region> getRegions()
    {
        return regions;
    }

    public void setRegions(List<Region> regions)
    {
        this.regions = regions;
    }
}




