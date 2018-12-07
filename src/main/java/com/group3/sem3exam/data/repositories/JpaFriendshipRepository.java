package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.FriendRequest;
import com.group3.sem3exam.data.entities.Friendship;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.base.AbstractJpaRepository;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
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
    public List<User> getFriends(User user, Integer pageSize, Integer pageNumber, String search)
    {
        return getEntityManager()
                .createQuery("SELECT f.pk.friend FROM Friendship f " +
                             "WHERE f.pk.owner = :user AND f.pk.friend.name LIKE :search", User.class)
                .setParameter("user", user)
                .setParameter("search", '%' + search + '%')
                .setMaxResults(pageSize)
                .setFirstResult(pageSize * (pageNumber - 1))
                .getResultList();
    }


    @Override
    public Friendship accept(FriendRequest request)
    {
        EntityManager entityManager = getEntityManager();
        request.setStatus(FriendRequest.Status.ACCEPTED);
        entityManager.merge(entityManager.contains(request) ? request : entityManager.merge(request));
        Friendship friendship = new Friendship(request.getRequester(), request.getReceiver());
        entityManager.persist(friendship);
        entityManager.persist(new Friendship(request.getReceiver(), request.getRequester()));
        return friendship;
    }

    @Override
    public FriendRequest createRequest(User requester, User receiver)
    {
        FriendRequest friendRequest = new FriendRequest(requester, receiver);
        getEntityManager().persist(friendRequest);
        return friendRequest;
    }

    @Override
    public FriendRequest reject(FriendRequest request)
    {
        EntityManager entityManager = this.getEntityManager();
        request.setStatus(FriendRequest.Status.REJECTED);
        return entityManager.merge(entityManager.contains(request) ? request : entityManager.merge(request));
    }

    @Override
    public Friendship getFriendship(User self, User other)
    {
        try {
            return getEntityManager()
                    .createQuery("SELECT f FROM Friendship f WHERE f.pk.owner = :self AND f.pk.friend = :other", Friendship.class)
                    .setParameter("self", self)
                    .setParameter("other", other)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public FriendRequest getRequest(Integer id)
    {
        return getEntityManager().find(FriendRequest.class, id);
    }

    @Override
    public FriendRequest getPendingRequest(User requester, User receiver)
    {
        try {
            return getEntityManager()
                    .createQuery("SELECT fr FROM FriendRequest fr " +
                                 "WHERE fr.requester = :requester AND fr.receiver = :receiver AND fr.status = :status",
                                 FriendRequest.class)
                    .setParameter("requester", requester)
                    .setParameter("receiver", receiver)
                    .setParameter("status", FriendRequest.Status.PENDING)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<FriendRequest> getReceivedPendingRequests(User user)
    {
        return getEntityManager()
                .createQuery("SELECT fr FROM FriendRequest fr " +
                             "WHERE fr.receiver = :user AND fr.status = :status", FriendRequest.class)
                .setParameter("user", user)
                .setParameter("status", FriendRequest.Status.PENDING)
                .getResultList();
    }

    @Override
    public Friendship deleteFriendship(User user, User other)
    {
        Friendship friendship1 = getFriendship(user, other);
        Friendship friendship2 = getFriendship(other, user);

        if (friendship2 != null && friendship1 != null) {
            getEntityManager().remove(friendship1);
            getEntityManager().remove(friendship2);
            return friendship1;
        }
        return null;
    }
}
