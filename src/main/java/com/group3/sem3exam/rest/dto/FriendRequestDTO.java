package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.FriendRequest;

public class FriendRequestDTO
{

    public final Integer              id;
    public final UserDTO              requester;
    public final UserDTO              receiver;
    public final FriendRequest.Status status;

    private FriendRequestDTO(Integer id, UserDTO requester, UserDTO receiver, FriendRequest.Status status)
    {
        this.id = id;
        this.requester = requester;
        this.receiver = receiver;
        this.status = status;
    }

    public static FriendRequestDTO complete(FriendRequest request)
    {
        return new FriendRequestDTO(
                request.getId(),
                UserDTO.publicView(request.getRequester()),
                UserDTO.publicView(request.getReceiver()),
                request.getStatus()
        );
    }
}
