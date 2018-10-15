package com.group3.sem3exam.data.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "user")
public class User
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(fetch = LAZY, cascade = MERGE, mappedBy = "author", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "pk.owner")
    private List<Friendship> friendships = new ArrayList<>();

    @OneToOne(optional = false)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    public User(String name, String email, String passwordHash, City city, Gender gender, LocalDate dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.city = city;
        this.gender = gender;
    }

    public User(String name, String email, String passwordHash)
    {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }


    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPasswordHash()
    {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public List<Post> getPosts()
    {
        return this.posts;
    }

    public void setPosts(List<Post> posts)
    {
        this.posts = posts;
        for (Post post : posts)
            post.setAuthor(this);
    }

    public void addPost(Post post)
    {
        this.posts.add(post);
        post.setAuthor(this);
    }

    public List<Friendship> getFriendships()
    {
        return this.friendships;
    }

    public void setFriendships(List<Friendship> friendships)
    {
        this.friendships = friendships;
    }

    public void addFriendship(Friendship friendship)
    {
        this.friendships.add(friendship);
        friendship.setOwner(this);
    }

    public City getCity()
    {
        return city;
    }

    public void setCity(City city)
    {
        this.city = city;
    }
}
