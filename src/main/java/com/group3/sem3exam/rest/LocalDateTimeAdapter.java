package com.group3.sem3exam.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>
{
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public JsonElement serialize(LocalDateTime dateTime, Type typeOfSrc, JsonSerializationContext context)
    {
        return new JsonPrimitive(formatter.format(dateTime));
    }
}
