package com.group3.sem3exam.data.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friendship")
@AssociationOverrides({
                              @AssociationOverride(name = "pk.owner", joinColumns = @JoinColumn(name = "owner")),
                              @AssociationOverride(name = "pk.friend", joinColumns = @JoinColumn(name = "friend"))
                      })
public class Friendship
{
    @EmbeddedId
    private FriendshipPK pk;

    @Column
    @CreationTimestamp
    private LocalDateTime since;

    public Friendship()
    {
        this.pk = new FriendshipPK();
    }

    public Friendship(User owner, User friend)
    {
        this.pk = new FriendshipPK(owner, friend);
    }

    public FriendshipPK getPk()
    {
        return this.pk;
    }

    public void setPk(FriendshipPK pk)
    {
        this.pk = pk;
    }

    @Transient
    public User getOwner()
    {
        return getPk().getOwner();
    }

    public void setOwner(User owner)
    {
        getPk().setOwner(owner);
    }

    @Transient
    public User getFriend()
    {
        return getPk().getFriend();
    }

    public void setFiend(User friend)
    {
        getPk().setTwo(friend);
    }

    public LocalDateTime getSince()
    {
        return this.since;
    }

    public void setSince(LocalDateTime since)
    {
        this.since = since;
    }
}
