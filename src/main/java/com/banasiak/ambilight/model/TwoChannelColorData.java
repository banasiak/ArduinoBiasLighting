package com.banasiak.ambilight.model;

import java.awt.Color;

public class TwoChannelColorData extends TwoChannelData<Color> {

	public TwoChannelColorData(Color first, Color second) {
		super(first, second);
	}

	@Override
	public String toString() {
		return "TwoChannelColorData [getFirst()=" + getFirst()
				+ ", getSecond()=" + getSecond() + ", getFirstDescription()="
				+ getFirstDescription() + ", getSecondDescription()="
				+ getSecondDescription() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	
}
