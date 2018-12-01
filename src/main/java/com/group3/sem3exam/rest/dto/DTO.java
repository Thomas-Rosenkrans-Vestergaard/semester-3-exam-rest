package com.group3.sem3exam.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DTO
{

    public static <E, D> List<D> map(List<E> elements, Function<E, D> f)
    {
        List<D> results = new ArrayList<>();
        for (E element : elements)
            results.add(f.apply(element));

        return results;
    }
}
