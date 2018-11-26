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

    /**
     * Returns a paginated view of the friends of the provided user.
     *
     * @param user       The user to search the friends of.
     * @param pageSize   The number of results on a single page. {@code pageSize >= 0}
     * @param pageNumber The page number to return. {@code pageNumber > 0}.
     * @param search     Optional parameter, search in the names.
     * @return The paginated view of the friends of the provided user.
     */
    List<User> getFriends(User user, Integer pageSize, Integer pageNumber, String search);

    Friendship createFriendship(FriendRequest friendRequest);

    FriendRequest createFriendRequest(User requester, User reciever);

    FriendRequest getFriendRequest(int id);
}
