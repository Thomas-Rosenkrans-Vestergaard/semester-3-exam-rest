package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.data.entities.City;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JpaCityRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        JpaReadCrudRepositoryTester<City, Integer, JpaCityRepository> tester =
                new JpaReadCrudRepositoryTester<>(
                        () -> new JpaCityRepository(JpaTestConnection.create()),
                        (repository) -> createCityMap(repository),
                        -1
                );

        return tester.getDynamicTests();
    }

    private TreeMap<Integer, City> createCityMap(JpaCityRepository repository)
    {
        TreeMap<Integer, City> map = new TreeMap<>();
        for (City city : repository.get())
            map.put(city.getId(), city);

        return map;
    }

    @Test
    void getByRegion()
    {
        try (JpaCityRepository tcr = new JpaCityRepository(JpaTestConnection.create())) {
            List<City> result = tcr.getByRegion(5);
            assertEquals(3, result.size());
            assertEquals(5, (int) result.get(0).getId());
            assertEquals(6, (int) result.get(1).getId());
            assertEquals(7, (int) result.get(2).getId());
        }
    }
}