package com.group3.sem3exam.data.entities;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = LAZY, optional = false)
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

}




