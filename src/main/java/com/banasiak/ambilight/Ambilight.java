package com.banasiak.ambilight;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import com.banasiak.ambilight.Config.Mode;
import com.banasiak.ambilight.Config.ScreenRegion;

public class Ambilight
{
    private static final int SERIAL_PORT_TIMEOUT = 2000;
    private static final int SERIAL_PORT_DATA_RATE = 9600;

    private static SerialPort serialPort = null;
    private static BufferedReader input = null;
    private static OutputStream output = null;

    static Config config = new Config();

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        final ScreenRegion regionOne = config.getRegion1();
        final ScreenRegion regionTwo = config.getRegion2();
        final int sampleResolution = config.getSampleResolution();

        if (!initializeSerialPort())
        {
            return;
        }

        try
        {
            // robot class for screen capturing
            final Robot robot = new Robot();

            // get the screen size
            final Dimension screenDimension = Toolkit.getDefaultToolkit()
                .getScreenSize();

            // create rectangles for the each screen region as specified in the
            // config file
            final Rectangle regionOneRectangle = createRectangle(
                screenDimension, regionOne);
            final Rectangle regionTwoRectangle = createRectangle(
                screenDimension, regionTwo);

            // variables to hold the RGB color values for each region
            Color regionOneColor = new Color(0, 0, 0);
            Color regionTwoColor = new Color(0, 0, 0);

            // main loop that samples the screen colors and writes the values to
            // the serial port
            while (true)
            {
                // sample the screen regions calculate the average color
                if (config.getMode() == Mode.DYNAMIC)
                {
                    regionOneColor = sampleRectangle(robot, regionOneRectangle,
                        sampleResolution);
                    regionTwoColor = sampleRectangle(robot, regionTwoRectangle,
                        sampleResolution);
                }

                // read the custom colors from the config file and use those
                if (config.getMode() == Mode.MANUAL)
                {
                    final int red = config.getCustomRedValue();
                    final int green = config.getCustomGreenValue();
                    final int blue = config.getCustomBlueValue();

                    regionOneColor = new Color(red, green, blue);
                    regionTwoColor = new Color(red, green, blue);
                }

                if (config.getDebug())
                {
                    System.out.println("Region: " + regionOne.name()
                        + " | Color: " + regionOneColor.toString());
                    System.out.println("Region: " + regionTwo.name()
                        + " | Color: " + regionTwoColor.toString());
                    System.out.println("");
                }

                // write the appropriate region colors to the serial port
                writeColorsToSerial(regionOneColor, regionTwoColor);

                // give the system a breather
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
        catch (final IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static void writeColorsToSerial(Color regionOneColor,
        Color regionTwoColor) throws IOException
    {
        // write marker for synchronization
        output.write(0xff);

        // RGB colors for left region
        output.write(regionOneColor.getRed());
        output.write(regionOneColor.getGreen());
        output.write(regionOneColor.getBlue());

        // RGB colors for right region
        output.write(regionTwoColor.getRed());
        output.write(regionTwoColor.getGreen());
        output.write(regionTwoColor.getBlue());
    }

    private static boolean initializeSerialPort()
    {
        final String serialPortName = config.getSerialPort();
        CommPortIdentifier portId = null;
        final Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier
            .getPortIdentifiers();

        while (portEnum.hasMoreElements())
        {
            final CommPortIdentifier currentPortId = portEnum.nextElement();
            if (currentPortId.getName().equals(serialPortName))
            {
                portId = currentPortId;
                break;
            }
        }

        if (portId == null)
        {
            System.out.println("Could not find COM port: " + serialPortName);
            return false;
        }

        try
        {
            serialPort = (SerialPort) portId.open("Ambilight",
                SERIAL_PORT_TIMEOUT);
            serialPort.setSerialPortParams(SERIAL_PORT_DATA_RATE,
                SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

            output = serialPort.getOutputStream();

        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }

        return true;
    }

    public synchronized void closeSerialPort()
    {
        if (serialPort != null)
        {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    private static Rectangle createRectangle(Dimension screenDimension,
        ScreenRegion region)
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
                rectangle = new Rectangle(halfWidth + 1, 0, halfWidth, height);
            break;
            case TOP:
                rectangle = new Rectangle(0, 0, width, halfHeight);
            break;
            case BOTTOM:
                rectangle = new Rectangle(0, halfHeight + 1, width, halfHeight);
            break;
            case FULL:
                rectangle = new Rectangle(0, 0, width, height);
            break;
            case DISABLED:
                rectangle = null;
            break;

        }

        return rectangle;
    }

    private static Color sampleRectangle(final Robot robot, Rectangle region,
        int sampleResolution)
    {
        int red = 0;
        int green = 0;
        int blue = 0;

        // if the region rectangle is null, disable the LEDs by returning black
        if (region == null)
        {
            return new Color(red, green, blue);
        }

        // otherwise, capture the screen region and sample the pixel colors
        final BufferedImage grid = robot.createScreenCapture(region);
        final Raster raster = grid.getData();
        int rgbArray[] = null;

        for (int x = 0; x < raster.getWidth(); x = x + sampleResolution)
        {
            for (int y = 0; y < raster.getHeight(); y = y + sampleResolution)
            {
                rgbArray = raster.getPixel(x, y, rgbArray);
                red = red + rgbArray[0];
                green = green + rgbArray[1];
                blue = blue + rgbArray[2];
            }
        }

        // average color while compensating for the sampling resolution
        final int sampleArea = raster.getWidth() * raster.getHeight();
        final int resolutionArea = sampleArea
            / (sampleResolution * sampleResolution);

        red = red / resolutionArea;
        green = green / resolutionArea;
        blue = blue / resolutionArea;

        return new Color(red, green, blue);
    }
}
