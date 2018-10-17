package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Region;

import java.util.List;
import java.util.stream.Collectors;

public class RegionDTO
{

    public Integer       id;
    public String        name;
    public String        code;
    public List<CityDTO> cities;
    public CountryDTO    country;

    public RegionDTO(Region region, boolean withCities, boolean withCountry)
    {
        this.id = region.getId();
        this.name = region.getName();
        this.code = region.getCode();
        if (withCities)
            this.cities = region.getCities().stream().map(CityDTO::basic).collect(Collectors.toList());
        if (withCountry)
            this.country = CountryDTO.basic(region.getCountry());
    }

    public static RegionDTO basic(Region region)
    {
        return new RegionDTO(region, false, false);
    }

    public static RegionDTO withCities(Region region)
    {
        return new RegionDTO(region, true, false);
    }
}
