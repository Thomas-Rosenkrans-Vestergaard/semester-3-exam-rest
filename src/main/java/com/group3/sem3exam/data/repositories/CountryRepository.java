package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.Country;

import java.util.List;

/**
 * Represents a data source of users. Defines read operations on the data source.
 */
public interface CountryRepository extends ReadRepository<Country, Integer>
{

    /**
     * Returns a complete list of the countries in the repository.
     *
     * @return The complete list of the countries in the repository.
     */
    List<Country> get();
}
