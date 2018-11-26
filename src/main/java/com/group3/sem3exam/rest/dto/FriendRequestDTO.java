package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.FriendRequest;

public class FriendRequestDTO
{
    private UserDTO requester, reciever;
    private FriendRequest.FRIENDSHIP_STATUS status;

    private FriendRequestDTO(FriendRequest friendRequest, FriendRequest.FRIENDSHIP_STATUS status) {

        this.requester = UserDTO.basic( friendRequest.getRequester() );
        this.reciever = UserDTO.basic( friendRequest.getReceiver() );
        this.status = status;
    }

    public static FriendRequestDTO basicFriendRequest(FriendRequest friendRequest, FriendRequest.FRIENDSHIP_STATUS status)
    {
        return new FriendRequestDTO( friendRequest, status );
    }

    public UserDTO getRequester()
    {
        return this.requester;
    }

    public UserDTO getReciever()
    {
        return this.reciever;
    }

    public FriendRequest.FRIENDSHIP_STATUS getStatus()
    {
        return this.status;
    }
}
