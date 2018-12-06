package com.group3.sem3exam;

import com.group3.sem3exam.data.entities.City;
import com.group3.sem3exam.data.entities.Gender;
import com.group3.sem3exam.data.entities.Post;
import com.group3.sem3exam.data.entities.User;
import com.group3.sem3exam.data.repositories.PostRepository;
import com.group3.sem3exam.data.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class TestingUtils
{

    private static Random      random     = new Random();
    private static Set<String> userEmails = new HashSet<>();

    public static User randomUser(UserRepository repository, City city)
    {
        return repository.createUser(
                randomString(),
                randomUnique(TestingUtils::randomString, userEmails),
                randomString(),
                city,
                randomEnum(Gender.class),
                LocalDate.now()
        );
    }

    public static Post randomPost(PostRepository repository, User user)
    {
        return repository.create(
                user,
                randomString(),
                new ArrayList<>(),
                LocalDateTime.now()
        );
    }

    private static <T extends Enum<T>> T randomEnum(Class<T> c)
    {
        int x = random.nextInt(c.getEnumConstants().length);
        return c.getEnumConstants()[x];
    }

    private static <T> T randomUnique(Supplier<T> f, Set<T> set)
    {
        while (true) {
            T attempt = f.get();
            if (!set.contains(attempt)) {
                set.add(attempt);
                return attempt;
            }
        }
    }

    private static String randomString()
    {
        int           leftLimit          = 97; // letter 'a'
        int           rightLimit         = 122; // letter 'z'
        int           targetStringLength = 10;
        StringBuilder buffer             = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }
}
