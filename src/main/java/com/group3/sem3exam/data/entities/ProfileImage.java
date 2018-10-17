package com.group3.sem3exam.data.entities;

import javax.persistence.*;

import java.sql.Blob;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;


@Entity(name="profileimage")
public class ProfileImage
{


    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String fileName;

    //@Lob annotationen betyder, at vi gemmer filen som binær data i databasen
    @Lob
    //denne skal måske laves om til en Base64 encoding, pga JAX-RS
    private byte[] image;

    //ved ikke lige om den skal være eager eller lazy
    @ManyToOne(optional = false) // mangler lige nogle ting i denne annotation
    private User user;

    public ProfileImage(String fileName, byte[] image)
    {
        this.fileName = fileName;
        this.image = image;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public byte[] getImage()
    {
        return image;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
