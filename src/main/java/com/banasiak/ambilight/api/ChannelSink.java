package com.banasiak.ambilight.api;

public interface ChannelSink<T, C> {
	
	/**
	 * 
	 * @param input for this channel to accept
	 * @return a modified T [if this sink is a filter]
	 */
	public T accept(T input, ContextProvider<C> contextProvider);
}
