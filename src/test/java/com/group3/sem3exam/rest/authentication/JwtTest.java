package com.group3.sem3exam.rest.authentication;

import com.group3.sem3exam.data.entities.User;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class JwtTest
{

    @Test
    public void testSuccess() throws Exception
    {
        JwtSecret         secret    = new JwtSecret("secret");
        JwtTokenGenerator generator = new JwtTokenGenerator(secret);
        JwtTokenUnpacker  unpacker  = new JwtTokenUnpacker(secret);

        User user = new User();
        user.setId(15);

        String generated = generator.generate(user);
        assertEquals(15, unpacker.unpack(generated).getUserId());
    }

    @Test(expected = AuthenticationException.class)
    public void testFailure() throws Exception
    {
        JwtSecret         secretA   = new JwtSecret("secretA");
        JwtSecret         secretB   = new JwtSecret("secretB");
        JwtTokenGenerator generator = new JwtTokenGenerator(secretA);
        JwtTokenUnpacker  unpacker  = new JwtTokenUnpacker(secretB);

        User user = new User();
        user.setId(15);

        String generated = generator.generate(user);
        unpacker.unpack(generated);
    }
}
