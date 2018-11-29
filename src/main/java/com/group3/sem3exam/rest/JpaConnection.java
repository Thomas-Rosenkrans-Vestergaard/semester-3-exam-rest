package com.group3.sem3exam.rest;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class JpaConnection
{
    private static EntityManagerFactory emf;

    public static EntityManagerFactory create()
    {
        if (emf == null)
            emf = createConnection();

        return emf;
    }

    public static void main(String[] args)
    {
        createConnection();
    }

    private static EntityManagerFactory createConnection()
    {
        System.out.println("Loading connection.cfg.");
        Map<String, String> options = loadOptions("/connection.cfg");
        System.out.println(String.format("Found %d options.", options.size()));
        return Persistence.createEntityManagerFactory("rest-api-pu", options);
    }

    private static Map<String, String> loadOptions(String file)
    {
        HashMap<String, String> options     = new HashMap<>();
        InputStream             inputStream = JpaConnection.class.getResourceAsStream(file);
        if (inputStream == null)
            throw new IllegalStateException(String.format("No %s configuration file in resources.", file));

        Scanner scanner    = new Scanner(inputStream);
        int     lineNumber = 1;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            line = line.trim();

            if (!line.isEmpty()) {
                if (line.startsWith("#")) {
                    System.out.println("Ignoring line " + lineNumber);
                    continue;
                }
                line += " ";
                String[] parts = line.split("=");
                if (parts.length != 2)
                    throw new IllegalStateException(
                            String.format("Bad configuration entry '%s' on line %d.", line, lineNumber));

                options.put(parts[0].trim(), parts[1].trim());
            }

            lineNumber++;
        }

        return options;
    }
}
