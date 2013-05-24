package com.banasiak.ambilight;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.junit.Test;

import com.banasiak.ambilight.Config.ScreenRegion;

public class ConfigTest {

	@Test
	public void testScreenRegion() {
		subtest("120a", 120, 120, ScreenRegion.BOTTOM, 0, 60, 120, 60);
		subtest("120b", 120, 120, ScreenRegion.LEFT_THIRD, 0, 0, 40, 120);
	}

	private void subtest(String where, int inw, int inh, ScreenRegion bottom, int x, int y, int w, int h) {
		Rectangle expected = new Rectangle(x, y, w, h);
		Dimension dimension = new Dimension(inw, inh);
		Rectangle actual = bottom.createRectangle(dimension);
		assertEquals(where + " by dimension", expected, actual);
		
		Rectangle actual2 = bottom.createRectangle(ScreenRegion.dimensionToRectangle(dimension));
		assertEquals(where + " by dim2rectangle", expected, actual2);
	}

}
