package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.FriendRequest;
import com.group3.sem3exam.data.entities.Friendship;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class JpaFriendshipRepository extends AbstractJpaRepository implements FriendshipRepository
{
    public JpaFriendshipRepository(EntityManager entityManager)
    {
        super(entityManager);
    }

    public JpaFriendshipRepository(JpaTransaction transaction)
    {
        super(transaction);
    }

    public JpaFriendshipRepository(EntityManagerFactory emf)
    {
        super(emf);
    }

    @Override
    public List<User> getFriends(Integer userId)
    {
        return getEntityManager()
                .createQuery("SELECT f.pk.friend FROM Friendship f WHERE f.pk.owner.id = :id", User.class)
                .setParameter("id", userId)
                .getResultList();
    }

    @Override
    public Friendship createFriendship(FriendRequest friendRequest)
    {
        Friendship friendship = new Friendship(friendRequest.getRequester(), friendRequest.getReceiver());
        getEntityManager().persist(friendship);
        return friendship;
    }

    @Override
    public FriendRequest createFriendRequest(User requester, User reciever)
    {
        FriendRequest friendRequest = new FriendRequest(requester, reciever);
        getEntityManager().persist(friendRequest);
        return friendRequest;
    }

    @Override
    public FriendRequest getFriendRequest(int id)
    {
        return getEntityManager().find(FriendRequest.class, id);
    }


}
