package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Friendship;

import java.time.LocalDateTime;

public class FriendshipDTO
{

    private LocalDateTime since;


    public FriendshipDTO(Friendship friendship)
    {
        this.since = friendship.getSince();
    }

    public LocalDateTime getSince()
    {
        return since;
    }

    public void setSince(LocalDateTime since)
    {
        this.since = since;
    }
}
