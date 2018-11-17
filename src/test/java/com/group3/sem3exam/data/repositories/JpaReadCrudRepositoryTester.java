package com.group3.sem3exam.data.repositories;

import com.group3.sem3exam.data.repositories.queries.RepositoryQuery;
import org.junit.jupiter.api.DynamicTest;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class JpaReadCrudRepositoryTester<
        E extends RepositoryEntity<K>,
        K extends Comparable<K>,
        I extends JpaReadCrudRepository<E, K>>
{

    protected final Supplier<I>                constructor;
    protected final Function<I, TreeMap<K, E>> dataProducer;
    protected final K                          unknownKey;
    protected final long                       initialDataSize;

    public JpaReadCrudRepositoryTester(
            Supplier<I> constructor,
            Function<I, TreeMap<K, E>> dataProducer,
            K unknownKey,
            long initialDataSize)
    {
        this.constructor = constructor;
        this.dataProducer = dataProducer;
        this.unknownKey = unknownKey;
        this.initialDataSize = initialDataSize;
    }

    Collection<DynamicTest> getDynamicTests()
    {
        ArrayList<DynamicTest> tests = new ArrayList<>(Arrays.asList(
                createGetTest(),
                createGetPaginatedTest(),
                createCountTest(),
                createGetByIdTest(),
                createGetByIdsTest(),
                createExistsTest()
        ));

        tests.addAll(createQueryTests());

        return tests;
    }

    private DynamicTest createGetTest()
    {
        return DynamicTest.dynamicTest("get", () -> {

            try (I instance = constructor.get()) {
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

            try (I instance = constructor.get()) {
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
            try (I instance = constructor.get()) {
                instance.begin();
                TreeMap<K, E> data = dataProducer.apply(instance);
                assertEquals(data.size(), instance.count());
            }
        });
    }

    private DynamicTest createGetByIdTest()
    {
        return DynamicTest.dynamicTest("get(id)", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                TreeMap<K, E> data = dataProducer.apply(instance);
                for (Map.Entry<K, E> entry : data.entrySet())
                    assertEquals(entry.getValue(), instance.get(entry.getKey()));
            }
        });
    }


    private DynamicTest createGetByIdsTest()
    {
        return DynamicTest.dynamicTest("get(Set<id>)", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                TreeMap<K, E> data = dataProducer.apply(instance);
                if (data.size() < 3) {
                    fail("Cannot test get(Set<id>): not enough data.");
                    return;
                }

                List<K> keys = new ArrayList<>(data.keySet());
                Map<K, E> result = instance.get(new HashSet<>(Arrays.asList(
                        keys.get(0),
                        keys.get(2)
                )));


                assertEquals(2, result.size());
                assertEquals(data.get(keys.get(0)), result.get(keys.get(0)));
                assertEquals(data.get(keys.get(2)), result.get(keys.get(2)));
            }
        });
    }

    private DynamicTest createExistsTest()
    {
        return DynamicTest.dynamicTest("exists", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                TreeMap<K, E> data = dataProducer.apply(instance);
                for (Map.Entry<K, E> entry : data.entrySet()) {
                    assertTrue(instance.exists(entry.getKey()));
                }

                assertFalse(instance.exists(unknownKey));
            }
        });
    }

    private List<DynamicTest> createQueryTests()
    {
        return Arrays.asList(
                createQueryDescTest(),
                createQueryAscTest(),
                createQueryWhereGtTest(),
                createQueryWhereLtTest(),
                createQueryWhereGtoeTest(),
                createQueryWhereLtoeTest(),
                createQueryGetTest(),
                createQueryGetPageTest(),
                createQueryGetFirstNTest(),
                createQueryGetAtTest(),
                createQueryGetFirstTest(),
                createQueryCountTest(),
                createQueryExistsTest(),
                createQueryContainsTest(),
                createQuerySkipGetAtTest(),
                createQueryLimitGetTest(),
                createQueryMaxTest(),
                createQueryMinTest(),
                createQueryGetAttributesTest(),
                createQueryGetKeysTest(),
                createQueryChunkTest()
        );
    }

    private static Random random = new Random();

    private K getRandomKey(List<E> keys)
    {
        return keys.get(random.nextInt(keys.size())).getId();
    }

    private DynamicTest createQueryWhereGtTest()
    {
        return DynamicTest.dynamicTest("query.gt", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data   = new ArrayList<>(dataProducer.apply(instance).values());
                K       cutoff = getRandomKey(data);

                assertEquals(
                        data.stream().filter(e -> e.getId().compareTo(cutoff) > 0).collect(Collectors.toList()),
                        instance.query().gt(instance.kAttribute, cutoff).get()
                );
            }
        });
    }

    private DynamicTest createQueryWhereLtTest()
    {
        return DynamicTest.dynamicTest("query.lt", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data   = new ArrayList<>(dataProducer.apply(instance).values());
                K       cutoff = getRandomKey(data);

                assertEquals(
                        data.stream().filter(e -> e.getId().compareTo(cutoff) < 0).collect(Collectors.toList()),
                        instance.query().lt(instance.kAttribute, cutoff).get()
                );
            }
        });
    }

    private DynamicTest createQueryWhereGtoeTest()
    {
        return DynamicTest.dynamicTest("query.gtoe", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data   = new ArrayList<>(dataProducer.apply(instance).values());
                K       cutoff = getRandomKey(data);

                assertEquals(
                        data.stream().filter(e -> e.getId().compareTo(cutoff) > -1).collect(Collectors.toList()),
                        instance.query().gtoe(instance.kAttribute, cutoff).get()
                );
            }
        });
    }

    private DynamicTest createQueryWhereLtoeTest()
    {
        return DynamicTest.dynamicTest("query.ltoe", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data   = new ArrayList<>(dataProducer.apply(instance).values());
                K       cutoff = getRandomKey(data);

                assertEquals(
                        data.stream().filter(e -> e.getId().compareTo(cutoff) < 1).collect(Collectors.toList()),
                        instance.query().ltoe(instance.kAttribute, cutoff).get()
                );
            }
        });
    }

    private DynamicTest createQueryGetTest()
    {
        return DynamicTest.dynamicTest("query.get", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                Collection<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                assertEquals(data, instance.query().get());
            }
        });
    }

    private DynamicTest createQueryGetPageTest()
    {
        return DynamicTest.dynamicTest("query.getPage", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());

                if (data.size() < 3) {
                    fail(String.format("Cannot test %s.query().getPage(), not enough test data.", instance.getClass().getName()));
                    return;
                }

                {
                    // Test page size
                    List<E> result = instance.query().getPage(3, 0);
                    assertEquals(3, result.size());
                    assertEquals(data.get(0), result.get(0));
                    assertEquals(data.get(2), result.get(2));
                }

                {
                    // Test offset
                    List<E> result = instance.query().getPage(2, 2);
                    assertEquals(2, result.size());
                    assertEquals(data.get(2), result.get(0));
                    assertEquals(data.get(3), result.get(1));
                }

                assertEquals(data, instance.query().get());
            }
        });
    }

    private DynamicTest createQueryGetFirstNTest()
    {
        return DynamicTest.dynamicTest("query.getFirst(n)", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data   = new ArrayList<>(dataProducer.apply(instance).values());
                List<E> result = instance.query().getFirst(data.size() - 1);
                assertEquals(data.size() - 1, result.size());
                assertEquals(data.get(0), result.get(0));
                assertEquals(data.get(result.size() - 1), result.get(result.size() - 1));
            }
        });
    }

    private DynamicTest createQueryGetAtTest()
    {
        return DynamicTest.dynamicTest("query.getAt", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());

                assertEquals(data.get(0), instance.query().getAt(0));
                assertEquals(data.get(data.size() - 1), instance.query().getAt(data.size() - 1));
            }
        });
    }

    private DynamicTest createQueryGetFirstTest()
    {
        return DynamicTest.dynamicTest("query.getFirst", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data   = new ArrayList<>(dataProducer.apply(instance).values());
                E       result = instance.query().desc("id").getFirst();
                assertEquals(data.get(data.size() - 1), result);
            }
        });
    }

    private DynamicTest createQueryCountTest()
    {
        return DynamicTest.dynamicTest("query.count", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                assertEquals(data.size(), instance.query().count());
            }

            try (I instance = constructor.get()) {
                instance.begin();
                assertEquals(this.initialDataSize, instance.query().count());
            }
        });
    }

    private DynamicTest createQueryExistsTest()
    {
        return DynamicTest.dynamicTest("query.exists", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                if (data.size() > 0)
                    assertTrue(instance.query().exists());
                else
                    assertFalse(instance.query().exists());
            }

            try (I instance = constructor.get()) {
                instance.begin();
                if (this.initialDataSize > 0)
                    assertTrue(instance.query().exists());
                else
                    assertFalse(instance.query().exists());
            }
        });
    }

    private DynamicTest createQueryContainsTest()
    {
        return DynamicTest.dynamicTest("query.contains", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                K       key  = data.get(0).getId();
                assertTrue(instance.query().contains(key));
                assertFalse(instance.query().not(instance.kAttribute, key).contains(key));

                assertFalse(instance.exists(unknownKey));
            }
        });
    }

    private DynamicTest createQuerySkipGetAtTest()
    {
        return DynamicTest.dynamicTest("query.skip + query.getAt", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                assertEquals(data.get(1), instance.query().skip(1).getAt(0));
            }
        });
    }

    private DynamicTest createQueryLimitGetTest()
    {
        return DynamicTest.dynamicTest("query.limit + query.get", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data   = new ArrayList<>(dataProducer.apply(instance).values());
                List<E> result = instance.query().limit(data.size() - 1).get();
                assertEquals(data.size() - 1, result.size());
            }
        });
    }

    private DynamicTest createQueryMaxTest()
    {
        return DynamicTest.dynamicTest("query.max", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                assertEquals(data.get(data.size() - 1).getId(), instance.query().max(instance.kAttribute, instance.kClass));
            }
        });
    }

    private DynamicTest createQueryMinTest()
    {
        return DynamicTest.dynamicTest("query.min", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                assertEquals(data.get(0).getId(), instance.query().min(instance.kAttribute, instance.kClass));
            }
        });
    }

    private DynamicTest createQueryGetAttributesTest()
    {
        return DynamicTest.dynamicTest("query.getAttributes", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                assertEquals(data.stream().map(E::getId).collect(Collectors.toList()),
                             instance.query().getAttributes(instance.kAttribute, instance.kClass));
            }
        });
    }

    private DynamicTest createQueryGetKeysTest()
    {
        return DynamicTest.dynamicTest("query.getKeys", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                assertEquals(data.stream().map(E::getId).collect(Collectors.toList()),
                             instance.query().getKeys());
            }
        });
    }

    private DynamicTest createQueryChunkTest()
    {
        return DynamicTest.dynamicTest("query.chunk", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                if (data.size() < 3) {
                    fail("Cannot test query.chunk, not enough data.");
                    return;
                }

                CountingChunker<E> countingChunker = new CountingChunker<>(data.size());
                assertTrue(instance.query().chunk(2, countingChunker));
                assertEquals(0, countingChunker.missing);
                assertEquals(Math.ceil((float) data.size() / 2), countingChunker.counter);

                // Test that chunk() returns false when the chunk was stopped.
                assertFalse(instance.query().chunk(2, ((chunk, stopper) -> stopper.run())));
            }
        });
    }

    private class CountingChunker<E> implements RepositoryQuery.Chunker<E>
    {
        public int missing;
        public int counter = 0;

        public CountingChunker(int missing)
        {
            this.missing = missing;
        }

        @Override
        public void handle(RepositoryQuery.Chunk<E> chunk, Runnable stopper)
        {
            counter++;
            missing -= chunk.getResults().size();
        }
    }

    private DynamicTest createQueryDescTest()
    {
        return DynamicTest.dynamicTest("query.desc + query.getKeys", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                assertEquals(
                        data.stream().map(E::getId).sorted(Comparator.reverseOrder()).collect(Collectors.toList()),
                        instance.query().desc(instance.kAttribute).getKeys()
                );
            }
        });
    }

    private DynamicTest createQueryAscTest()
    {
        return DynamicTest.dynamicTest("query.asc + query.getKeys", () -> {
            try (I instance = constructor.get()) {
                instance.begin();
                List<E> data = new ArrayList<>(dataProducer.apply(instance).values());
                assertEquals(
                        data.stream().map(E::getId).sorted(Comparator.naturalOrder()).collect(Collectors.toList()),
                        instance.query().asc(instance.kAttribute).getKeys()
                );
            }
        });
    }
}
