package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.City;

import java.util.List;

public interface CityRepository extends ReadCrudRepository<City, Integer>
{

    /**
     * Returns the cities in the region with the provided id.
     *
     * @param region The id of the region to return the cities of.
     * @return The cities in the region with the provided id.
     */
    List<City> getByRegion(int region);
}
