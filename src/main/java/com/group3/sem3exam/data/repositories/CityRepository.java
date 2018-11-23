package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.City;

import java.util.List;

/**
 * Represents a data source of users. Defines read operations on the data source.
 */
public interface CityRepository extends ReadRepository<City, Integer>
{

    /**
     * Returns the cities in the region with the provided id.
     *
     * @param region The id of the region to return the cities of.
     * @return The cities in the region with the provided id.
     */
    List<City> getByRegion(int region);
}
