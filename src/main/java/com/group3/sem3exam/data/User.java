package com.group3.sem3exam.data;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "user")
public class User
{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(nullable = false)
    private Calendar createdAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = MERGE, mappedBy = "author", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = MERGE)
    private List<User> friends = new ArrayList<>();

    public User()
    {

    }

    public User(Integer id, String name, String email, String passwordHash)
    {
        this.id = id;
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

    public Calendar getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(Calendar createdAt)
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

    public List<User> getFriends()
    {
        return this.friends;
    }

    public void setFriends(List<User> friends)
    {
        this.friends = friends;
    }

    public void addFriend(User friend)
    {
        this.friends.add(friend);
    }
}
