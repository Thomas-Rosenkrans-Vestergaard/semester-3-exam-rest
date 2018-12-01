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

    /**
     * Creates a new friendship from the provided friend request. The provided friend request is also marked
     * {@link FriendRequest.Status#ACCEPTED}.
     *
     * @param request The friend request from which to create the friendship, this entity is then marked accepted.
     * @return The friendship created from the request.
     */
    Friendship createFriendship(FriendRequest request);

    /**
     * Creates a new friend request from the provided {@code requester} to the provided {@code receiver}.
     *
     * @param requester The user requesting the friendship.
     * @param receiver  The user being requested friendship.
     * @return The newly created friend request.
     */
    FriendRequest createRequest(User requester, User receiver);

    /**
     * Marks the provided friend request {@link FriendRequest.Status#REJECTED}.
     *
     * @param fetchedRequest The friend request to mark as rejected.
     * @return The updated entity.
     */
    FriendRequest reject(FriendRequest fetchedRequest);

    Friendship getFriendship(User self, User other);

    /**
     * Returns the friend request with the provided id.
     *
     * @param id The id of the friend request to return.
     * @return The friend request with the provided id, {@code null} when no such record exists.
     */
    FriendRequest getRequest(Integer id);

    /**
     * Returns a PENDING friend request between the provided {@code user} and {@code other}.
     *
     * @param user  The sender.
     * @param other The receiver.
     * @return the friend request between the provided {@code user} and {@code other}, {@code null} when
     * no such record exists.
     */
    FriendRequest getRequest(User user, User other);

    /**
     * Returns the pending friend requests received by the provided {@code user}.
     *
     * @param user The user to return the received friend requests of.
     * @return The friend requests received from the provided {@code user}.
     */
    List<FriendRequest> getReceivedPendingRequests(User user);
}
