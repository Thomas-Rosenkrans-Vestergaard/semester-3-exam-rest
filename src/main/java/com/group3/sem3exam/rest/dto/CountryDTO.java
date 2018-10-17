package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Country;

import java.util.List;

public class CountryDTO
{

    public Integer         id;
    public String          name;
    public String          code;
    public List<RegionDTO> regions;

    public CountryDTO(Country country, boolean withRegions)
    {
        this.id = country.getId();
        this.name = country.getName();
        this.code = country.getCode();
    }

    public static CountryDTO basic(Country country)
    {
        return new CountryDTO(country, false);
    }

    public static CountryDTO withRegions(Country country)
    {
        return new CountryDTO(country, true);
    }
}
