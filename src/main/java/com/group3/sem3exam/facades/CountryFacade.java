package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Country;
import com.group3.sem3exam.data.repositories.TransactionalCityRepository;
import com.group3.sem3exam.data.repositories.TransactionalCountryRepository;
import com.group3.sem3exam.rest.exceptions.CityNotFoundException;
import com.group3.sem3exam.rest.exceptions.CountryNotFoundException;

import javax.persistence.EntityManagerFactory;

public class CountryFacade
{

    private EntityManagerFactory emf;

    public CountryFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }


    public Country get(Integer id) throws CountryNotFoundException
    {
        try (TransactionalCountryRepository tcr = new TransactionalCountryRepository(emf)) {
            Country country = tcr.get(id);
            if (country.equals(null)) {
                throw new CountryNotFoundException(id);
            }
            return country;
        }

    }
    }

