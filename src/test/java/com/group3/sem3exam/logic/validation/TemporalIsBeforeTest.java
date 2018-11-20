package com.group3.sem3exam.logic.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemporalIsBeforeTest
{

    private class Tester
    {
        public LocalDateTime subject;
    }

    @Test
    void isTrue()
    {

        Tester tester = new Tester();
        tester.subject = LocalDateTime.now();

        TemporalIsBefore<Tester> instance = new TemporalIsBefore<>("subject", (t) -> t.subject, LocalDateTime.now());
        assertTrue(instance.isTrue(LocalDateTime.now().minusDays(1)));
        assertFalse(instance.isTrue(LocalDateTime.now().plusDays(1)));
    }
}