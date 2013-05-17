package com.banasiak.ambilight.input;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class SampleRectangle {
	private static SampleRectangle singleton = new SampleRectangle();
	
	public static Color helperSampleRectangle(final Robot robot, 
			  							      Rectangle region,
			  							      final int sampleResolution) {
		return singleton.sampleRectangle(robot, region, sampleResolution);
	}
	

	private void debug(String s) {
		// nothing
	}
	
	private Color emptyInput = new Color(0, 0, 0); // i.e. all black
	private Color emptyInput() {
		return emptyInput;
	}
	
	
	public  Color sampleRectangle(final Robot robot, 
								  Rectangle region,
								  final int sampleResolution) {

		// if the region rectangle is null, disable the LEDs by returning black
		if (region == null) {
			return emptyInput();
		}

		// otherwise, capture the screen region and sample the pixel colors
		final BufferedImage grid = robot.createScreenCapture(region);
		final Raster raster = grid.getData();

		return sampleRaster(raster, sampleResolution);
	}
	
	public Color sampleRaster(Raster raster) {
		if (raster == null) {
			return emptyInput();
		}
		// see comments below why this doesn't work out -- i.e. it only works for square input regions
		return sampleRaster(raster, Math.min(raster.getHeight(), raster.getWidth()));
	}
	
	public Color sampleRaster(Raster raster, 
			                  final int sampleResolution) {
		
		if (raster == null) {
			return emptyInput();
		}
		
		long red = 0;
		long green = 0;
		long blue = 0;

		int rgbArray[] = null;

		for (int x = 0; x < raster.getWidth(); x = x + sampleResolution) {
			for (int y = 0; y < raster.getHeight(); y = y + sampleResolution) {
				rgbArray = raster.getPixel(x, y, rgbArray);
				red = red + rgbArray[0];
				green = green + rgbArray[1];
				blue = blue + rgbArray[2];
			}
		}

		// TT: ok, this does not make much sense
		// TT: "red" now contains the SUM of all the red RGB values
		// TT: so,   red / totalNumberOfPixels  == red / totalArea    ==   average red  ...  good
		// TT: but,  the formula below decreases the value of 'totalNumberOfPixels'
		// TT:   i.e. it changes red/1000 into red/100 - i.e. things just get "more red"
		// TT: wouldn't it be more straight-forward to have a tuple of
		// TT:     (times-red, times-green, times-blue) to compensate to make it "more"???
		
		// average color while compensating for the sampling resolution
		final int totalArea = raster.getWidth() * raster.getHeight();
		final int sampleArea = (sampleResolution * sampleResolution);
		final int resolutionArea = totalArea / sampleArea;
		debug("total=" + totalArea + " sample=" + sampleArea + " resolution=" + resolutionArea);
		debug("red=" + red + " green=" + green + " blue=" + blue);

		int outRed   = ( (int) (red / resolutionArea));
		int outGreen = ( (int) (green / resolutionArea));
		int outBlue  = ( (int) (blue / resolutionArea));

		debug("outRed=" + outRed + " outGreen=" + outGreen + " outBlue=" + outBlue);
		
		return new Color(outRed, outGreen, outBlue);
	}
}
