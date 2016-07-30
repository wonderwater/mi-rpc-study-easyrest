package com.citic.resteasy.common;


public class HttpParamBean {
	
	private String charset = Constant.CHARSET_UTF8;
	
	private String contentType = Constant.CONTENT_TYPE_JSON;
	
	private String method = Constant.HTTP_METHOD_POST;
	
	private Integer readTimeOut = 5000;
	
	private Integer connectTimeout = 5000;
	
	private String url;
	
	private String params;
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Integer getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(Integer readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
}
