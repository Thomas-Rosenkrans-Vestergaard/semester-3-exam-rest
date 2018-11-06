package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.repositories.CityRepository;

import java.util.List;
import java.util.function.Supplier;

public class CityFacade
{

    /**
     * The factory that produces city repositories used by this facade.
     */
    private final Supplier<CityRepository> cityRepositoryFactory;

    /**
     * Creates a new {@link CityFacade}.
     *
     * @param cityRepositoryFactory The factory that produces city repositories used by this facade.
     */
    public CityFacade(Supplier<CityRepository> cityRepositoryFactory)
    {
        this.cityRepositoryFactory = cityRepositoryFactory;
    }

    /**
     * Returns the city with the provided id.
     *
     * @param id The id of the city to return.
     * @return The city with the provided id.
     * @throws ResourceNotFoundException When a city with the provided id does not exist.
     */
    public City get(Integer id) throws ResourceNotFoundException
    {
        try (CityRepository cr = cityRepositoryFactory.get()) {
            City city = cr.get(id);
            if (city == null)
                throw ResourceNotFoundException.with404(City.class, id);

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
        try (CityRepository cr = cityRepositoryFactory.get()) {
            return cr.getByRegion(region);
        }
    }
}
