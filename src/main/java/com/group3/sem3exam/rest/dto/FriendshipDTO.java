package com.group3.sem3exam.rest.dto;

import java.time.LocalDateTime;

public class FriendshipDTO
{

    private LocalDateTime since;

    public FriendshipDTO(LocalDateTime since)
    {
        this.since = since;
    }

    public FriendshipDTO()
    {

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
