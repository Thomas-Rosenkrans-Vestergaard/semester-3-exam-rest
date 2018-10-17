package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.Country;
import com.group3.sem3exam.data.repositories.TransactionalCountryRepository;
import com.group3.sem3exam.rest.exceptions.CountryNotFoundException;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class CountryFacade
{

    private EntityManagerFactory emf;

    public CountryFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    /**
     * Returns the country with the provided id.
     *
     * @param id The id of the country to return.
     * @return The country with the provided id.
     * @throws CountryNotFoundException When a country with the provided id does not exist.
     */
    public Country get(Integer id) throws CountryNotFoundException
    {
        try (TransactionalCountryRepository tcr = new TransactionalCountryRepository(emf)) {
            Country country = tcr.get(id);
            if (country == null)
                throw new CountryNotFoundException(id);

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
        try (TransactionalCountryRepository tcr = new TransactionalCountryRepository(emf)) {
            return tcr.get();
        }
    }
}

