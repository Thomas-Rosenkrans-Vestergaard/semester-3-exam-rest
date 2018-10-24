package com.group3.sem3exam.rest.authentication.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.rest.authentication.AuthenticationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtTokenGeneratorUnpackerTest
{

    @Test
    public void verifySuccess() throws Exception
    {
        JwtSecret         secret         = new BasicJwtSecret(256 / 8);
        JwtTokenGenerator tokenGenerator = new JwtTokenGenerator(secret);
        JwtTokenUnpacker  tokenVerifier  = new JwtTokenUnpacker(secret);

        User user = new User();
        user.setId(1);
        String token = tokenGenerator.generate(AuthenticationContext.user(user));
        assertEquals(1, (int) tokenVerifier.unpack(token).getClaim("user").asInt());
    }

    @Test
    public void verifyFailureSecret() throws Exception
    {
        assertThrows(JWTVerificationException.class, () -> {
            JwtSecret         secretA        = new BasicJwtSecret("secretA");
            JwtSecret         secretB        = new BasicJwtSecret("secretB");
            JwtTokenGenerator tokenGenerator = new JwtTokenGenerator(secretA);
            JwtTokenUnpacker  tokenVerifier  = new JwtTokenUnpacker(secretB);

            User user = new User();
            tokenVerifier.unpack(tokenGenerator.generate(AuthenticationContext.user(user)));
        });
    }

    @Test
    public void verifyFailureToken() throws Exception
    {
        assertThrows(JWTVerificationException.class, () -> {
            JwtSecret         secretA        = new BasicJwtSecret("secretA");
            JwtSecret         secretB        = new BasicJwtSecret("secretB");
            JwtTokenGenerator tokenGenerator = new JwtTokenGenerator(secretA);
            JwtTokenUnpacker  tokenVerifier  = new JwtTokenUnpacker(secretB);

            User user = new User();
            tokenVerifier.unpack("a" + tokenGenerator.generate(AuthenticationContext.user(user))); // Illegal header format
        });
    }
}
