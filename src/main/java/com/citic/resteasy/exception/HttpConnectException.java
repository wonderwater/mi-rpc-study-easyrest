package com.citic.resteasy.exception;

public class HttpConnectException extends Exception {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = 6888154261699455544L;
	
	public HttpConnectException(String message) {
		super(message);
	}
	
	public HttpConnectException(Exception exception) {
		super(exception);
	}
}
