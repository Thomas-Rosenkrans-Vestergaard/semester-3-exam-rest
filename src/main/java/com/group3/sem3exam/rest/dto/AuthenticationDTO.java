package com.group3.sem3exam.rest.dto;

public class AuthenticationDTO
{

    public final String token;

    public AuthenticationDTO(String token)
    {
        this.token = token;
    }

    public static AuthenticationDTO basic(String token)
    {
        return new AuthenticationDTO(token);
    }
}
