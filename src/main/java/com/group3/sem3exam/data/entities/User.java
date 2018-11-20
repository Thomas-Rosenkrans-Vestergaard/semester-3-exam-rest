package com.group3.sem3exam.data.entities;

import com.group3.sem3exam.data.repositories.RepositoryEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user_")
public class User implements RepositoryEntity<Integer>
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(fetch = LAZY, cascade = MERGE, mappedBy = "author", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "pk.owner")
    private List<Friendship> friendships = new ArrayList<>();

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "user")
    private List<Image> images = new ArrayList<>();

    @OneToOne(fetch = EAGER, optional = true)
    private Image profilePicture;

    public User(String name, String email, String passwordHash, City city, Gender gender, LocalDate dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.city = city;
        this.gender = gender;
    }

    public User()
    {

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

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Image> getImages()
    {
        return this.images;
    }

    public void setImages(List<Image> images)
    {
        this.images = images;
    }

    public void addImage(Image image)
    {
        this.images.add(image);
    }

    public Image getProfilePicture()
    {
        return this.profilePicture;
    }

    public void setProfilePicture(Image profilePicture)
    {
        this.profilePicture = profilePicture;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
               Objects.equals(getName(), user.getName()) &&
               Objects.equals(getEmail(), user.getEmail()) &&
               Objects.equals(getPasswordHash(), user.getPasswordHash()) &&
               Objects.equals(getCity(), user.getCity()) &&
               getGender() == user.getGender() &&
               Objects.equals(getDateOfBirth(), user.getDateOfBirth()) &&
               Objects.equals(getCreatedAt(), user.getCreatedAt());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId(), getName(), getEmail(), getPasswordHash(), getCity(), getGender(), getDateOfBirth(), getCreatedAt());
    }
}
