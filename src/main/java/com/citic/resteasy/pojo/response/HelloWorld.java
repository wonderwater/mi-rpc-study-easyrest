package com.citic.resteasy.pojo.response;

public class HelloWorld {

	String	message;
	
	public HelloWorld() {
		
	}
	
	public HelloWorld(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
