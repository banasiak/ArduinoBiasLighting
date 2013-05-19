package com.banasiak.ambilight.sink;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import com.banasiak.ambilight.api.AmbilightException;
import com.banasiak.ambilight.api.ContextProvider;
import com.banasiak.ambilight.model.TwoChannelColorData;

public class SerialPortSink implements AmbilightSink 
{
    private static final int SERIAL_PORT_TIMEOUT = 2000;
    private static final int SERIAL_PORT_DATA_RATE = 9600;
    
    private SerialPort serialPort = null;
    private CommPortIdentifier portId = null;
    private OutputStream output = null;
    
    public SerialPortSink(final String serialPortName)
    {
    	// config.getSerialPort();
    	initializeSerialPort(serialPortName);
    }

	@Override
	public TwoChannelColorData accept(final TwoChannelColorData input, ContextProvider<AmbilightSinkContext> contextProvider) 
	{
		Color first = input.getFirst();
		Color second = input.getSecond();
		
        // write marker for synchronization
        try {
			output.write(0xff);
		
			// TODO: guard against writing 0xff as a data value???
        
			// RGB colors for left region
			output.write(first.getRed());
			output.write(first.getGreen());
			output.write(first.getBlue());

			// RGB colors for right region
			output.write(second.getRed());
			output.write(second.getGreen());
			output.write(second.getBlue());

        } catch (IOException e) {
        	throw new AmbilightException("SerialPort IOException", e);
		}

        return input;
	}
	
	@SuppressWarnings("unchecked")
	private static Enumeration<CommPortIdentifier> getPortIdentifiers() 
	{
		return (Enumeration<CommPortIdentifier>) CommPortIdentifier.getPortIdentifiers();
	}
	
    private void initializeSerialPort(final String serialPortName)
    {
        //final String serialPortName = config.getSerialPort();

        
        final Enumeration<CommPortIdentifier> portEnum = getPortIdentifiers();

        while (portEnum.hasMoreElements())
        {
            final CommPortIdentifier currentPortId = portEnum.nextElement();
            if (currentPortId.getName().equals(serialPortName))
            {
                portId = currentPortId;
                break;
            }
        }

        if (portId == null)
        {
            throw new AmbilightException("Could not find COM port: " + serialPortName);
        }

        try
        {
            serialPort = (SerialPort) portId.open("Ambilight", SERIAL_PORT_TIMEOUT);
            serialPort.setSerialPortParams(SERIAL_PORT_DATA_RATE,
            		                       SerialPort.DATABITS_8, 
            		                       SerialPort.STOPBITS_1,
            		                       SerialPort.PARITY_NONE);

            output = serialPort.getOutputStream();
        }
        catch (final Exception e)
        {
        	throw new AmbilightException("Error initializing COM port(" + serialPortName + ")", e);
        }
    }

    public void closeSerialPort()
    {
    	try {
    		if (serialPort != null)
    		{
    			serialPort.removeEventListener();
    			serialPort.close();
    		}
    		serialPort = null;
        
    		output.close();
		} catch (IOException e) {
			// MEH // e.printStackTrace();
		}
    }	
	
}
