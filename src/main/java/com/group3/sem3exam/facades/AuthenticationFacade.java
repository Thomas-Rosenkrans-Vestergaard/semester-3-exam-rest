package com.group3.sem3exam.facades;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.rest.authentication.jwt.FileJwtSecret;
import com.group3.sem3exam.rest.authentication.jwt.JwtTokenGenerator;

import javax.persistence.EntityManagerFactory;
import java.io.File;

public class AuthenticationFacade
{

    private EntityManagerFactory emf;

    public AuthenticationFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    public String generateToken(User user) throws Exception
    {
        File              file      = new File("test.txt");
        FileJwtSecret     jwtSecret = new FileJwtSecret(file, 256 / 8);
        JwtTokenGenerator generator = new JwtTokenGenerator(jwtSecret);
        return generator.generate(user);
    }
}
