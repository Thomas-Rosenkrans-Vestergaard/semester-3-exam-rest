package com.group3.sem3exam.facades;

import com.group3.sem3exam.rest.exceptions.ImageTypeException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;
import java.util.Base64;


public class DataUriEncoder
{

    public String bytesToDataURI (byte[] data) throws IOException {
        String mimeType = getMimeType(data);
        String extension = getExtension(mimeType);
        return String.format("data:%s;base64,%s", mimeType, Base64.getEncoder().encodeToString(data));
    }


    private String getMimeType(byte[] data) throws IOException{
        InputStream is       = new ByteArrayInputStream(data);
        return URLConnection.guessContentTypeFromStream(is);

    }

    private String getExtension(String mime){
        return mime.substring(mime.indexOf("/") + 2);
    }

    public String findFileExtension(byte[] data) throws IOException, ImageTypeException
    {
    InputStream is       = new ByteArrayInputStream(data);
    String      mimeType = URLConnection.guessContentTypeFromStream(is);
    if(mimeType.contains("image/")){
        //mimeType.replace("image/", "");
    }else{
        throw new ImageTypeException();
    }
    //...close stream
        is.close();
    return mimeType;
    }

}

