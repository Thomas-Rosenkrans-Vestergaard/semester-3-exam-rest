package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.FriendRequest;
import com.group3.sem3exam.data.entities.Friendship;

import javax.ejb.Local;
import java.time.LocalDateTime;

public class FriendshipDTO
{

    private LocalDateTime since;
    private UserDTO requester, reciever;

    private  FriendshipDTO(UserDTO requester, UserDTO reciever, LocalDateTime since)
    {
        this.since = since;
        this.requester = requester;
        this.reciever = reciever;
    }

    public static FriendshipDTO basicFriendshipDTO(Friendship friendship)
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

    public UserDTO getReciever()
    {
        return this.reciever;
    }

    public void setReciever(UserDTO reciever)
    {
        this.reciever = reciever;
    }
}
