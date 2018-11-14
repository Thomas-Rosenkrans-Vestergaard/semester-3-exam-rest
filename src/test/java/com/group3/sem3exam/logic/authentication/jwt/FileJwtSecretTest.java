package com.group3.sem3exam.logic.authentication.jwt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FileJwtSecretTest
{

    private final File saveFile = new File("./src/test/java/com/group3/sem3exam/logic/authentication/jwt/saveFile.test");

    @AfterEach
    public void tearDown() throws Exception
    {
        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            fileWriter.append("");
            fileWriter.flush();
        }
    }

    @Test
    public void constructorReadsEmpty() throws Exception
    {
        JwtSecret instance = new FileJwtSecret(saveFile);
        assertEquals(0, instance.getValue().length);
    }

    @Test
    public void constructorReadsNonEmpty() throws Exception
    {
        byte[] randomBytes = randomByteArray(5);

        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            fileWriter.write(Base64.getEncoder().encodeToString(randomBytes));
            fileWriter.flush();
        }

        JwtSecret instance = new FileJwtSecret(saveFile);
        assertArrayEquals(randomBytes, instance.getValue());
    }

    @Test
    public void constructorGeneratesAndWritesWhenEmpty() throws Exception
    {
        assertEquals(0, saveFile.length());

        JwtSecret instance = new FileJwtSecret(saveFile, 6);
        assertNotEquals(0, saveFile.length());
        assertEquals(Base64.getEncoder().encodeToString(instance.getValue()), read());
    }

    @Test
    public void constructorDoesNotGenerateAndWriteWhenNonEmpty() throws Exception
    {
        byte[] randomBytes = randomByteArray(5);
        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            fileWriter.write(Base64.getEncoder().encodeToString(randomBytes));
            fileWriter.flush();
        }

        JwtSecret instance = new FileJwtSecret(saveFile, 6);
        assertArrayEquals(randomBytes, instance.getValue());
    }

    @Test
    public void regenerate() throws Exception
    {
        assertEquals(0, saveFile.length());
        JwtSecret instance = new FileJwtSecret(saveFile);
        assertArrayEquals(instance.regenerate(6), Base64.getDecoder().decode(read()));
    }

    private String read() throws Exception
    {
        Scanner stream = new Scanner(new FileInputStream(saveFile));
        if (!stream.hasNextLine())
            return "";
        String contents = stream.nextLine();
        stream.close();
        return contents;
    }

    private byte[] randomByteArray(int size)
    {
        byte[]       array        = new byte[size];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(array);
        return array;
    }
}