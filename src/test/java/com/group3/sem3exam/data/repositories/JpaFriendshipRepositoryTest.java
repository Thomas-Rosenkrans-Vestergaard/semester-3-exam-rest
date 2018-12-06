package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.TestingUtils;
import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.FriendRequest;
import com.group3.sem3exam.data.entities.Friendship;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.JpaTransaction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.group3.sem3exam.data.entities.FriendRequest.Status.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaFriendshipRepositoryTest
{

    @Test
    void acceptFriendship()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaFriendshipRepository friendshipRepository = new JpaFriendshipRepository(transaction);
            JpaUserRepository       userRepository       = new JpaUserRepository(transaction);
            City                    city                 = new JpaCityRepository(friendshipRepository.getEntityManager()).get(1);

            User requester = TestingUtils.randomUser(userRepository, city);
            User receiver  = TestingUtils.randomUser(userRepository, city);

            FriendRequest request = friendshipRepository.createRequest(requester, receiver);
            assertEquals(PENDING, request.getStatus());
            Friendship friendship = friendshipRepository.accept(request);
            assertEquals(ACCEPTED, request.getStatus());

            assertEquals(friendship.getOwner(), requester);
            assertEquals(friendship.getFriend(), receiver);
        }
    }

    @Test
    void rejectFriendship()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaFriendshipRepository friendshipRepository = new JpaFriendshipRepository(transaction);
            JpaUserRepository       userRepository       = new JpaUserRepository(transaction);
            City                    city                 = new JpaCityRepository(friendshipRepository.getEntityManager()).get(1);

            User requester = TestingUtils.randomUser(userRepository, city);
            User receiver  = TestingUtils.randomUser(userRepository, city);

            FriendRequest request = friendshipRepository.createRequest(requester, receiver);
            assertEquals(PENDING, request.getStatus());
            request = friendshipRepository.reject(request);
            assertEquals(REJECTED, request.getStatus());
        }
    }

    @Test
    void getFriends()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaFriendshipRepository friendshipRepository = new JpaFriendshipRepository(transaction);
            JpaUserRepository       userRepository       = new JpaUserRepository(transaction);
            City                    city                 = new JpaCityRepository(friendshipRepository.getEntityManager()).get(1);

            User requester = TestingUtils.randomUser(userRepository, city);
            User receiver  = TestingUtils.randomUser(userRepository, city);

            assertEquals(0, friendshipRepository.getFriends(requester.getId()).size());
            assertEquals(0, friendshipRepository.getFriends(receiver.getId()).size());

            friendshipRepository.accept(friendshipRepository.createRequest(requester, receiver));

            assertEquals(1, friendshipRepository.getFriends(requester.getId()).size());
            assertEquals(1, friendshipRepository.getFriends(receiver.getId()).size());
            assertEquals(receiver, friendshipRepository.getFriends(requester.getId()).get(0));
            assertEquals(requester, friendshipRepository.getFriends(receiver.getId()).get(0));
        }
    }

    @Test
    void getFriendsPaginated()
    {

    }

    @Test
    void getFriendship()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaFriendshipRepository friendshipRepository = new JpaFriendshipRepository(transaction);
            JpaUserRepository       userRepository       = new JpaUserRepository(transaction);
            City                    city                 = new JpaCityRepository(friendshipRepository.getEntityManager()).get(1);

            User requester = TestingUtils.randomUser(userRepository, city);
            User receiver  = TestingUtils.randomUser(userRepository, city);

            assertNull(friendshipRepository.getFriendship(requester, receiver));
            assertNull(friendshipRepository.getFriendship(receiver, requester));

            friendshipRepository.accept(friendshipRepository.createRequest(requester, receiver));

            assertNotNull(friendshipRepository.getFriendship(requester, receiver));
            assertNotNull(friendshipRepository.getFriendship(receiver, requester));
        }
    }

    @Test
    void getRequest()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaFriendshipRepository friendshipRepository = new JpaFriendshipRepository(transaction);
            JpaUserRepository       userRepository       = new JpaUserRepository(transaction);
            City                    city                 = new JpaCityRepository(friendshipRepository.getEntityManager()).get(1);

            User          requester = TestingUtils.randomUser(userRepository, city);
            User          receiver  = TestingUtils.randomUser(userRepository, city);
            FriendRequest request   = friendshipRepository.createRequest(requester, receiver);
            assertEquals(request, friendshipRepository.getRequest(request.getId()));
            assertNull(friendshipRepository.getRequest(-1));
        }
    }

    @Test
    void getPendingRequest()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaFriendshipRepository friendshipRepository = new JpaFriendshipRepository(transaction);
            JpaUserRepository       userRepository       = new JpaUserRepository(transaction);
            City                    city                 = new JpaCityRepository(friendshipRepository.getEntityManager()).get(1);

            User requester = TestingUtils.randomUser(userRepository, city);
            User receiver  = TestingUtils.randomUser(userRepository, city);
            assertNull(friendshipRepository.getPendingRequest(requester, receiver));
            FriendRequest request = friendshipRepository.createRequest(requester, receiver);
            assertEquals(request, friendshipRepository.getPendingRequest(requester, receiver));
            friendshipRepository.reject(request);
            assertNull(friendshipRepository.getPendingRequest(requester, receiver));
        }
    }

    @Test
    void getReceivedPendingRequests()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaFriendshipRepository friendshipRepository = new JpaFriendshipRepository(transaction);
            JpaUserRepository       userRepository       = new JpaUserRepository(transaction);
            City                    city                 = new JpaCityRepository(friendshipRepository.getEntityManager()).get(1);

            User requester = TestingUtils.randomUser(userRepository, city);
            User receiver  = TestingUtils.randomUser(userRepository, city);
            assertEquals(0, friendshipRepository.getReceivedPendingRequests(receiver).size());
            FriendRequest       request  = friendshipRepository.createRequest(requester, receiver);
            List<FriendRequest> requests = friendshipRepository.getReceivedPendingRequests(receiver);
            assertEquals(1, requests.size());
            assertEquals(request, requests.get(0));
            friendshipRepository.reject(request);
            assertEquals(0, friendshipRepository.getReceivedPendingRequests(receiver).size());
        }
    }

    @Test
    void deleteFriendship()
    {
        try (JpaTransaction transaction = new JpaTransaction(JpaTestConnection.create())) {
            transaction.begin();
            JpaFriendshipRepository friendshipRepository = new JpaFriendshipRepository(transaction);
            JpaUserRepository       userRepository       = new JpaUserRepository(transaction);
            City                    city                 = new JpaCityRepository(friendshipRepository.getEntityManager()).get(1);

            User requester = TestingUtils.randomUser(userRepository, city);
            User receiver  = TestingUtils.randomUser(userRepository, city);

            FriendRequest request = friendshipRepository.createRequest(requester, receiver);
            assertNull(friendshipRepository.getFriendship(requester, receiver));
            friendshipRepository.accept(request);
            assertNotNull(friendshipRepository.getFriendship(requester, receiver));
            friendshipRepository.deleteFriendship(requester, receiver);
            assertNull(friendshipRepository.getFriendship(requester, receiver));
        }
    }
}