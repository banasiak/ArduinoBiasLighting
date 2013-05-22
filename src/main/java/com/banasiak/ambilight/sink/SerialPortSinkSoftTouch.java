package com.banasiak.ambilight.sink;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.banasiak.ambilight.api.AmbilightException;

public class SerialPortSinkSoftTouch {

	public static boolean canTouchSerialPortSink()
	{
		return canTouchSerialPortSink(null);
	}
	public static boolean canTouchSerialPortSink(String serialPort)
	{
		final String fakeSerialPort = "faAkEserIal";
		final String useSerialPort = (serialPort != null) ? serialPort : fakeSerialPort;
		boolean ret = false;
		SerialPortSink obj = null;
		try {
			final Class<?> c = Class.forName("com.banasiak.ambilight.sink.SerialPortSink");
			final Constructor cons = c.getConstructor(java.lang.String.class);
			try
			{
				obj = (SerialPortSink) cons.newInstance(useSerialPort);
				ret = (obj != null);
			}
			catch (final InstantiationException e)
			{
				if (useSerialPort.equals(fakeSerialPort))
				{
					// "probably ok" if fakeSerialPort was used? ?TODO?
					ret = true;
				}
				else
				{
					//overloaded meaning here : the library load worked, but your serial port didn't
					// since we're trying to prevent "kill the JVM errors", we return true ?TODO?

					ret = true;
				}
			}
			catch (final IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final InvocationTargetException e) {
				// If you do NOT have the library .so/.dll, you get
				//   UnsatisifiedLinkError inside InvocationTargetException
				ret = false;
			}
		} catch (final ClassNotFoundException e) {
			ret = false;
		} catch (final NoSuchMethodException e) {
			throw new AmbilightException("Constructor not found");
		} catch (final SecurityException e) {
			throw new AmbilightException("Constructor not found");
		}
		// have to close the serial port manually, otherwise we can't open it again
		if(obj != null){
		    obj.closeSerialPort();
		}
		return ret;
	}
}
