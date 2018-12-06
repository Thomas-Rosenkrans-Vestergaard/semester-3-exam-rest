package com.group3.sem3exam.data.repositories.base;

import org.junit.jupiter.api.DynamicTest;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class JpaCrudRepositoryTester<
        E extends RepositoryEntity<K>,
        K extends Comparable<K>,
        I extends JpaCrudRepository<E, K>>
        extends JpaReadRepositoryTester<E, K, I>
{

    public JpaCrudRepositoryTester(
            Supplier<I> constructor,
            Function<I, TreeMap<K, E>> dataProducer,
            K unknownKey)
    {
        super(constructor, dataProducer, unknownKey);
    }

    public Collection<DynamicTest> getDynamicTests()
    {
        Collection<DynamicTest> tests = super.getDynamicTests();
        tests.add(createUpdateTest());
        tests.add(createDeleteTest());
        tests.add(createDeleteEntityTest());
        tests.add(createDeleteKeyCollection());
        tests.add(createDeleteListOfEntitiesTest());

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
        return DynamicTest.dynamicTest("delete(K)", () -> {
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


    public DynamicTest createDeleteEntityTest()
    {
        return DynamicTest.dynamicTest("delete(E)", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                for (E e : data) {
                    assertTrue(instance.exists(e.getId()));
                    assertEquals(e, instance.delete(e));
                    assertFalse(instance.exists(e.getId()));
                }
            }
        });
    }

    public DynamicTest createDeleteKeyCollection()
    {

        return DynamicTest.dynamicTest("delete(K...)", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                Map<K, E> data    = dataProducer.apply(instance);
                Map<K, E> deleted = instance.delete(data.keySet());

                for (Map.Entry<K, E> entry : deleted.entrySet())
                    assertFalse(instance.exists(entry.getKey()));
            }
        });
    }

    public DynamicTest createDeleteListOfEntitiesTest()
    {

        return DynamicTest.dynamicTest("delete(E...)", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                Map<K, E> data = dataProducer.apply(instance);
                for (E entity : data.values())
                    assertTrue(instance.exists(entity.getId()));
                instance.delete(new ArrayList<>(data.values()));
                for (E entity : data.values())
                    assertFalse(instance.exists(entity.getId()));
            }
        });
    }
}
