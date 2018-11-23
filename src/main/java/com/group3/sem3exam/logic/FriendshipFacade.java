package com.group3.sem3exam.logic;

import com.group3.sem3exam.data.entities.FriendRequest;
import com.group3.sem3exam.data.entities.Friendship;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.FriendshipRepository;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.data.repositories.transactions.Transaction;

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

    public Friendship createFriendship(int friendshipRequestId) throws ResourceNotFoundException
    {
        try (FriendshipRepository friendshipRepository = friendshipRepositoryFactory.apply(transactionFactory.get())) {
            FriendRequest friendRequest = friendshipRepository.getFriendRequest(friendshipRequestId);
            if (friendRequest == null)
                throw new ResourceNotFoundException(FriendRequest.class, friendshipRequestId);

            Friendship friendship = friendshipRepository.createFriendship(friendRequest);
            return friendship;
        }
    }
}
