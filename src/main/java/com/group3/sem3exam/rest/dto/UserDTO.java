package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserDTO
{
    private final Integer       id;
    private final String        name;
    private final String        email;
    private final CityDTO       city;
    private final Gender        gender;
    private final LocalDate     dateOfBirth;
    private final LocalDateTime createdAt;
    private final ImageDTO      profilePicture;

    private UserDTO(Integer id, String name, String email, CityDTO city, Gender gender, LocalDate dateOfBirth, LocalDateTime createdAt, ImageDTO profilePicture)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.city = city;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
        this.profilePicture = profilePicture;
    }

    public static UserDTO complete(User user)
    {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                CityDTO.basic(user.getCity()),
                user.getGender(),
                user.getDateOfBirth(),
                user.getCreatedAt(),
                ImageDTO.withoutUser(user.getProfilePicture())
        );
    }

    public static UserDTO publicView(User user)
    {
        return new UserDTO(
                user.getId(),
                user.getName(),
                null,
                null,
                user.getGender(),
                null,
                user.getCreatedAt(),
                ImageDTO.withoutUser(user.getProfilePicture())
        );
    }
}
