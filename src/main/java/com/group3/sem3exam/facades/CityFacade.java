package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.repositories.CityRepository;
import com.group3.sem3exam.data.repositories.TransactionalCityRepository;
import com.group3.sem3exam.rest.exceptions.CityNotFoundException;

import javax.persistence.EntityManagerFactory;

public class CityFacade
{

    private EntityManagerFactory emf;


    public CityFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }


    public City get(Integer id) throws CityNotFoundException
    {
        try (TransactionalCityRepository tcr = new TransactionalCityRepository(emf)) {
            City city = tcr.get(id);
            if (city.equals(null)) {
                throw new CityNotFoundException(id);
            }
            return city;
        }

    }
}
