package com.group3.sem3exam.rest.authentication;

import com.group3.sem3exam.rest.authentication.jwt.FileJwtSecret;
import com.group3.sem3exam.rest.authentication.jwt.JwtSecret;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FileJwtSecretTest
{

    private final File saveFile = new File("./src/test/java/com/group3/sem3exam/rest/authentication/saveFile.test");

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
        assertEquals("", instance.getValue());
    }

    @Test
    public void constructorReadsNonEmpty() throws Exception
    {
        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            fileWriter.write("secret");
            fileWriter.flush();
        }

        JwtSecret instance = new FileJwtSecret(saveFile);
        assertEquals("secret", instance.getValue());
    }

    @Test
    public void constructorGeneratesAndWritesWhenEmpty() throws Exception
    {
        assertEquals(0, saveFile.length());

        JwtSecret instance = new FileJwtSecret(saveFile, 6);
        assertNotEquals(0, saveFile.length());
        assertEquals(instance.getValue(), read());
    }

    @Test
    public void constructorDoesNotGenerateAndWriteWhenNonEmpty() throws Exception
    {
        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            fileWriter.write("secret");
            fileWriter.flush();
        }

        JwtSecret instance = new FileJwtSecret(saveFile, 6);
        assertEquals("secret", instance.getValue());
    }

    @Test
    public void regenerate() throws Exception
    {
        assertEquals(0, saveFile.length());
        JwtSecret instance = new FileJwtSecret(saveFile);
        assertEquals(instance.regenerate(6), read());
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
}