package com.group3.sem3exam.data.repositories;

import org.junit.jupiter.api.DynamicTest;

import java.util.Collection;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionalCrudRepositoryTester<
        E extends RepositoryEntity<K>,
        K extends Comparable<K>,
        I extends TransactionalCrudRepository<E, K>>
        extends TransactionalReadCrudRepositoryTester<E, K, I>
{

    public TransactionalCrudRepositoryTester(
            Supplier<I> constructor,
            Function<I, TreeMap<K, E>> dataProducer,
            K unknownKey)
    {
        super(constructor, dataProducer, unknownKey);
    }

    Collection<DynamicTest> getDynamicTests()
    {
        Collection<DynamicTest> tests = super.getDynamicTests();
        tests.add(createUpdateTest());
        tests.add(createDeleteTest());

        return tests;
    }

    private DynamicTest createUpdateTest()
    {
        return DynamicTest.dynamicTest("update", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                TreeMap<K, E> data = dataProducer.apply(instance);

            }
        });
    }

    private DynamicTest createDeleteTest()
    {
        return DynamicTest.dynamicTest("delete", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                TreeMap<K, E> data = dataProducer.apply(instance);
                for (K key : data.keySet()) {
                    assertTrue(instance.exists(key));
                    assertNotNull(instance.delete(key));
                    assertFalse(instance.exists(key));
                }

                assertNull(instance.delete(unknownKey));
            }
        });
    }
}
