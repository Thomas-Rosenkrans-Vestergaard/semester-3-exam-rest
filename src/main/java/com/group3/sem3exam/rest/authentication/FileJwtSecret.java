package com.group3.sem3exam.rest.authentication;

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

    private static SecureRandom random = new SecureRandom();

    /**
     * The secret value.
     */
    private String secret;

    public FileJwtSecret(File saveFile, boolean regenerate)
    {
        this.saveFile = saveFile;
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

    private String readFile() throws IOException
    {

        Scanner stream   = new Scanner(new FileInputStream(saveFile));
        String  contents = stream.nextLine();
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
     * @return The old secret value.
     */
    @Override
    public String regenerate(int bytes) throws Exception
    {
        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            byte[] byteArray = new byte[bytes];
            random.nextBytes(byteArray);
            String retired = this.secret;
            this.secret = Base64.getEncoder().encodeToString(byteArray);
            fileWriter.append(this.secret);
            fileWriter.flush();
            return retired;
        }
    }
}
