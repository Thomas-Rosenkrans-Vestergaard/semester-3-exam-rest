package com.group3.sem3exam.data.repositories;

import org.junit.jupiter.api.DynamicTest;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionalReadCrudRepositoryTester<E, K, T extends TransactionalReadCrudRepository<E, K>>
{

    protected final Supplier<T>                constructor;
    protected final Function<T, TreeMap<K, E>> dataProducer;
    protected final K                          unknownKey;

    public TransactionalReadCrudRepositoryTester(
            Supplier<T> constructor,
            Function<T, TreeMap<K, E>> dataProducer,
            K unknownKey)
    {
        this.constructor = constructor;
        this.dataProducer = dataProducer;
        this.unknownKey = unknownKey;
    }

    Collection<DynamicTest> getDynamicTests()
    {
        return new ArrayList<>(Arrays.asList(
                createGetTest(),
                createGetPaginatedTest(),
                createCountTest(),
                createGetByIdTest(),
                createExistsTest()
        ));
    }

    private DynamicTest createGetTest()
    {
        return DynamicTest.dynamicTest("get", () -> {

            try (T instance = constructor.get()) {
                instance.begin();
                TreeMap<K, E> data   = dataProducer.apply(instance);
                List<E>       result = instance.get();
                assertEquals(data.size(), result.size());
                int index = 0;
                for (E dataElement : data.values())
                    assertEquals(dataElement, result.get(index++));
            }
        });
    }

    private DynamicTest createGetPaginatedTest()
    {
        return DynamicTest.dynamicTest("getPaginated", () -> {

            try (T instance = constructor.get()) {
                instance.begin();
                TreeMap<K, E> data     = dataProducer.apply(instance);
                List<E>       dataList = new ArrayList<>(data.values());

                if (data.size() < 3) {
                    fail(String.format("Cannot test %s.getPaginated(), not enough test data.", instance.getClass().getName()));
                    return;
                }

                {
                    // Test page size
                    List<E> result = instance.getPaginated(3, 0);
                    assertEquals(3, result.size());
                    assertEquals(dataList.get(0), result.get(0));
                    assertEquals(dataList.get(2), result.get(2));
                }

                {
                    // Test offset
                    List<E> result = instance.getPaginated(2, 2);
                    assertEquals(2, result.size());
                    assertEquals(dataList.get(2), result.get(0));
                    assertEquals(dataList.get(3), result.get(1));
                }

                {
                    // Test empty limit
                    assertEquals(0, instance.getPaginated(0, 0).size());
                }
            }
        });
    }

    private DynamicTest createCountTest()
    {
        return DynamicTest.dynamicTest("count", () -> {
            try (T instance = constructor.get()) {
                instance.begin();
                TreeMap<K, E> data = dataProducer.apply(instance);
                assertEquals(data.size(), instance.count());
            }
        });
    }

    private DynamicTest createGetByIdTest()
    {
        return DynamicTest.dynamicTest("get(id)", () -> {
            try (T instance = constructor.get()) {
                instance.begin();
                TreeMap<K, E> data = dataProducer.apply(instance);
                for (Map.Entry<K, E> entry : data.entrySet())
                    assertEquals(entry.getValue(), instance.get(entry.getKey()));
            }
        });
    }

    private DynamicTest createExistsTest()
    {
        return DynamicTest.dynamicTest("exists", () -> {
            try (T instance = constructor.get()) {
                instance.begin();
                TreeMap<K, E> data = dataProducer.apply(instance);
                for (Map.Entry<K, E> entry : data.entrySet()) {
                    assertTrue(instance.exists(entry.getKey()));
                }

                assertFalse(instance.exists(unknownKey));
            }
        });
    }
}
