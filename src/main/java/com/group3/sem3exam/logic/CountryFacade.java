package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.entities.Country;
import com.group3.sem3exam.data.repositories.CountryRepository;

import java.util.List;
import java.util.function.Supplier;

public class CountryFacade
{

    /**
     * The factory that produces country repositories used by this facade.
     */
    private final Supplier<CountryRepository> countryRepositoryFactory;

    /**
     * Creates a new {@link CountryFacade}.
     *
     * @param countryRepositoryFactory The factory that produces country repositories used by this facade.
     */
    public CountryFacade(Supplier<CountryRepository> countryRepositoryFactory)
    {
        this.countryRepositoryFactory = countryRepositoryFactory;
    }

    /**
     * Returns the country with the provided id.
     *
     * @param id The id of the country to return.
     * @return The country with the provided id.
     * @throws ResourceNotFoundException When a country with the provided id does not exist.
     */
    public Country get(Integer id) throws ResourceNotFoundException
    {
        try (CountryRepository cr = countryRepositoryFactory.get()) {
            Country country = cr.get(id);
            if (country == null)
                throw new ResourceNotFoundException(Country.class, id);

            return country;
        }
    }

    /**
     * Returns all the countries in the system.
     *
     * @return All the countries in the system.
     */
    public List<Country> all()
    {
        try (CountryRepository cr = countryRepositoryFactory.get()) {
            return cr.get();
        }
    }
}

