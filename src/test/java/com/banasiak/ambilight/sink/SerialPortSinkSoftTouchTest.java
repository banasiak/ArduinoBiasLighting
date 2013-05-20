package com.banasiak.ambilight.sink;

import static org.junit.Assert.*;

import org.junit.Test;

public class SerialPortSinkSoftTouchTest {

	@Test
	public void testSoftTouch() {
		try
		{
			boolean actual = SerialPortSinkSoftTouch.canTouchSerialPortSink();
			System.out.println("Softtouch says " + actual);
		}
		catch (Exception e)
		{
			fail("Should not have thrown exception");
		}
	}

}
