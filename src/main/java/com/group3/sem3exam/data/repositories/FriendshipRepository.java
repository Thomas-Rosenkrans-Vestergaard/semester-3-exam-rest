package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.FriendRequest;
import com.group3.sem3exam.data.entities.Friendship;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.TransactionalRepository;

import java.util.List;

public interface FriendshipRepository extends TransactionalRepository
{

    /**
     * Returns the friends of the user with the provided id.
     *
     * @param userId The id of the user to return the friends of.
     * @return The friends of the user with the provided id.
     */
    List<User> getFriends(Integer userId);

    Friendship createFriendship(FriendRequest friendRequest);

    FriendRequest createFriendRequest(User requester, User reciever);

    FriendRequest getFriendRequest(int id);
}
