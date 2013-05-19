package com.banasiak.ambilight.source;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;

import com.banasiak.ambilight.Config.ScreenRegion;
import com.banasiak.ambilight.api.ChannelSource;
import com.banasiak.ambilight.api.ContextProvider;
import com.banasiak.ambilight.compute.ComputeSampleFromRectangle;
import com.banasiak.ambilight.model.EmptyContext;
import com.banasiak.ambilight.model.TwoChannelColorData;

public class SampleRectangleSource implements ChannelSource<TwoChannelColorData, EmptyContext>
{
	private final ComputeSampleFromRectangle compute;
	private final Robot robot;
	private final ScreenRegion firstRegion;
	private final Rectangle firstRectangle;
	private final ScreenRegion secondRegion;
	private final Rectangle secondRectangle;
	private final Dimension outerRectangle;
	private final int sampleStepSize;
	
	public SampleRectangleSource(Robot robot, Dimension outerRectangle, ScreenRegion firstRegion, ScreenRegion secondRegion, int sampleStepSize) 
	{
		this.robot = robot;
		this.firstRegion = firstRegion;
		this.secondRegion = secondRegion;
		this.outerRectangle = outerRectangle;
		this.sampleStepSize = sampleStepSize;
				
		this.firstRectangle = this.firstRegion.createRectangle(this.outerRectangle);
		this.secondRectangle = this.secondRegion.createRectangle(this.outerRectangle);
		System.out.println("First =" + this.firstRectangle);
		System.out.println("Second=" + this.secondRectangle);
		
		this.compute = new ComputeSampleFromRectangle();
	}
	
	@Override
	public TwoChannelColorData generate(ContextProvider<EmptyContext> contextProvider) 
	{
		Color firstColor  = compute.sampleRectangle(robot, firstRectangle, sampleStepSize);
		Color secondColor = compute.sampleRectangle(robot, secondRectangle, sampleStepSize);
		
		TwoChannelColorData ret = new TwoChannelColorData(firstColor, secondColor);
		System.out.println("Source generated: " + ret);
		return ret;
	}

}
