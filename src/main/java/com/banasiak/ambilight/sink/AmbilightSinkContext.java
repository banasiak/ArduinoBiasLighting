package com.banasiak.ambilight.sink;

import com.banasiak.ambilight.api.ContextProvider;

public class AmbilightSinkContext implements ContextProvider<AmbilightSinkContext> 
{

	@Override
	public AmbilightSinkContext getContext() {
		return this;
	}

}
