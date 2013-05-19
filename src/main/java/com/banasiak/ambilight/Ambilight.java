package com.banasiak.ambilight;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;

import com.banasiak.ambilight.Config.Mode;
import com.banasiak.ambilight.Config.ScreenRegion;
import com.banasiak.ambilight.api.AmbilightException;
import com.banasiak.ambilight.api.ContextProvider;
import com.banasiak.ambilight.model.EmptyContext;
import com.banasiak.ambilight.model.TwoChannelColorData;
import com.banasiak.ambilight.sink.AmbilightSink;
import com.banasiak.ambilight.sink.AmbilightSinkContext;
import com.banasiak.ambilight.sink.DebugFilter;
import com.banasiak.ambilight.sink.IntensityAdjustFilter;
import com.banasiak.ambilight.sink.NoOpSink;
import com.banasiak.ambilight.sink.SerialPortSink;
import com.banasiak.ambilight.source.ConstantSource;
import com.banasiak.ambilight.source.SampleRectangleSource;

public class Ambilight
{

	private static Config config = new Config();

    private static AmbilightSink getDebugFilter() 
    {
    	if (config.getDebug()) 
    	{
    		return new DebugFilter(System.out);
    	}
    	else
    	{
    		return new NoOpSink();
    	}
    }
    private static AmbilightSink getIntensityAdjustFilter() 
    {
        double redIntensity = config.getRedIntensity() / 100.0;
        double greenIntensity = config.getGreenIntensity() / 100.0;
        double blueIntensity = config.getBlueIntensity() / 100.0;
        return new IntensityAdjustFilter(redIntensity, greenIntensity, blueIntensity);
    }
    
    private static AmbilightSink getSerialPortSink() 
    {
    	final String portName = config.getSerialPort();
    	try 
    	{
    		return new SerialPortSink(portName);
    	} 
    	catch (AmbilightException e) 
    	{
    		System.err.println("Failed to initialize serial port(" + portName + ").  Skipping.");
    		return new NoOpSink();
    	}
    }
    private static ConstantSource getConstantSource()
    {
    	final int red = config.getCustomRedValue();
        final int green = config.getCustomGreenValue();
        final int blue = config.getCustomBlueValue();
        
        final Color color = new Color(red, green, blue);
        
        return new ConstantSource(color, color);
    }
    private static SampleRectangleSource getSampleRectangleSource() 
    {
        final ScreenRegion regionOne = config.getRegion1();
        final ScreenRegion regionTwo = config.getRegion2();
        final int sampleStepSize = config.getSampleResolution();

		try {
	        // robot class for screen capturing
			Robot robot = new Robot();
	        // get the screen size
	        final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
	 
	        return new SampleRectangleSource(robot, screenDimension, regionOne, regionTwo, sampleStepSize);
			
		} catch (AWTException e) {
			throw new AmbilightException("Failed to initialize screen sample source", e);
		}

    }
    /**
     * @param args
     */
    public static void main(String[] args)
    {

        AmbilightSink debugFilter = getDebugFilter();
        AmbilightSink intensityAdjustFilter = getIntensityAdjustFilter();
        AmbilightSink serialPortSink = getSerialPortSink();

        SampleRectangleSource sourceScreen = getSampleRectangleSource();
        ConstantSource sourceConstant = getConstantSource();

        ContextProvider<EmptyContext> sourceContextProvider = null;
        ContextProvider<AmbilightSinkContext> sinkContextProvider = null;
        
        try
        {
            // main loop that samples the screen colors and writes the values to
            // the serial port
            while (true)
            {
            	TwoChannelColorData channelData;
            	
                // sample the screen regions calculate the average color
                if (config.getMode() == Mode.DYNAMIC)
                {
                	channelData = sourceScreen.generate(sourceContextProvider);
                }
                // read the custom colors from the config file and use those
                else  if (config.getMode() == Mode.MANUAL)
                {
                	channelData = sourceConstant.generate(sourceContextProvider);
                }
                else 
                {
                	throw new AmbilightException("config.getMode() illegal state: " + config.getMode());
                }
                
                // Slam it through all of the filter/sinks:
                // TODO: wire together more generically, create a filter chain helper, etc.:
                channelData = intensityAdjustFilter.accept(channelData, sinkContextProvider);
                channelData = debugFilter.accept(channelData, sinkContextProvider);
                channelData = serialPortSink.accept(channelData, sinkContextProvider);
                
                // give the system a breather
                Thread.sleep(config.getSleepMillis());
            }

        }
        catch (final AmbilightException e)
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
}
