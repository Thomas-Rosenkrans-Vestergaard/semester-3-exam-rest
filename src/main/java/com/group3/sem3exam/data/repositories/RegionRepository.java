package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Region;

import java.util.List;

public interface RegionRepository extends CrudRepository<Region, Integer>
{

    /**
     * Returns the regions in the country with the provide id.
     *
     * @param country The id of the country to return the regions of.
     * @return The regions in the country with the provided id.
     */
    List<Region> getByCountry(int country);
}
