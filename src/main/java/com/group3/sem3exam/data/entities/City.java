package com.group3.sem3exam.data.entities;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "city")
public class City
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 15)
    private String latitude;

    @Column(nullable = false, length = 15)
    private String longitude;

    @ManyToOne(fetch = EAGER, optional = false)
    private Region region;

    public City(String name, String latitude, String longitude, Region region)
    {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
    }

    public City()
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

    public String getLatitude()
    {
        return this.latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return this.longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public Region getRegion()
    {
        return region;
    }

    public void setRegion(Region region)
    {
        this.region = region;
    }
}




