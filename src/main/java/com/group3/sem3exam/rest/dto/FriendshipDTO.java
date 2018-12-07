package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Friendship;

import java.time.LocalDateTime;

public class FriendshipDTO
{

    private LocalDateTime since;
    private UserDTO       requester;
    private UserDTO       receiver;

    private FriendshipDTO(UserDTO requester, UserDTO receiver, LocalDateTime since)
    {
        this.since = since;
        this.requester = requester;
        this.receiver = receiver;
    }

    public static FriendshipDTO complete(Friendship friendship)
    {
        return new FriendshipDTO(UserDTO.publicView(friendship.getOwner()), UserDTO.publicView(friendship.getFriend()), friendship.getSince());
    }

    public LocalDateTime getSince()
    {
        return since;
    }

    public void setSince(LocalDateTime since)
    {
        this.since = since;
    }

    public UserDTO getRequester()
    {
        return this.requester;
    }

    public void setRequester(UserDTO requester)
    {
        this.requester = requester;
    }

    public UserDTO getReceiver()
    {
        return this.receiver;
    }

    public void setReceiver(UserDTO receiver)
    {
        this.receiver = receiver;
    }
}
