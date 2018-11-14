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
    private byte[] secret;

    /**
     * Creates a new {@link FileJwtSecret}.
     *
     * @param saveFile The file to read and write the secret to.
     * @throws IOException When the file cannot be read from.
     */
    public FileJwtSecret(File saveFile) throws IOException
    {
        this.saveFile = saveFile;
        this.secret = readFileBytes();
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
            this.secret = readFileBytes();
        else
            regenerate(bytes);
    }

    /**
     * Reads the save file, returning its contents.
     *
     * @return The contents of the file.
     * @throws IOException When en error prevents the file from being read.
     */
    private byte[] readFileBytes() throws IOException
    {
        Scanner stream = new Scanner(new FileInputStream(saveFile));
        if (!stream.hasNextLine())
            return new byte[0];
        String contents = stream.nextLine();
        stream.close();
        return Base64.getDecoder().decode(contents);
    }

    /**
     * Returns the secret string value.
     *
     * @return The secret byte value.
     */
    @Override
    public byte[] getValue()
    {
        return secret;
    }

    /**
     * Regenerates the Jwt secret.
     *
     * @param bytes The number of bytes the secret string should be generated from.
     * @return The newly generated secret value.
     * @throws JwtSecretGenerationException When a secret cannot be generated.
     */
    @Override
    public byte[] regenerate(int bytes) throws JwtSecretGenerationException
    {
        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            byte[] byteArray = new byte[bytes];
            random.nextBytes(byteArray);
            this.secret = byteArray;
            fileWriter.append(Base64.getEncoder().encodeToString(this.secret));
            fileWriter.flush();
            return this.secret;
        } catch (IOException e) {
            throw new JwtSecretGenerationException(e);
        }
    }
}
