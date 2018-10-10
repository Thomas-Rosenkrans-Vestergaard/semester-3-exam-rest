package com.group3.sem3exam.rest;

import com.group3.sem3exam.data.Friendship;
import com.group3.sem3exam.data.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("users")
public class UserResource
{

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("rest-api-pu");

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() throws Exception
    {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        User thomas = new User("Thomas", "tvestergaard@hotmail.com", "hash");
        User kasper = new User("Kasper", "kvestergaard@hotmail.com", "hash");
        User sanne  = new User("Sanne", "svestergaard@hotmail.com", "hash");

        StringBuilder stringBuilder = new StringBuilder();
        try {

            entityManager.persist(thomas);
            entityManager.persist(kasper);
            entityManager.persist(sanne);

            {
                Friendship friendship = new Friendship(thomas, kasper);
                entityManager.persist(friendship);
                thomas.addFriendship(friendship);
            }

            {
                Friendship friendship = new Friendship(thomas, sanne);
                entityManager.persist(friendship);
                thomas.addFriendship(friendship);
            }

            {
                Friendship friendship = new Friendship(sanne, thomas);
                entityManager.persist(friendship);
                sanne.addFriendship(friendship);
            }

            {

                Friendship friendship = new Friendship(kasper, thomas);
                entityManager.persist(friendship);
                kasper.addFriendship(friendship);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e){
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();

        }

        return stringBuilder.toString();
    }
}
