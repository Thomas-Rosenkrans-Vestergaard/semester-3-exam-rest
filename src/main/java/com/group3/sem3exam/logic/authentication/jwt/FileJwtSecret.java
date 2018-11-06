package com.group3.sem3exam.logic.authentication.jwt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class FileJwtSecret implements JwtSecret
{

    /**
     * The location of the save file storing the JWT secret.
     */
    private final File saveFile;

    /**
     * The source of randomness the secret is generated from.
     */
    private static SecureRandom random = new SecureRandom();

    /**
     * The secret value.
     */
    private String secret;

    /**
     * Creates a new {@link FileJwtSecret}.
     *
     * @param saveFile The file to read and write the secret to.
     * @throws IOException When the file cannot be read from.
     */
    public FileJwtSecret(File saveFile) throws IOException
    {
        this.saveFile = saveFile;
        this.secret = readFile();
    }

    /**
     * Creates a new JWT secret from the contents of the provided file. When the provided file is empty, a new base64
     * secret of the provided size is generated.
     *
     * @param saveFile The file to store the base64 secret in.
     */
    public FileJwtSecret(File saveFile, int bytes) throws Exception
    {
        this.saveFile = saveFile;
        if (saveFile.length() > 0)
            this.secret = readFile();
        else
            regenerate(bytes);
    }

    /**
     * Reads the save file, returning its contents.
     *
     * @return The contents of the file.
     * @throws IOException When en error prevents the file from being read.
     */
    private String readFile() throws IOException
    {
        Scanner stream = new Scanner(new FileInputStream(saveFile));
        if (!stream.hasNextLine())
            return "";
        String contents = stream.nextLine();
        stream.close();
        return contents;
    }

    /**
     * Returns the secret string value.
     *
     * @return The secret string value.
     */
    @Override
    public String getValue()
    {
        return secret;
    }

    /**
     * Regenerates the Jwt secret.
     *
     * @param bytes The number of bytes the secret string should be generated from.
     * @return The newly generated value.
     * @throws JwtSecretGenerationException When a secret cannot be generated.
     */
    @Override
    public String regenerate(int bytes) throws JwtSecretGenerationException
    {
        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            byte[] byteArray = new byte[bytes];
            random.nextBytes(byteArray);
            this.secret = Base64.getEncoder().encodeToString(byteArray);
            fileWriter.append(this.secret);
            fileWriter.flush();
            return this.secret;
        } catch (IOException e) {
            throw new JwtSecretGenerationException(e);
        }
    }
}
