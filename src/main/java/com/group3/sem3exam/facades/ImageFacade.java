package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.Image;
import com.group3.sem3exam.data.repositories.TransactionalImageRepository;
import com.group3.sem3exam.rest.exceptions.ImageNotFoundException;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class ImageFacade
{

    private EntityManagerFactory emf;

    public ImageFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }


    public Image get(Integer id) throws ImageNotFoundException{

        try(TransactionalImageRepository tir = new TransactionalImageRepository(emf)){
            Image image = tir.get(id);
            if(image == null){
                throw new ImageNotFoundException(id);
            }
            return image;
        }
    }



    public List<Image> getByUser(Integer user) throws ImageNotFoundException{

        try(TransactionalImageRepository tir = new TransactionalImageRepository(emf)) {
            List<Image> images = tir.getByUser(user);
            if (images == null) {
                throw new ImageNotFoundException(user);
            }
            return images;
        }
    }
}