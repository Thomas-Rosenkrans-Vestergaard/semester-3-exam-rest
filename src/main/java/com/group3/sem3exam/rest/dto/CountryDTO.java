package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Country;

import java.util.List;

public class CountryDTO
{

    private String name;
    private List<RegionDTO> regions;


    public CountryDTO(Country country){
        this(country, true);
    }

    public CountryDTO(Country country, boolean withRegions){
        this.name = country.getName();
    }

    public static CountryDTO basic(Country country){
        return new CountryDTO(country, false);
    }
}
