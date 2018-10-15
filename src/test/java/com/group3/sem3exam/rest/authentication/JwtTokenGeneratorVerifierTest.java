package com.group3.sem3exam.rest.authentication;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.rest.authentication.jwt.BasicJwtSecret;
import com.group3.sem3exam.rest.authentication.jwt.JwtSecret;
import com.group3.sem3exam.rest.authentication.jwt.JwtTokenGenerator;
import com.group3.sem3exam.rest.authentication.jwt.JwtTokenVerifier;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class JwtTokenGeneratorVerifierTest
{

    @Test
    public void verifySuccess() throws Exception
    {
        JwtSecret         secret         = new BasicJwtSecret(256 / 8);
        JwtTokenGenerator tokenGenerator = new JwtTokenGenerator(secret);
        JwtTokenVerifier  tokenVerifier  = new JwtTokenVerifier(secret);

        int  id   = 43252;
        User user = new User();
        user.setId(id);

        String token = tokenGenerator.generate(user);
        assertEquals(id, tokenVerifier.unpack(token).getUserId());
    }

    @Test(expected = JWTVerificationException.class)
    public void verifyFailureSecret() throws Exception
    {
        JwtSecret         secretA        = new BasicJwtSecret("secretA");
        JwtSecret         secretB        = new BasicJwtSecret("secretB");
        JwtTokenGenerator tokenGenerator = new JwtTokenGenerator(secretA);
        JwtTokenVerifier  tokenVerifier  = new JwtTokenVerifier(secretB);

        int  id   = 43252;
        User user = new User();
        user.setId(id);

        tokenVerifier.unpack(tokenGenerator.generate(user));
    }

    @Test(expected = JWTVerificationException.class)
    public void verifyFailureToken() throws Exception
    {
        JwtSecret         secretA        = new BasicJwtSecret("secretA");
        JwtSecret         secretB        = new BasicJwtSecret("secretB");
        JwtTokenGenerator tokenGenerator = new JwtTokenGenerator(secretA);
        JwtTokenVerifier  tokenVerifier  = new JwtTokenVerifier(secretB);

        int  id   = 43252;
        User user = new User();
        user.setId(id);

        tokenVerifier.unpack("a" + tokenGenerator.generate(user)); // Illegal header format
    }
}
