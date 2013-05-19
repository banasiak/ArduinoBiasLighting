package com.banasiak.ambilight.source;

import java.awt.Color;

import com.banasiak.ambilight.api.ChannelSource;
import com.banasiak.ambilight.api.ContextProvider;
import com.banasiak.ambilight.model.EmptyContext;
import com.banasiak.ambilight.model.TwoChannelColorData;

public class ConstantSource implements ChannelSource<TwoChannelColorData, EmptyContext>{

	private final Color firstColor;
	private final Color secondColor;
	
	public ConstantSource(Color first, Color second)
	{
		this.firstColor = first;
		this.secondColor = second;
	}
	
	@Override
	public TwoChannelColorData generate(ContextProvider<EmptyContext> contextProvider) 
	{
		TwoChannelColorData ret = new TwoChannelColorData(firstColor, secondColor);
		return ret;
	}

}
