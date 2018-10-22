package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.data.entities.Country;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionalCountryRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        TransactionalReadCrudRepositoryTester<Country, Integer, TransactionalCountryRepository> tester =
                new TransactionalReadCrudRepositoryTester<>(
                        () -> new TransactionalCountryRepository(JpaTestConnection.emf),
                        (repository) -> createCountryMap(repository),
                        -1
                );

        return tester.getDynamicTests();
    }

    private TreeMap<Integer, Country> createCountryMap(TransactionalCountryRepository repository)
    {
        TreeMap<Integer, Country> map = new TreeMap<>();
        for (Country Country : repository.get())
            map.put(Country.getId(), Country);

        return map;
    }

    @Test
    void get()
    {
        try (TransactionalCountryRepository tcr = new TransactionalCountryRepository(JpaTestConnection.emf)) {
            List<Country> result = tcr.get();
            assertEquals(5, result.size());
            assertEquals(1, (int) result.get(0).getId());
            assertEquals(5, (int) result.get(4).getId());
        }
    }
}