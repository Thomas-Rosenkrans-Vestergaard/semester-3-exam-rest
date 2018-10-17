package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.City;

public class CityDTO
{

    public Integer   id;
    public String    name;
    public String    latitude;
    public String    longitude;
    public RegionDTO region;

    public CityDTO(City city, boolean withRegion)
    {
        this.id = city.getId();
        this.name = city.getName();
        this.latitude = city.getLatitude();
        this.longitude = city.getLongitude();
        if (withRegion)
            this.region = RegionDTO.basic(city.getRegion());
    }

    public static CityDTO basic(City city)
    {
        return new CityDTO(city, false);
    }

    public static CityDTO withRegion(City city)
    {
        return new CityDTO(city, true);
    }
}


