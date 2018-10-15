package com.group3.sem3exam.data.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "region")
public class Region
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = LAZY, mappedBy = "region")
    private List<City> cities = new ArrayList();

    @ManyToOne(fetch = EAGER, optional = false)
    private Country country;

    public Region()
    {
    }

    public Region(String name)
    {
        this.name = name;
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
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<City> getCities()
    {
        return this.cities;
    }

    public void setCities(List<City> cities)
    {
        this.cities = cities;
    }

    public Country getCountry()
    {
        return this.country;
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }
}




