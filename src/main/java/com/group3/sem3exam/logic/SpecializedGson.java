package com.group3.sem3exam.logic;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SpecializedGson
{

    public static Gson create()
    {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    static private class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime>
    {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }

        public JsonElement serialize(LocalDateTime dateTime, Type typeOfSrc, JsonSerializationContext context)
        {
            return new JsonPrimitive(formatter.format(dateTime));
        }
    }

    static private class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate>
    {

        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context)
        {
            return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            return LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(json.getAsString()));
        }
    }
}
