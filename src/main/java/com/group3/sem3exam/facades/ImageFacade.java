package com.group3.sem3exam.facades;

import javax.persistence.EntityManagerFactory;

public class ImageFacade
{

    private EntityManagerFactory emf;

    public ImageFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
}
