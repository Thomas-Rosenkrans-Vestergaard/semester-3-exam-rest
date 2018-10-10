package com.group3.sem3exam.data;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FriendshipPK implements Serializable
{

    @ManyToOne(optional = false)
    private User owner;

    @ManyToOne(optional = false)
    private User friend;

    public FriendshipPK()
    {

    }

    public FriendshipPK(User owner, User friend)
    {
        this.owner = owner;
        this.friend = friend;
    }

    public User getOwner()
    {
        return this.owner;
    }

    public void setOwner(User userOne)
    {
        this.owner = userOne;
    }

    public User getFriend()
    {
        return this.friend;
    }

    public void setTwo(User userTwo)
    {
        this.friend = userTwo;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof FriendshipPK)) return false;
        FriendshipPK that = (FriendshipPK) o;
        return Objects.equals(getOwner(), that.getOwner()) &&
               Objects.equals(getFriend(), that.getFriend());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getOwner(), getFriend());
    }
}
