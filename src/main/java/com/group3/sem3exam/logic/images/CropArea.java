package com.group3.sem3exam.logic.images;

import java.util.Objects;

/**
 * Represents an area to crop.
 */
public class CropArea
{

    /**
     * The x-coordinate of the top-left area.
     */
    private final int x;

    /**
     * The y-coordinate of the top-left area.
     */
    private final int y;

    /**
     * The width of the crop area.
     */
    private final int width;

    /**
     * The height of the crop area.
     */
    private final int height;

    /**
     * Creates a new {@link CropArea}.
     *
     * @param x      The x-coordinate of the top-left area.
     * @param y      The y-coordinate of the top-left area.
     * @param width  The width of the crop area.
     * @param height The height of the crop area.
     */
    public CropArea(int x, int y, int width, int height)
    {
        if (x < 0)
            throw new IllegalArgumentException("x cannot be < 0");
        if (y < 0)
            throw new IllegalArgumentException("y cannot be < 0");

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the x-coordinate of the top-left area.
     *
     * @return The x-coordinate of the top-left area.
     */
    public int getX()
    {
        return this.x;
    }

    /**
     * Returns the y-coordinate of the top-left area.
     *
     * @return The y-coordinate of the top-left area.
     */
    public int getY()
    {
        return this.y;
    }

    /**
     * Returns the width of the crop area.
     *
     * @return The width of the crop area.
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Returns the height of the crop area.
     *
     * @return The height of the crop area.
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Returns the x-coordinate of the rightmost edge of the crop area.
     *
     * @return The x-coordinate of the rightmost edge of the crop area.
     */
    public int getRightEdge()
    {
        return this.width + this.x;
    }

    /**
     * Returns the y-coordinate of the bottommost edge of the crop area.
     *
     * @return The y-coordinate of the bottommost edge of the crop area.
     */
    public int getBottomEdge()
    {
        return this.height + this.y;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof CropArea)) return false;
        CropArea cropArea = (CropArea) o;
        return getX() == cropArea.getX() &&
               getY() == cropArea.getY() &&
               getWidth() == cropArea.getWidth() &&
               getHeight() == cropArea.getHeight();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public String toString()
    {
        return "CropArea{" +
               "x=" + x +
               ", y=" + y +
               ", width=" + width +
               ", height=" + height +
               '}';
    }
}
