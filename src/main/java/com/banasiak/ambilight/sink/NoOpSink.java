package com.banasiak.ambilight.sink;

import com.banasiak.ambilight.api.ContextProvider;
import com.banasiak.ambilight.model.TwoChannelColorData;

public class NoOpSink implements AmbilightSink
{

	@Override
	public TwoChannelColorData accept(TwoChannelColorData input, ContextProvider<AmbilightSinkContext> contextProvider) 
	{
		return input;
	}

}
