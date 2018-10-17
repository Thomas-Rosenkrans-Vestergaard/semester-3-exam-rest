package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.repositories.TransactionalCityRepository;
import com.group3.sem3exam.rest.exceptions.CityNotFoundException;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class CityFacade
{

    private EntityManagerFactory emf;

    public CityFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    /**
     * Returns the city with the provided id.
     *
     * @param id The id of the city to return.
     * @return The city with the provided id.
     * @throws CityNotFoundException When a city with the provided id does not exist.
     */
    public City get(Integer id) throws CityNotFoundException
    {
        try (TransactionalCityRepository tcr = new TransactionalCityRepository(emf)) {
            City city = tcr.get(id);
            if (city == null)
                throw new CityNotFoundException(id);

            return city;
        }
    }

    /**
     * Returns the cities in the region with the provided id.
     *
     * @param region The id of the region to return the cities of.
     * @return The cities in the region with the provided id.
     */
    public List<City> getByRegion(int region)
    {
        try (TransactionalCityRepository tcr = new TransactionalCityRepository(emf)) {
            return tcr.getByRegion(region);
        }
    }
}
