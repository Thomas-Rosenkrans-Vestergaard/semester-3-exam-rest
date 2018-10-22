package com.group3.sem3exam.rest.authentication;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.UserRepository;
import com.group3.sem3exam.rest.authentication.jwt.BasicJwtSecret;
import com.group3.sem3exam.rest.authentication.jwt.JwtSecret;
import com.group3.sem3exam.rest.authentication.jwt.JwtTokenGenerator;
import com.group3.sem3exam.rest.authentication.jwt.JwtTokenUnpacker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtTokenGeneratorVerifierTest
{

    private static User           user;
    private static UserRepository userRepository;

    @BeforeAll
    public static void before() throws Exception
    {
        userRepository = mock(UserRepository.class);
        user = new User();
        user.setId(1);
        when(userRepository.get(eq(1))).thenReturn(user);
    }

    @Test
    public void verifySuccess() throws Exception
    {
        JwtSecret         secret         = new BasicJwtSecret(256 / 8);
        JwtTokenGenerator tokenGenerator = new JwtTokenGenerator(secret);
        JwtTokenUnpacker  tokenVerifier  = new JwtTokenUnpacker(userRepository, secret);

        String token = tokenGenerator.generate(AuthenticationContext.user(user));
        assertEquals(user, tokenVerifier.unpack(token).getUser());
    }

    @Test
    public void verifyFailureSecret() throws Exception
    {
        assertThrows(JWTVerificationException.class, () -> {
            JwtSecret         secretA        = new BasicJwtSecret("secretA");
            JwtSecret         secretB        = new BasicJwtSecret("secretB");
            JwtTokenGenerator tokenGenerator = new JwtTokenGenerator(secretA);
            JwtTokenUnpacker  tokenVerifier  = new JwtTokenUnpacker(userRepository, secretB);

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
            JwtTokenUnpacker  tokenVerifier  = new JwtTokenUnpacker(userRepository, secretB);

            tokenVerifier.unpack("a" + tokenGenerator.generate(AuthenticationContext.user(user))); // Illegal header format
        });
    }
}
