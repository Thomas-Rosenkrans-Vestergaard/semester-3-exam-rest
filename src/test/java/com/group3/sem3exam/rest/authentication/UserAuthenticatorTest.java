package com.group3.sem3exam.rest.authentication;

import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserAuthenticatorTest
{

    private User              existing;
    private UserAuthenticator instance;

    @BeforeEach
    public void setUp()
    {

        existing = new User();
        existing.setEmail("existing@email.com");
        existing.setPasswordHash(BCrypt.hashpw("password", BCrypt.gensalt()));

        instance = new UserAuthenticator(() -> {
            UserRepository repository = mock(UserRepository.class);
            when(repository.getByEmail(any())).thenReturn(null);
            when(repository.getByEmail("existing@email.com")).thenReturn(existing);
            return repository;
        });
    }

    @Test
    public void authenticateCorrectCredentials() throws Exception
    {
        AuthenticationContext authenticationContext = instance.authenticate("existing@email.com", "password");
        assertNotNull(authenticationContext);
        assertNotNull(authenticationContext.getType());
        assertNotNull(authenticationContext.getUser());
        assertEquals(existing, authenticationContext.getUser());
    }

    @Test
    public void authenticateIncorrectEmail() throws Exception
    {
        assertThrows(AuthenticationException.class, () -> {
            instance.authenticate("nonexistant@email.com", "password");
        });
    }

    @Test
    public void authenticateIncorrectPassword() throws Exception
    {
        assertThrows(AuthenticationException.class, () -> {
            instance.authenticate("existing@email.com", "wrong password");
        });
    }
}