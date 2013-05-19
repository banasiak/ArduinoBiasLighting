package com.banasiak.ambilight.model;

public class TwoChannelData<T> {
	private final T first;
	private String firstDescription;
	private final T second;
	private String secondDescription;
	
	public TwoChannelData(T first, T second) {
		this("", first, "", second);
	}
	public TwoChannelData(String firstDescription, T first, String secondDescription, T second) {
		this.first = first;
		this.second = second;
		this.firstDescription = firstDescription;
		this.secondDescription = secondDescription;
	}
	public T getFirst() {
		return first;
	}
	public T getSecond() {
		return second;
	}
	public String getFirstDescription() {
		return firstDescription;
	}
	public String getSecondDescription() {
		return secondDescription;
	}
}
