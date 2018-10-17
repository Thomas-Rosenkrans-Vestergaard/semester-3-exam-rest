package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.Country;
import com.group3.sem3exam.data.entities.Region;
import com.group3.sem3exam.data.repositories.TransactionalCountryRepository;
import com.group3.sem3exam.data.repositories.TransactionalRegionRepository;
import com.group3.sem3exam.rest.exceptions.CountryNotFoundException;

import javax.persistence.EntityManagerFactory;

public class RegionFacade
{

    private EntityManagerFactory emf;

    public RegionFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }


    public Region get(Integer id) throws CountryNotFoundException
    {
        try (TransactionalRegionRepository trr = new TransactionalRegionRepository(emf)) {
            Region region = trr.get(id);
            if (region.equals(null)) {
                throw new CountryNotFoundException(id);
            }
            return region;
        }

    }
}
