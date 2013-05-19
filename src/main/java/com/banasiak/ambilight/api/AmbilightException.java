package com.banasiak.ambilight.api;

public class AmbilightException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3551040680396412834L;

	public AmbilightException() {
		super();
	}

	public AmbilightException(String message, Throwable cause) {
		super(message, cause);
	}

	public AmbilightException(String message) {
		super(message);
	}

	public AmbilightException(Throwable cause) {
		super(cause);
	}

}
