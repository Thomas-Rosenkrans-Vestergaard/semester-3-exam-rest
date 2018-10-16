package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Region;

import java.util.List;
import java.util.stream.Collectors;

public class RegionDTO
{
    private String        name;
    private List<CityDTO> cities;
    private CountryDTO    country;

    public RegionDTO(Region region)
    {
        this(region, true, true);
    }

    public RegionDTO(Region region, boolean showCities, boolean showCountry)
    {
        this.name = region.getName();
        if (showCities)
            this.cities = region.getCities()
                                .stream()
                                .map(city -> new CityDTO(city))
                                .collect(Collectors.toList());
        if (showCountry)
            this.country = new CountryDTO(region.getCountry());
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<CityDTO> getCities()
    {
        return cities;
    }

    public void setCities(List<CityDTO> cities)
    {
        this.cities = cities;
    }

    public CountryDTO getCountryDTO()
    {
        return countryDTO;
    }

    public void setCountryDTO(CountryDTO countryDTO)
    {
        this.countryDTO = countryDTO;
    }
}
