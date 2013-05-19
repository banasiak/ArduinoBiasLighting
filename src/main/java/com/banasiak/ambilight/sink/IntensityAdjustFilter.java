package com.banasiak.ambilight.sink;

import java.awt.Color;

import com.banasiak.ambilight.api.ContextProvider;
import com.banasiak.ambilight.model.TwoChannelColorData;

public class IntensityAdjustFilter implements AmbilightSink 
{
	private final double redIntensity;
	private final double greenIntensity;
	private final double blueIntensity;
	public IntensityAdjustFilter(double red, double green, double blue)
	{
		check("red", red);
		check("green", green);
		check("blue", blue);
		this.redIntensity = red;
		this.greenIntensity = green;
		this.blueIntensity = blue;
	}
	private void check(String where, double val) 
	{
		if (val < 0.0) 
		{
			throw new IllegalArgumentException("Intensity modification(" + where + ") must be >= 0");
		}
		if (val > 1.0)
		{
			throw new IllegalArgumentException("Intensity modification(" + where + ") must be <= 1.0");
		}
	}
	@Override
	public TwoChannelColorData accept(TwoChannelColorData input, ContextProvider<AmbilightSinkContext> contextProvider) 
	{
		Color regionOneColor = input.getFirst();
		Color regionTwoColor = input.getSecond();
		
        // adjust the colors according to the custom intensity
        final int red1 = (int) (regionOneColor.getRed() * redIntensity);
        final int green1 = (int) (regionOneColor.getGreen() * greenIntensity);
        final int blue1 = (int) (regionOneColor.getBlue() * blueIntensity);

        final int red2 = (int) (regionTwoColor.getRed() * redIntensity);
        final int green2 = (int) (regionTwoColor.getGreen() * greenIntensity);
        final int blue2 = (int) (regionTwoColor.getBlue() * blueIntensity);

        return new TwoChannelColorData(new Color(red1, green1, blue1),
        		                       new Color(red2, green2, blue2));
	}

}
