package com.group3.sem3exam.logic.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class IsAfterCheckTest
{

    @Test
    void passes()
    {
        assertTrue(new IsAfterCheck<>(LocalDateTime.now().plusDays(1), "subject", LocalDateTime.now()).passes());
        assertFalse(new IsAfterCheck<>(LocalDateTime.now().minusDays(1), "subject", LocalDateTime.now()).passes());
    }
}