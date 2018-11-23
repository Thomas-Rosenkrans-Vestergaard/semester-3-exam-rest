package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.data.entities.*;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JpaFriendRequestRepositoryTest
{
    @Test
    void createFriendship()
    {
        //using transaction because two repositories are used
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            JpaFriendshipRepository friendshipRepository = new JpaFriendshipRepository(transaction);
            JpaUserRepository       jpaUserRepository    = new JpaUserRepository(transaction);


            friendshipRepository.begin();
            jpaUserRepository.begin();
            City city = new JpaCityRepository(friendshipRepository.getEntityManager()).get(1);

            User          requester      = jpaUserRepository.createUser("user-owner", "owner@email.com", "pass", city, Gender.FEMALE, LocalDate.now());
            User          reciever1      = jpaUserRepository.createUser("user-friend", "friend@email.com", "pass", city, Gender.FEMALE, LocalDate.now());
            User          reciever2      = jpaUserRepository.createUser("user3", "user3@email.com", "pass", city, Gender.MALE, LocalDate.now());
            FriendRequest friendRequest  = new FriendRequest(requester, reciever1);
            FriendRequest friendRequest2 = new FriendRequest(requester, reciever2);

            Friendship fr               = friendshipRepository.createFriendship(friendRequest);
            Friendship fr2              = friendshipRepository.createFriendship(friendRequest2);
            List<User> ownerFriendships = friendshipRepository.getFriends(requester.getId());

            assertNotNull(requester.getId());
            assertNotNull(fr);
            assertEquals(reciever1, ownerFriendships.get(0));
            assertEquals(reciever2, ownerFriendships.get(1));

        }
    }
}
