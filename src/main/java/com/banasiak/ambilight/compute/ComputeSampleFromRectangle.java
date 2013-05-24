package com.banasiak.ambilight.compute;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ComputeSampleFromRectangle {
	private static ComputeSampleFromRectangle singleton = new ComputeSampleFromRectangle();
	
	public static Color helperSampleRectangle(final Robot robot, 
			  							      Rectangle region,
			  							      final int sampleResolution) {
		return singleton.sampleRectangle(robot, region, sampleResolution);
	}
	

	private void debug(String s) {
		//System.out.println(s);
	}
	
	private Color emptyInput = new Color(0, 0, 0); // i.e. all black
	private Color emptyInput() {
		return emptyInput;
	}

	public Color sampleRaster(Raster raster) {
		if (raster == null) {
			return emptyInput();
		}
		// see comments below why this doesn't work out -- i.e. it only works for square input regions
		return sampleRaster(raster, Math.min(raster.getHeight(), raster.getWidth()));
	}
	

	public Color sampleRaster(final Raster raster, final Rectangle rectangle, final int sampleStepSize) 
	{
		return sampleRaster(subraster(raster, rectangle), sampleStepSize);
	}
	public Color sampleRaster(final BufferedImage grid, final Rectangle rectangle, final int sampleStepSize) 
	{
		return sampleRaster(subimage(grid, rectangle).getData(), sampleStepSize);
	}
	private static Raster subraster(final Raster raster, final Rectangle r)
	{
		return raster.createChild(r.x, r.y, r.width, r.height, 0, 0, null);
	}
	private static BufferedImage subimage(final BufferedImage image, final Rectangle r) 
	{
		return image.getSubimage(r.x, r.y, r.width, r.height);
	}
	public  Color sampleRectangle(final Robot robot, 
								  Rectangle region,
								  final int sampleStepSize) {

		// if the region rectangle is null, disable the LEDs by returning black
		if (region == null) {
			return emptyInput();
		}

		// otherwise, capture the screen region and sample the pixel colors
		final BufferedImage grid = robot.createScreenCapture(region);
		final Raster raster = grid.getData();

		return sampleRaster(raster, sampleStepSize);
	}
	
	public Color sampleRaster(Raster raster, 
			                  final int sampleResolution) {
		
		if (raster == null) {
			return emptyInput();
		}
		
		long totalRed = 0;
		long totalGreen = 0;
		long totalBlue = 0;
		int totalSampleCount = 0;
		
		int rgbArray[] = null;
		int debugEvery = 9876;
		int specials = 5;
		
		for (int x = 0; x < raster.getWidth(); x = x + sampleResolution) {
			for (int y = 0; y < raster.getHeight(); y = y + sampleResolution) {
				rgbArray = raster.getPixel(x, y, rgbArray);
				int red = rgbArray[0];
				int green = rgbArray[1];
				int blue = rgbArray[2];
				
				totalRed = totalRed + red;
				totalGreen = totalGreen + green;
				totalBlue = totalBlue + blue;
				totalSampleCount++;
				boolean every = (totalSampleCount % debugEvery) == 0;
				boolean special = false;
				if (specials > 0)
				{
					if ((x > 200) && (y > 200)) 
					{
						if ((red+green+blue) > 100)
						{
							special = true;
							specials--;
						}
					}
				}
				boolean debug = every || special;
				if (debug)
				{
					debug(((special) ? "###1### " : "") +
							"[" + x + "," + y + "] pixel is " + "[r=" + rgbArray[0] + ",g=" + rgbArray[1] + ",b=" + rgbArray[2] + "]");
				}
			}
		}

		debug("totalRed=" + totalRed + " totalGreen=" + totalGreen + " totalBlue=" + totalBlue + " totalSamples=" + totalSampleCount);
		
		// average color = total color / total number of samples actually taken

		int outRed   = ( (int) (totalRed / totalSampleCount));
		int outGreen = ( (int) (totalGreen / totalSampleCount));
		int outBlue  = ( (int) (totalBlue / totalSampleCount));

		debug("outRed=" + outRed + " outGreen=" + outGreen + " outBlue=" + outBlue);
		
		return new Color(outRed, outGreen, outBlue);
	}
}
