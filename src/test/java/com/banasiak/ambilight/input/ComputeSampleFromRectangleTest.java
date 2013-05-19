package com.banasiak.ambilight.input;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

import org.junit.Test;

import com.banasiak.ambilight.compute.ComputeSampleFromRectangle;

public class ComputeSampleFromRectangleTest {

	@Test
	public void testJustRed() {
		// given/when/then
		
		// given a rectangle with RGB(8, 0, 0) in every pixel
		ComputeSampleFromRectangle compute = new ComputeSampleFromRectangle();
		Raster raster = createFilledRaster(new Color(8, 0, 0));
		
		// when calling sample with sample size equal to the input size
		Color actual = compute.sampleRaster(raster);

		// then sample returns RGB(8, 0, 0)
		Color expected = new Color(8, 0, 0);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testJustReadSampleSize4() {
		// given a rectangle with RGB(8, 0, 0) in every pixel
		ComputeSampleFromRectangle compute = new ComputeSampleFromRectangle();
		Raster raster = createFilledRaster(new Color(8, 0, 0));
		
		// when calling sample with sample size equal to the default configuration value of 4
		Color actual = compute.sampleRaster(raster, 4);

		// then sample returns RGB(8, 0, 0)
		Color expected = new Color(8, 0, 0);
		
		assertEquals("filled color, sample size 4", expected, actual);		
	}
	
	
	@Test 
	public void testAllSingleColorRectangles() {
		subtestAllSingleColorRectangles(new Color(8, 0, 0));
		subtestAllSingleColorRectangles(new Color(0, 8, 0));
		subtestAllSingleColorRectangles(new Color(0, 0, 8));
		subtestAllSingleColorRectangles(new Color(50, 50, 50));
	}
	
	private void subtestAllSingleColorRectangles(final Color color) {
		ComputeSampleFromRectangle compute = new ComputeSampleFromRectangle();
		
		// given a rectangle filled will a single color 
		Raster raster = createFilledRaster(color);
		Color actual = compute.sampleRaster(raster);
		
		// then sample returns that color
		Color expected = color;
		assertEquals(expected, actual);
	}

	private Raster createFilledRaster(Color color) {
		return createFilledRaster(color, 5, 5);
	}
	private Raster createFilledRaster(Color color, int width, int height) {
		BufferedImage image =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.setColor(color);
		g2.fillRect(0, 0, width, height);
		return image.getRaster();
	}
	// it turns out to be easier just to stick with images when creating rasters!!!
	private Raster createFilledRaster_hard(Color color, int width, int height) {
		int bitMask[] = new int[]{0xff0000,0xff00,0xff,0xff000000};
		SinglePixelPackedSampleModel sampleModel = new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT,width,height,bitMask);
		
		WritableRaster writeRaster = Raster.createWritableRaster(sampleModel, null);
		// the order here has to match the order in bitMask:
		int[] sample = new int[] { color.getBlue(), color.getGreen(), color.getRed(), color.getAlpha() };
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				writeRaster.setDataElements(x, y, sample);
			}
		}
		return writeRaster;
		
	}
}
