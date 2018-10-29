package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Region;

import java.util.List;

/**
 * Represents a data source of regions. Defines read operations on the data source.
 */
public interface RegionRepository extends ReadCrudRepository<Region, Integer>
{

    /**
     * Returns the regions in the country with the provide id.
     *
     * @param country The id of the country to return the regions of.
     * @return The regions in the country with the provided id.
     */
    List<Region> getByCountry(int country);
}
