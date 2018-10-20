package com.group3.sem3exam.data.entities;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(
        name = "privilege_mapping",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "thirdParty_id",
                        "user_id",
                        "privilege"
                }))
public class PrivilegeMapping
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private ThirdParty thirdParty;

    @Column(nullable = false)
    private Privilege privilege;

    public PrivilegeMapping()
    {

    }

    public PrivilegeMapping(User user, ThirdParty thirdParty, Privilege privilege)
    {
        this.user = user;
        this.thirdParty = thirdParty;
        this.privilege = privilege;
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public ThirdParty getThirdParty()
    {
        return this.thirdParty;
    }

    public void setThirdParty(ThirdParty thirdParty)
    {
        this.thirdParty = thirdParty;
    }

    public Privilege getPrivilege()
    {
        return this.privilege;
    }

    public void setPrivilege(Privilege privilege)
    {
        this.privilege = privilege;
    }
}
