package com.banasiak.ambilight;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;


public class Ambilight
{
    private enum ScreenRegion
    {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        final int sampleResolution = 2;

        try
        {
            final Robot robot = new Robot();
            final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

            final Rectangle regionLeftRectangle = createRectangle(screenDimension, ScreenRegion.LEFT);
            final Rectangle regionRightRectangle = createRectangle(screenDimension, ScreenRegion.RIGHT);

            Color regionLeftColor;
            Color regionRightColor;

            while(true)
            {
                regionLeftColor = sampleRectangle(robot, regionLeftRectangle, sampleResolution);
                System.out.println("Region: LEFT | Color: " + regionLeftColor.toString());

                regionRightColor = sampleRectangle(robot, regionRightRectangle, sampleResolution);
                System.out.println("Region: RIGHT | Color: " + regionRightColor.toString());

                System.out.println("");
                Thread.sleep(10);
            }


        }
        catch (final AWTException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (final InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private static Rectangle createRectangle(Dimension screenDimension, ScreenRegion region)
    {

        final int width = (int) screenDimension.getWidth();
        final int height = (int) screenDimension.getHeight();
        final int halfWidth = width / 2;
        final int halfHeight = height / 2;

        Rectangle rectangle = null;

        switch (region)
        {
            case LEFT:
                rectangle = new Rectangle(0, 0, halfWidth, height);
                break;
            case RIGHT:
                rectangle = new Rectangle(halfWidth+1, 0, halfWidth, height);
                break;
            case TOP:
                rectangle = new Rectangle(0, 0, width, halfHeight);
                break;
            case BOTTOM:
                rectangle = new Rectangle(0, halfHeight+1, width, halfHeight);
                break;

        }

        return rectangle;
    }

    private static Color sampleRectangle(final Robot robot, Rectangle region, int sampleResolution)
    {
        int red=0;
        int green=0;
        int blue=0;

        final BufferedImage grid = robot.createScreenCapture(region);
        final Raster raster = grid.getData();
        int rgbArray[] = null;

        for (int x=0; x < raster.getWidth(); x = x+sampleResolution)
        {
            for ( int y=0; y < raster.getHeight(); y = y+sampleResolution)
            {
                rgbArray = raster.getPixel(x, y, rgbArray);
                red = red + rgbArray[0];
                green = green + rgbArray[1];
                blue = blue + rgbArray[2];
            }
        }

        // average color
        final int sampleArea = raster.getWidth() * raster.getHeight();
        final int resolutionArea = sampleArea/sampleResolution;

        red = red / resolutionArea;
        green = green / resolutionArea;
        blue = blue / resolutionArea;

        return new Color(red, green, blue);
    }

}
