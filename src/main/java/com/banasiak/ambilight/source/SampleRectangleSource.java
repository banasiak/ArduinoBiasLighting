package com.banasiak.ambilight.source;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import com.banasiak.ambilight.Config.ScreenRegion;
import com.banasiak.ambilight.api.AmbilightException;
import com.banasiak.ambilight.api.ChannelSource;
import com.banasiak.ambilight.api.ContextProvider;
import com.banasiak.ambilight.compute.ComputeSampleFromRectangle;
import com.banasiak.ambilight.model.EmptyContext;
import com.banasiak.ambilight.model.TwoChannelColorData;

public class SampleRectangleSource implements ChannelSource<TwoChannelColorData, EmptyContext>
{
	private final ComputeSampleFromRectangle compute;
	//private final Robot robot;
	private final RasterSource rasterSource;
	
	private final ScreenRegion firstRegion;
	private final ScreenRegion secondRegion;
	
	private final int sampleStepSize;

	// optimizations:
	private Rectangle previousRasterRectangle;
	private Rectangle firstRectangle;
	private Rectangle secondRectangle;
	
	public SampleRectangleSource(RasterSource rasterSource, ScreenRegion firstRegion, ScreenRegion secondRegion, int sampleStepSize) 
	{
		/* Robot robot, Dimension outerRectangle, */
		this.rasterSource = rasterSource;
		this.firstRegion = firstRegion;
		this.secondRegion = secondRegion;
		this.sampleStepSize = sampleStepSize;
				
		
		this.compute = new ComputeSampleFromRectangle();
	}
	
	private void computeandcacheRectangles(Raster raster)
	{
		Rectangle nowRectangle = raster.getBounds();
		if (nowRectangle.equals(previousRasterRectangle))
		{	
			return;
		}
		this.firstRectangle = this.firstRegion.createRectangle(nowRectangle);
		this.secondRectangle = this.secondRegion.createRectangle(nowRectangle);
		previousRasterRectangle = nowRectangle;
		
		System.out.println("First =" + this.firstRectangle);
		System.out.println("Second=" + this.secondRectangle);

	}
	@Override
	public TwoChannelColorData generate(ContextProvider<EmptyContext> contextProvider) 
	{
		Raster raster = rasterSource.getRaster();
		computeandcacheRectangles(raster);
		
		Color firstColor  = compute.sampleRaster(raster, firstRectangle, sampleStepSize);
		Color secondColor = compute.sampleRaster(raster, secondRectangle, sampleStepSize);
		
		TwoChannelColorData ret = new TwoChannelColorData(firstColor, secondColor);
		System.out.println("Source generated: " + ret);
		return ret;
	}

	
	public interface RasterSource
	{
		/**
		 * 
		 * @return the "Big Picture" raster - i.e. the source BEFORE a ScreenRegion has been applied to it.
		 */
		public Raster getRaster();
	}
	
	/**
	 * Implementation that creates the raster using a Robot.
	 * By default, you get the entire screen.
	 * 
	 *
	 */
	public static class RobotRasterSource implements RasterSource
	{
		private final Robot robot;
		private final Rectangle captureRectangle;
		
		public RobotRasterSource()
		{
			this(null, null);
		}
		
		public RobotRasterSource(final Rectangle captureRectangle, final Robot robot)
		{
			Rectangle assignCaptureRectangle = captureRectangle;
			if (assignCaptureRectangle == null)
			{
				assignCaptureRectangle = ScreenRegion.dimensionToRectangle(Toolkit.getDefaultToolkit().getScreenSize());
			}
			Robot assignRobot = robot;
			if (assignRobot == null)
			{
				try
				{
					assignRobot = new Robot();
				} 
				catch (AWTException e) {
					throw new AmbilightException("Failed to create robot", e);
				}
			}
			this.robot = assignRobot;
			this.captureRectangle = assignCaptureRectangle;
		}
		public Raster getRaster() 
		{
			// capture the screen region and sample the pixel colors
			final BufferedImage grid = robot.createScreenCapture(captureRectangle);
			
			final Raster raster = grid.getData();
			
			return raster;
		}
	}
}
