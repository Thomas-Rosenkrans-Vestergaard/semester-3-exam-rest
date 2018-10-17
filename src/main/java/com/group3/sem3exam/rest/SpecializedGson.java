package com.group3.sem3exam.rest;

import com.google.gson.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

}
