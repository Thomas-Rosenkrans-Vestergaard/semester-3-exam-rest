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
    private final Supplier<T>                       transactionFactory;
    private final Function<T, FriendshipRepository> friendshipRepositoryFactory;

    public FriendshipFacade(Supplier<T> transactionFactory, Function<T, FriendshipRepository> friendshipRepositoryFactory)
    {
        this.transactionFactory = transactionFactory;
        this.friendshipRepositoryFactory = friendshipRepositoryFactory;
    }

    public List<User> getUserFriends(Integer userId)
    {
        try (T transaction = transactionFactory.get()) {
            transaction.begin();
            FriendshipRepository ur = friendshipRepositoryFactory.apply(transaction);
            return ur.getUserFriends(userId);
        }
    }

    public Friendship createFriendship(int friendshipRequestId) throws ResourceNotFoundException
    {
        try (FriendshipRepository friendshipRepository = friendshipRepositoryFactory.apply(transactionFactory.get())) {
            FriendRequest friendRequest = friendshipRepository.getFriendRequest(friendshipRequestId);
            if(friendRequest == null)
                throw new ResourceNotFoundException(FriendRequest.class, friendshipRequestId);

            Friendship    friendship    = friendshipRepository.createFriendship(friendRequest);
            return friendship;
        }
    }
}
