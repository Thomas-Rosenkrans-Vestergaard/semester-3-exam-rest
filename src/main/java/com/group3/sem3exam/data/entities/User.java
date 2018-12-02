package com.group3.sem3exam.data.entities;

import com.group3.sem3exam.data.repositories.base.RepositoryEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
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

    @OneToOne(fetch = EAGER, cascade = ALL, mappedBy = "user")
    private ProfilePicture profilePicture;

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

    public ProfilePicture getProfilePicture()
    {
        return this.profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture)
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
