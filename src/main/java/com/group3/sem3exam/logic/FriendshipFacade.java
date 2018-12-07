package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.entities.FriendRequest;
import com.group3.sem3exam.data.entities.Friendship;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.FriendshipRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;
import com.group3.sem3exam.logic.authentication.AuthenticationContext;
import com.group3.sem3exam.logic.authorization.AuthorizationException;
import com.group3.sem3exam.logic.authorization.Authorizator;
import com.group3.sem3exam.logic.authorization.IsUser;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class FriendshipFacade<T extends Transaction>
{

    /**
     * The factory that produces the transactions of type {@code T} used by this facade.
     */
    private final Supplier<T> transactionFactory;

    /**
     * The factory that produces the {@link UserRepository} instances used by this facade.
     */
    private final Function<T, UserRepository> userRepositoryFactory;

    /**
     * The factory that produces the {@link FriendshipRepository} instances used by this facade.
     */
    private final Function<T, FriendshipRepository> friendshipRepositoryFactory;

    /**
     * Creates a new {@link FriendshipFacade}.
     *
     * @param transactionFactory          The factory that produces the transactions of type {@code T} used by this
     *                                    facade.
     * @param userRepositoryFactory       The factory that produces the {@link UserRepository} instances used by this
     *                                    facade.
     * @param friendshipRepositoryFactory The factory that produces the {@link FriendshipRepository} instances used by
     *                                    this facade.
     */
    public FriendshipFacade(
            Supplier<T> transactionFactory,
            Function<T, UserRepository> userRepositoryFactory,
            Function<T, FriendshipRepository> friendshipRepositoryFactory)
    {
        this.transactionFactory = transactionFactory;
        this.userRepositoryFactory = userRepositoryFactory;
        this.friendshipRepositoryFactory = friendshipRepositoryFactory;
    }

    /**
     * Returns the friends of the user with the provided id.
     *
     * @param user The id of the user to return the friends of.
     * @return The friends of the user with the provided id.
     * @throws ResourceNotFoundException When the user with the provided id does not exist.
     */
    public List<User> getFriends(Integer user) throws ResourceNotFoundException
    {
        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            UserRepository       ur = userRepositoryFactory.apply(transaction);
            FriendshipRepository fr = friendshipRepositoryFactory.apply(transaction);
            if (!ur.exists(user))
                throw new ResourceNotFoundException(User.class, user);

            return fr.getFriends(user);
        }
    }

    /**
     * Returns a paginated view of the friends of the user with the provided id.
     *
     * @param userId     The id of the user to search the friends of.
     * @param pageSize   The number of results on a single page. {@code pageSize >= 0}
     * @param pageNumber The page number to return. {@code pageNumber > 0}.
     * @param search     Optional parameter, search in the names.
     * @return The paginated view of the friends of the user with the provided id.
     * @throws ResourceNotFoundException When the user with the provided id does not exist.
     */
    public List<User> searchFriends(Integer userId, Integer pageSize, Integer pageNumber, String search)
    throws ResourceNotFoundException
    {
        pageSize = Math.max(pageSize, 0);
        pageNumber = Math.max(pageNumber, 1);

        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            UserRepository       ur   = userRepositoryFactory.apply(transaction);
            FriendshipRepository fr   = friendshipRepositoryFactory.apply(transaction);
            User                 user = ur.get(userId);
            if (user == null)
                throw new ResourceNotFoundException(User.class, userId);
            return fr.getFriends(user, pageSize, pageNumber, search);
        }
    }

    /**
     * Rejects the friend-request with the provided id.
     *
     * @param auth    The user rejecting the friend request.
     * @param request The id of the friend request to reject.
     * @return The updates friend request entity.
     * @throws ResourceNotFoundException When the friend request with the provided id does not exist.
     * @throws AuthorizationException    When the authenticated user does not have permissions to reject the friend request.
     * @throws ResourceConflictException When the friend request with the provided id is not pending.
     */
    public FriendRequest reject(AuthenticationContext auth, Integer request)
    throws ResourceNotFoundException, AuthorizationException, ResourceConflictException
    {
        new Authorizator(auth).check(new IsUser());

        try (FriendshipRepository repository = friendshipRepositoryFactory.apply(transactionFactory.get())) {
            FriendRequest fetchedRequest = retrieveRequest(repository, auth, request);
            repository.begin();
            FriendRequest updated = repository.reject(fetchedRequest);
            repository.commit();
            return updated;
        }
    }

    /**
     * Accepts the friend-request with the provided id.
     *
     * @param auth    The user accepting the friend request.
     * @param request The id of the friend request to accept.
     * @return The friendship.
     * @throws ResourceNotFoundException When the friend request with the provided id does not exist.
     * @throws AuthorizationException    When the authenticated user does not have permissions to accept the friend request.
     * @throws ResourceConflictException When the friend request with the provided id is not pending.
     */
    public Friendship accept(AuthenticationContext auth, Integer request)
    throws ResourceNotFoundException, AuthorizationException, ResourceConflictException
    {
        new Authorizator(auth).check(new IsUser());

        try (FriendshipRepository repository = friendshipRepositoryFactory.apply(transactionFactory.get())) {
            repository.begin();
            FriendRequest fetchedRequest = retrieveRequest(repository, auth, request);
            Friendship created = repository.accept(fetchedRequest);
            repository.commit();
            return created;
        }
    }


    public Friendship unfriend(AuthenticationContext auth, User other) throws AuthorizationException, ResourceNotFoundException, ResourceConflictException
    {

        new Authorizator(auth).check(new IsUser());

        try(FriendshipRepository friendshipRepository = friendshipRepositoryFactory.apply(transactionFactory.get())){
            friendshipRepository.begin();
            Friendship friendship = friendshipRepository.deleteFriendship(auth.getUser(), other);
            if(friendship == null){
                throw new ResourceNotFoundException(Friendship.class, auth.getUserId() + "+" + other.getId());
            }
        }
        return null;
    }

    /**
     * Creates a new friend-request from the authenticated user to the user with the provided id.
     *
     * @param auth     The authenticated user making the friend request.
     * @param receiver The id of the user the friend-request is sent to.
     * @return The newly created friend request.
     * @throws ResourceNotFoundException When the user with the provided id does not exist.
     * @throws IllegalOperationException When the two users are already friends, and the request is operation is
     *                                   therefor not permitted.
     */
    public FriendRequest createRequest(AuthenticationContext auth, Integer receiver)
    throws ResourceNotFoundException, IllegalOperationException
    {
        try (T transaction = transactionFactory.get()) {
            FriendshipRepository friendshipRepository = friendshipRepositoryFactory.apply(transaction);
            UserRepository       userRepository       = userRepositoryFactory.apply(transaction);

            User fetchedReceiver = userRepository.get(receiver);
            if (fetchedReceiver == null)
                throw new ResourceNotFoundException(User.class, receiver);
            if (friendshipRepository.getFriendship(auth.getUser(), fetchedReceiver) != null)
                throw new IllegalOperationException("You are already friends with the receiver.");

            transaction.begin();
            FriendRequest created = friendshipRepository.createRequest(auth.getUser(), fetchedReceiver);
            transaction.commit();
            return created;
        }
    }

    /**
     * Returns the friendship between the users with the provided id's.
     *
     * @param self  The requester.
     * @param other The other user.
     * @return The friendship between the users.
     * @throws ResourceNotFoundException When the user with the provided id does not exist.
     * @throws ResourceNotFoundException When no friendship exists between the users with the provided id's.
     */
    public Friendship getFriendship(AuthenticationContext self, Integer other) throws ResourceNotFoundException
    {
        try (T transaction = transactionFactory.get()) {
            FriendshipRepository friendshipRepository = friendshipRepositoryFactory.apply(transaction);
            UserRepository       userRepository       = userRepositoryFactory.apply(transaction);

            User fetchedOther = userRepository.get(other);
            if (fetchedOther == null)
                throw new ResourceNotFoundException(User.class, other);

            Friendship friendship = friendshipRepository.getFriendship(self.getUser(), fetchedOther);
            if (friendship == null)
                throw new ResourceNotFoundException(Friendship.class, self.getUserId() + "+" + other);

            return friendship;
        }
    }

    /**
     * Returns the friend request sent by {@code self} to {@code other}.
     *
     * @param self  The sender.
     * @param other The receiver.
     * @return The friend request sent by {@code self} to {@code other}.
     * @throws ResourceNotFoundException When no user with the provided id {@code other} exists.
     * @throws ResourceNotFoundException When there exists no such friend request.
     * @throws AuthorizationException    When the provided authentication context is not a user.
     */
    public FriendRequest getRequest(AuthenticationContext self, Integer other)
    throws ResourceNotFoundException, AuthorizationException
    {
        new Authorizator(self).check(new IsUser());

        try (T transaction = transactionFactory.get()) {
            FriendshipRepository friendshipRepository = friendshipRepositoryFactory.apply(transaction);
            UserRepository       userRepository       = userRepositoryFactory.apply(transaction);

            User fetchedOther = userRepository.get(other);
            if (fetchedOther == null)
                throw new ResourceNotFoundException(User.class, other);

            FriendRequest request = friendshipRepository.getPendingRequest(self.getUser(), fetchedOther);
            if (request == null)
                throw new ResourceNotFoundException(FriendRequest.class, self.getUserId() + ", " + other);

            return request;
        }
    }

    /**
     * Returns all the friend requests sent to the user in the provided authentication context.
     *
     * @param auth The authentication context containing the user to return the received friend requests of.
     * @return All the friend requests sent to the user in the provided authentication context.
     * @throws AuthorizationException When the provided authentication context is not of type USER.
     */
    public List<FriendRequest> getReceivedRequests(AuthenticationContext auth) throws AuthorizationException
    {
        new Authorizator(auth).check(new IsUser());

        try (T transaction = transactionFactory.get()) {
            FriendshipRepository friendshipRepository = friendshipRepositoryFactory.apply(transaction);
            return friendshipRepository.getReceivedPendingRequests(auth.getUser());
        }
    }

    private FriendRequest retrieveRequest(FriendshipRepository repository, AuthenticationContext auth, Integer request)
    throws ResourceNotFoundException, AuthorizationException, ResourceConflictException
    {
        FriendRequest fetchedRequest = repository.getRequest(request);
        if (fetchedRequest == null)
            throw new ResourceNotFoundException(Friendship.class, request);
        if (fetchedRequest.getReceiver().getId() != auth.getUserId())
            throw new AuthorizationException("You do not own this friend request.");
        if (fetchedRequest.getStatus() != FriendRequest.Status.PENDING)
            throw new ResourceConflictException(FriendRequest.class, "The friend request has already been answered.");

        return fetchedRequest;
    }
}
