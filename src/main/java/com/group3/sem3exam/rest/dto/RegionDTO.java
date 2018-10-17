package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegionDTO
{

    public Integer       id;
    public String        name;
    public String        code;
    public List<CityDTO> cities = new ArrayList();
    public CountryDTO    country;

    public RegionDTO(Region region, boolean withCities, boolean withCountry)
    {
        this.name = region.getName();
        if (withCities)
            this.cities = region.getCities()
                                .stream()
                                .map(city -> CityDTO.basic(city))
                                .collect(Collectors.toList());
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
