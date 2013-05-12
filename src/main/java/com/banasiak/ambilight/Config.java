package com.banasiak.ambilight;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config
{
    private static String CONFIG_FILE = "config.properties";

    private static String MODE_KEY = "mode";
    private static String SERIAL_PORT_KEY = "serialPort";
    private static String SAMPLE_RESOLUTION_KEY = "sampleResolution";
    private static String CUSTOM_RED_KEY = "red";
    private static String CUSTOM_GREEN_KEY = "green";
    private static String CUSTOM_BLUE_KEY = "blue";

    private String mode = "DYNAMIC";
    private String serialPort = "/dev/ttyACM0";
    private int sampleResolution = 4;
    private int customRedValue = 127;
    private int customGreenValue = 127;
    private int customBlueValue = 127;

    private static Properties prop = new Properties();

    public Config()
    {
        prop = readProperties(CONFIG_FILE);

        this.mode = prop.getProperty(MODE_KEY);
        this.serialPort = prop.getProperty(SERIAL_PORT_KEY);
        this.sampleResolution = Integer.parseInt(prop.getProperty(SAMPLE_RESOLUTION_KEY));
        this.customRedValue = Integer.parseInt(prop.getProperty(CUSTOM_RED_KEY));
        this.customGreenValue = Integer.parseInt(prop.getProperty(CUSTOM_GREEN_KEY));
        this.customBlueValue = Integer.parseInt(prop.getProperty(CUSTOM_BLUE_KEY));
    }

    public Properties readProperties(String propFile)
    {
        try
        {
            prop.load(new FileInputStream(propFile));
        }
        catch (final IOException e)
        {
            // can't load properties file. doesn't exist?
            prop.setProperty(MODE_KEY, this.mode);
            prop.setProperty(SERIAL_PORT_KEY, this.serialPort);
            prop.setProperty(SAMPLE_RESOLUTION_KEY, String.valueOf(this.sampleResolution));
            prop.setProperty(CUSTOM_RED_KEY, String.valueOf(this.customRedValue));
            prop.setProperty(CUSTOM_GREEN_KEY, String.valueOf(this.customGreenValue));
            prop.setProperty(CUSTOM_BLUE_KEY, String.valueOf(this.customBlueValue));

            writeProperties(prop);
        }

        System.out.println(MODE_KEY + "=" + this.mode);
        System.out.println(SERIAL_PORT_KEY + "=" + this.serialPort);
        System.out.println(SAMPLE_RESOLUTION_KEY + "=" + this.sampleResolution);
        System.out.println(CUSTOM_RED_KEY + "=" + this.customRedValue);
        System.out.println(CUSTOM_GREEN_KEY + "=" + this.customGreenValue);
        System.out.println(CUSTOM_BLUE_KEY + "=" + this.customBlueValue);

        return prop;
    }

    public void writeProperties(Properties prop)
    {
        try
        {
            prop.store(new FileOutputStream(CONFIG_FILE), null);
        }
        catch (final IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //getters
    public String getMode()
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

    //setters
    public void setMode(String mode)
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

}
