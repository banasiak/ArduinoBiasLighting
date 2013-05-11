package com.banasiak.ambilight;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class SimpleRead implements Runnable, SerialPortEventListener {
	
	public static void main(String[] args) {

		Enumeration<CommPortIdentifier> portList;
		CommPortIdentifier portId;

		
		portList = SimpleWrite.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				// if (portId.getName().equals("COM1")) {
				if (portId.getName().equals("/dev/term/a")) {
					SimpleRead reader = new SimpleRead(portId);
				}
			}
		}
	}
	
	SerialPort serialPort;
	Thread readThread;
	InputStream inputStream;
	

	public SimpleRead(CommPortIdentifier portId) {
		try {
			serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
		} catch (PortInUseException e) {
		}
		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
		}
		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
		}
		serialPort.notifyOnDataAvailable(true);
		try {
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
		}
		readThread = new Thread(this);
		readThread.start();
	}

	public void run() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
		}
	}

	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[20];

			try {
				while (inputStream.available() > 0) {
					int numBytes = inputStream.read(readBuffer);
					if (numBytes < 0) {
						System.out.println("NumBytes error, " + numBytes);
					}
				}
				System.out.print(new String(readBuffer));
			} catch (IOException e) {
			}
			break;
		}
	}
}