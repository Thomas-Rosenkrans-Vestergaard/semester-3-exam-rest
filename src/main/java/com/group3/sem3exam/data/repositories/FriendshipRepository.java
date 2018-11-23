package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.entities.FriendRequest;
import com.group3.sem3exam.data.entities.Friendship;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.transactions.TransactionalRepository;

import  java.util.List;

public interface FriendshipRepository extends TransactionalRepository
{
    List<User> getUserFriends(Integer userId);
    Friendship createFriendship(FriendRequest friendRequest);
    FriendRequest createFriendRequest(User requester, User reciever);
    FriendRequest getFriendRequest(int id);
}
