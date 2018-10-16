package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Region;

public class CityDTO
{
    private String name;
    private String zipCode;
    private RegionDTO region;

    public CityDTO(City city)
    {
        this(city, true);
    }


    public CityDTO(City city, boolean showRegion)
    {
        this.name = city.getName();
        this.zipCode = city.getZipCode();
        if(showRegion)
            this.region = new RegionDTO(city.getRegion(), false, false);

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public RegionDTO getRegion()
    {
        return region;
    }

    public void setRegion(RegionDTO region)
    {
        this.region = region;
    }

    public static CityDTO basic(City city)
    {
        return new CityDTO(city);
    }
}


