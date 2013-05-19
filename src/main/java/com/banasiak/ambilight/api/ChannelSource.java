package com.banasiak.ambilight.api;

public interface ChannelSource<T, C> {
	
	/**
	 * 
	 * @param input for this channel to accept
	 */
	public T generate(ContextProvider<C> contextProvider);
}
