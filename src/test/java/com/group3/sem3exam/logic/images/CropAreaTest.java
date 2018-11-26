package com.group3.sem3exam.logic.images;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CropAreaTest
{

    int getRandom()
    {
        return new Random().nextInt(10_000) + 1;
    }

    @Test
    void getX()
    {
        int      expected = getRandom();
        CropArea crop     = new CropArea(expected, 0, 0, 0);
        assertEquals(expected, crop.getX());
    }

    @Test
    void getY()
    {
        int      expected = getRandom();
        CropArea crop     = new CropArea(0, expected, 0, 0);
        assertEquals(expected, crop.getY());
    }

    @Test
    void getWidth()
    {
        int      expected = getRandom();
        CropArea crop     = new CropArea(0, 0, expected, 0);
        assertEquals(expected, crop.getWidth());
    }

    @Test
    void getHeight()
    {
        int      expected = getRandom();
        CropArea crop     = new CropArea(0, 0, 0, expected);
        assertEquals(expected, crop.getHeight());
    }

    @Test
    void getRightEdge()
    {
        int      x     = getRandom();
        int      width = getRandom();
        CropArea crop  = new CropArea(x, 0, width, 0);
        assertEquals(x + width, crop.getRightEdge());
    }

    @Test
    void getBottomEdge()
    {
        int      y      = getRandom();
        int      height = getRandom();
        CropArea crop   = new CropArea(0, y, 0, height);
        assertEquals(y + height, crop.getBottomEdge());
    }
}