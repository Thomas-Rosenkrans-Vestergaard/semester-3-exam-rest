package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.JpaTestConnection;
import com.group3.sem3exam.data.entities.Region;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionalRegionRepositoryTest
{

    @TestFactory
    public Collection<DynamicTest> testCrudRepositoryMethods()
    {
        TransactionalReadCrudRepositoryTester<Region, Integer, TransactionalRegionRepository> tester =
                new TransactionalReadCrudRepositoryTester<>(
                        () -> new TransactionalRegionRepository(JpaTestConnection.create()),
                        (repository) -> createRegionMap(repository),
                        -1
                );

        return tester.getDynamicTests();
    }

    private TreeMap<Integer, Region> createRegionMap(TransactionalRegionRepository repository)
    {
        TreeMap<Integer, Region> map = new TreeMap<>();
        for (Region Region : repository.get())
            map.put(Region.getId(), Region);

        return map;
    }

    @Test
    void getByCountry()
    {
        try (TransactionalRegionRepository trr = new TransactionalRegionRepository(JpaTestConnection.create())) {
            List<Region> result = trr.getByCountry(5);
            assertEquals(3, result.size());
            assertEquals(5, (int) result.get(0).getId());
            assertEquals(7, (int) result.get(2).getId());
        }
    }
}