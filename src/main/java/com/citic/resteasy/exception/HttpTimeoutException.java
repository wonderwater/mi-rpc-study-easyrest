package com.citic.resteasy.exception;

public class HttpTimeoutException extends Exception {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = 6888153261699433544L;
	
	public HttpTimeoutException(String message) {
		super(message);
	}
}
