package com.group3.sem3exam.logic.authentication;

import com.group3.sem3exam.data.entities.User;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class LazyAuthenticationContextTest
{

    @Test
    void user()
    {
    }

    @Test
    void getType()
    {
        LazyAuthenticationContext lac = LazyAuthenticationContext.user(null, () -> null);
        assertEquals(AuthenticationType.USER, lac.getType());
    }

    @Test
    void getUserId()
    {
        LazyAuthenticationContext lac;

        lac = LazyAuthenticationContext.user(null, () -> null);
        assertNull(lac.getUserId());


        lac = LazyAuthenticationContext.user(5, () -> null);
        assertEquals(5, (int) lac.getUserId());
    }

    @Test
    void getUserIdReturnsNull()
    {
        LazyAuthenticationContext lac = new LazyAuthenticationContext(AuthenticationType.THIRD_PARTY, null, null);
        assertNull(lac.getUserId());
    }

    @Test
    void getUser()
    {
        User           user     = mock(User.class);
        Supplier<User> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(user);
        LazyAuthenticationContext lac = LazyAuthenticationContext.user(1, supplier);

        assertSame(user, lac.getUser());
        assertSame(user, lac.getUser());
        assertSame(user, lac.getUser());

        verify(supplier, times(1)).get();
    }

    @Test
    void getUserReturnsNull()
    {
        LazyAuthenticationContext lac = new LazyAuthenticationContext(AuthenticationType.THIRD_PARTY, null, null);
        assertNull(lac.getUser());
    }
}