package com.banasiak.ambilight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config
{
    private static String CONFIG_FILE = "config.properties";

    // defaults
    private Mode mode = Mode.DYNAMIC;
    private String serialPort = "/dev/ttyACM0";
    private int sampleResolution = 4;
    private ScreenRegion region1 = ScreenRegion.LEFT;
    private ScreenRegion region2 = ScreenRegion.RIGHT;
    private int redIntensity = 100;
    private int greenIntensity = 100;
    private int blueIntensity = 100;
    private int customRedValue = 127;
    private int customGreenValue = 127;
    private int customBlueValue = 127;
    private boolean debug = false;

    private static String MODE_KEY = "mode";
    private static String SERIAL_PORT_KEY = "serialPort";
    private static String SAMPLE_RESOLUTION_KEY = "sampleResolution";
    private static String REGION_ONE_KEY = "region1";
    private static String REGION_TWO_KEY = "region2";
    private static String RED_INTENSITY_KEY = "redIntensity";
    private static String GREEN_INTENSITY_KEY = "greenIntensity";
    private static String BLUE_INTENSITY_KEY = "blueIntensity";
    private static String CUSTOM_RED_KEY = "customRed";
    private static String CUSTOM_GREEN_KEY = "customGreen";
    private static String CUSTOM_BLUE_KEY = "customBlue";
    private static String DEBUG_KEY = "debug";

    public enum ScreenRegion
    {
        LEFT, RIGHT, TOP, BOTTOM, FULL, DISABLED
    }

    public enum Mode
    {
        DYNAMIC, MANUAL
    }

    private static Properties prop = new Properties();

    public Config()
    {
        prop = readProperties(CONFIG_FILE);

        this.mode = Mode.valueOf(prop.getProperty(MODE_KEY));
        this.serialPort = prop.getProperty(SERIAL_PORT_KEY);
        this.sampleResolution = Integer.parseInt(prop
            .getProperty(SAMPLE_RESOLUTION_KEY));
        this.region1 = ScreenRegion.valueOf(prop.getProperty(REGION_ONE_KEY));
        this.region2 = ScreenRegion.valueOf(prop.getProperty(REGION_TWO_KEY));
        this.redIntensity = Integer.parseInt(prop.getProperty(RED_INTENSITY_KEY));
        this.greenIntensity = Integer.parseInt(prop.getProperty(GREEN_INTENSITY_KEY));
        this.blueIntensity = Integer.parseInt(prop.getProperty(BLUE_INTENSITY_KEY));
        this.customRedValue = Integer
            .parseInt(prop.getProperty(CUSTOM_RED_KEY));
        this.customGreenValue = Integer.parseInt(prop
            .getProperty(CUSTOM_GREEN_KEY));
        this.customBlueValue = Integer.parseInt(prop
            .getProperty(CUSTOM_BLUE_KEY));
        this.debug = Boolean.valueOf(prop.getProperty(DEBUG_KEY));

        if (debug)
        {
            System.out.println(MODE_KEY + "=" + this.mode.name());
            System.out.println(SERIAL_PORT_KEY + "=" + this.serialPort);
            System.out.println(SAMPLE_RESOLUTION_KEY + "="
                + this.sampleResolution);
            System.out.println(REGION_ONE_KEY + "=" + this.region1.name());
            System.out.println(REGION_TWO_KEY + "=" + this.region2.name());
            System.out.println(RED_INTENSITY_KEY + "=" + this.redIntensity);
            System.out.println(GREEN_INTENSITY_KEY + "=" + this.greenIntensity);
            System.out.println(BLUE_INTENSITY_KEY + "=" + this.blueIntensity);
            System.out.println(CUSTOM_RED_KEY + "=" + this.customRedValue);
            System.out.println(CUSTOM_GREEN_KEY + "=" + this.customGreenValue);
            System.out.println(CUSTOM_BLUE_KEY + "=" + this.customBlueValue);
            System.out.println(DEBUG_KEY + "=" + this.debug);
            System.out.println("");
        }
    }

    public Properties readProperties(String propFile)
    {
        try
        {
            prop.load(new FileInputStream(propFile));
        }
        catch (final FileNotFoundException e)
        {
            // file doesn't exist, make a new one with defaults
            System.out.println(CONFIG_FILE
                + " not found.  Creating with default values.");
            writeProperties(prop);
        }
        catch (final IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return prop;
    }

    public void writeProperties(Properties prop)
    {
        try
        {
            prop.setProperty(MODE_KEY, this.mode.name());
            prop.setProperty(SERIAL_PORT_KEY, this.serialPort);
            prop.setProperty(SAMPLE_RESOLUTION_KEY,
                String.valueOf(this.sampleResolution));
            prop.setProperty(RED_INTENSITY_KEY, String.valueOf(this.redIntensity));
            prop.setProperty(GREEN_INTENSITY_KEY, String.valueOf(this.greenIntensity));
            prop.setProperty(BLUE_INTENSITY_KEY, String.valueOf(this.blueIntensity));
            prop.setProperty(REGION_ONE_KEY, this.region1.name());
            prop.setProperty(REGION_TWO_KEY, this.region2.name());
            prop.setProperty(CUSTOM_RED_KEY,
                String.valueOf(this.customRedValue));
            prop.setProperty(CUSTOM_GREEN_KEY,
                String.valueOf(this.customGreenValue));
            prop.setProperty(CUSTOM_BLUE_KEY,
                String.valueOf(this.customBlueValue));
            prop.setProperty(DEBUG_KEY, String.valueOf(this.debug));

            prop.store(new FileOutputStream(CONFIG_FILE), null);
        }
        catch (final IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // getters
    public Mode getMode()
    {
        return mode;
    }

    public String getSerialPort()
    {
        return serialPort;
    }

    public int getSampleResolution()
    {
        return sampleResolution;
    }

    public ScreenRegion getRegion1()
    {
        return region1;
    }

    public ScreenRegion getRegion2()
    {
        return region2;
    }

    public int getRedIntensity()
    {
        return redIntensity;
    }

    public int getGreenIntensity()
    {
        return greenIntensity;
    }

    public int getBlueIntensity()
    {
        return blueIntensity;
    }

    public int getCustomRedValue()
    {
        return customRedValue;
    }

    public int getCustomGreenValue()
    {
        return customGreenValue;
    }

    public int getCustomBlueValue()
    {
        return customBlueValue;
    }

    public boolean getDebug()
    {
        return debug;
    }

    // setters
    public void setMode(Mode mode)
    {
        this.mode = mode;
    }

    public void setSerialPort(String port)
    {
        this.serialPort = port;
    }

    public void setSampleResolution(int sampleResolution)
    {
        this.sampleResolution = sampleResolution;
    }

    public void setRegion1(ScreenRegion region1)
    {
        this.region1 = region1;
    }

    public void setRegion2(ScreenRegion region2)
    {
        this.region2 = region2;
    }

    public void setRedIntensity(int redIntensity)
    {
        this.redIntensity = redIntensity;
    }

    public void setGreenIntensity(int greenIntensity)
    {
        this.greenIntensity = greenIntensity;
    }

    public void setBlueIntensity(int blueIntensity)
    {
        this.blueIntensity = blueIntensity;
    }

    public void setCustomRedValue(int customRedValue)
    {
        this.customRedValue = customRedValue;
    }

    public void setCustomGreenValue(int customGreenValue)
    {
        this.customGreenValue = customGreenValue;
    }

    public void setCustomBlueValue(int customBlueValue)
    {
        this.customBlueValue = customBlueValue;
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

}
