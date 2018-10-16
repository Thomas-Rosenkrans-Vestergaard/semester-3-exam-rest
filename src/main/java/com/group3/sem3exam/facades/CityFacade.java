package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.repositories.CityRepository;

import javax.persistence.EntityManagerFactory;

public class CityFacade
{

    private EntityManagerFactory emf;


    public CityFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    private CityRepository cityRepository;


    public City get(Integer id){
       return cityRepository.get(id);
    }


}
