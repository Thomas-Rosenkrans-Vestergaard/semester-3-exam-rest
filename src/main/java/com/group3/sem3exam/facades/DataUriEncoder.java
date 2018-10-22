package com.group3.sem3exam.facades;

import com.group3.sem3exam.rest.exceptions.ImageTypeException;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;


public class DataUriEncoder
{


    public static String encode(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imageString = "data:image/png;base64," +
                             DatatypeConverter.printBase64Binary(baos.toByteArray());
        return imageString;
    }


    public static String findFileExtension(byte[] data) throws IOException, ImageTypeException
    {
    InputStream is       = new ByteArrayInputStream(data);
    String      mimeType = URLConnection.guessContentTypeFromStream(is);
    if(mimeType.contains("image/")){
        mimeType.replace("image/", "");
    }else{
        throw new ImageTypeException();
    }
    //...close stream
        is.close();
    }



    //maybe make class methods private
    public static BufferedImage ByteArrayToImage(byte[] data) throws IOException // needs to have better exception
    {
            BufferedImage bImage = ImageIO.read(new File("sample.jpg"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos );
                         data    = bos.toByteArray();
            ByteArrayInputStream bis     = new ByteArrayInputStream(data);
            BufferedImage        bImage2 = ImageIO.read(bis);
            ImageIO.write(bImage2, "jpg", new File("output.jpg") );
            return bImage2;
        }
    }

