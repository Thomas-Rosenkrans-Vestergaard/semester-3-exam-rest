package com.group3.sem3exam.rest.authentication;

import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.rest.authentication.jwt.FileJwtSecret;
import com.group3.sem3exam.rest.authentication.jwt.JwtTokenUnpacker;
import com.group3.sem3exam.rest.authentication.jwt.JwtUnpackingException;

import java.io.File;

public class Authenticator
{

    private static FileJwtSecret secret = generateSecret();

    public static JwtTokenUnpacker jwt = new JwtTokenUnpacker(secret);// tjek lige for hvilken constructor den skal have

    public String getToken (String token) throws JwtUnpackingException
    {


    }


    public static FileJwtSecret generateSecret(){
        try {
            return new FileJwtSecret(new File("jwt.secret"), 256/8)
        } catch (Exception e) {
            e.printStackTrace(); // find bedre exception her.
        }
        return null;
    }


}
