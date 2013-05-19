package com.banasiak.ambilight.sink;

import java.awt.Color;
import java.io.PrintStream;

import com.banasiak.ambilight.api.ContextProvider;
import com.banasiak.ambilight.model.TwoChannelColorData;

public class DebugFilter implements AmbilightSink
{
	final PrintStream out;
	
	public DebugFilter(PrintStream out)
	{
		this.out = out;
	}
	
	@Override
	public TwoChannelColorData accept(TwoChannelColorData input, ContextProvider<AmbilightSinkContext> contextProvider) 
	{
		Color firstColor = input.getFirst();
		String firstInfo = input.getFirstDescription();
		
		Color secondColor = input.getSecond();
		String secondInfo = input.getSecondDescription();
		
		out.println(//"Region: " + config.getRegion1().name()
				    firstInfo + " | " + dumpColor(firstColor));
				    //" | Color: [r=" + firstColor.getRed() + ",g=" + green1 + ",b=" + blue1 + "]");
        out.println(//"Region: " + config.getRegion2().name()
        		     secondInfo + " | " + dumpColor(secondColor));
                //+ " | Color: [r=" + red2 + ",g=" + green2 + ",b=" + blue2 + "]");
        out.println("");

		return null;
	}
	private String dumpColor(Color color) 
	{
		return "Color: [r=" + color.getRed() + ",g=" + color.getGreen() + ",b=" + color.getBlue() + "]";
	}
}
