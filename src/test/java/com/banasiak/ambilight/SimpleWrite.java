/**	
 * Java Serial Port Program
 * Posted: Sep 4, 2001 7:59 AM 		
 **/
package com.banasiak.ambilight;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class SimpleWrite {
	
	@SuppressWarnings("unchecked")
	public static Enumeration<CommPortIdentifier> getPortIdentifiers() {
		return (Enumeration<CommPortIdentifier>) CommPortIdentifier.getPortIdentifiers();		
	}

	public static void main(String[] args) {
		Enumeration<CommPortIdentifier> portList;
		CommPortIdentifier portId;
		String messageString = "Hello, world!\n";
		SerialPort serialPort = null;
		OutputStream outputStream = null;
		
		portList = getPortIdentifiers();

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				// if (portId.getName().equals("COM1")) {
				if (portId.getName().equals("/dev/term/a")) {
					try {
						serialPort = (SerialPort) portId.open("SimpleWriteApp", 2000);
					} catch (PortInUseException e) {
					}
					try {
						outputStream = serialPort.getOutputStream();
					} catch (IOException e) {
					}
					try {
						serialPort.setSerialPortParams(9600,
													   SerialPort.DATABITS_8, 
													   SerialPort.STOPBITS_1,
													   SerialPort.PARITY_NONE);
					} catch (UnsupportedCommOperationException e) {
					}
					try {
						outputStream.write(messageString.getBytes());
					} catch (IOException e) {
					}
				}
			}
		}
	}
}
