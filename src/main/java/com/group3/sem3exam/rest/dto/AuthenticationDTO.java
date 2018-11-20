package com.group3.sem3exam.rest.dto;

import com.group3.sem3exam.data.entities.User;

public class AuthenticationDTO
{

    public final String  token;
    public final UserDTO user;

    public AuthenticationDTO(String token, UserDTO userDTO)
    {
        this.token = token;
        this.user = userDTO;
    }

    public static AuthenticationDTO basic(String token, User user)
    {
        return new AuthenticationDTO(token, UserDTO.basic(user));
    }
}
