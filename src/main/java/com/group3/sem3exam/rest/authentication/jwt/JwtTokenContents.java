package com.group3.sem3exam.rest.authentication.jwt;

public class JwtTokenContents
{

    private final int userId;

    public JwtTokenContents(int userId)
    {
        this.userId = userId;
    }

    public int getUserId()
    {
        return this.userId;
    }
}
