package com.citic.resteasy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.citic.resteasy.common.Constant;
import com.citic.resteasy.common.HttpParamBean;
import com.citic.resteasy.exception.HttpConnectException;
import com.citic.resteasy.exception.HttpTimeoutException;

public class HttpUtil {
	
	private static Logger logger = Logger.getLogger(HttpUtil.class);
	
	/**
	 * 处理发送
	 * 
	 * @param http
	 * @param method
	 * @return
	 * @throws HttpTimeoutException
	 */
	public static String doService (HttpParamBean http) throws Exception {
		
		HttpUtil.appendGetURL(http);
		logger.info("doService # url = " + http.getUrl());
		logger.info("doService # params = " + http.getParams());
		
		HttpURLConnection connection = HttpUtil.initHttpConnection(http);
		try {
			
			if(Constant.HTTP_METHOD_POST.equals(http.getMethod())) {
				postData(http, connection);
			}
			
			return getResponseString(http.getCharset(), connection);
		} catch (SocketTimeoutException e) {
			logger.error("socket timeout: ", e);
			throw new HttpTimeoutException("Http doPost timeout: ");
		} catch(Exception e) {
			logger.error("http doPost failed: ", e);
		} finally {
			release(connection);
		}
		
		return null;
	}
	
	/**
	 * 拼接URL
	 * @param http
	 * @param connection
	 * @throws Exception
	 */
	private static void appendGetURL(HttpParamBean http) {
		
		boolean isPost = Constant.HTTP_METHOD_POST.equals(http.getMethod());
		boolean isParamsNull = StringUtils.isBlank(http.getParams());
		
		if(isPost || isParamsNull) return;
		
		StringBuffer sb = new StringBuffer();
		String params = http.getParams().replaceAll("[ \t]+", "");
		Set<Entry<String, Object>> set = JSON.parseObject(params).entrySet();
		for (Entry<String, Object> entry : set) {
			
			sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		
		String url = http.getUrl() + "?" + sb.substring(1);
		http.setUrl(url);
	}
	
	/**
	 * 处理POST数据
	 * @param http
	 * @param conn
	 * @throws Exception
	 */
	private static void postData(HttpParamBean http, HttpURLConnection conn) throws Exception {
		
		OutputStreamWriter out = null;
		try {
			
			out = new OutputStreamWriter(conn.getOutputStream(), http.getCharset());
			out.write(http.getParams());
			out.flush();
		} finally {
			release(out);
		}
	}
	
	/**
	 * 建立连接
	 * @param http
	 * @return
	 */
	private static HttpURLConnection initHttpConnection(HttpParamBean http) throws Exception {
		
		URL postUrl = null;
		HttpURLConnection connection = null;
		try {
			
			postUrl = new URL(http.getUrl());
			connection = (HttpURLConnection) postUrl.openConnection();
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			
			connection.setRequestMethod(http.getMethod());
			connection.setRequestProperty("Data-type", "html/text");
			connection.setRequestProperty("Accept-Charset", http.getCharset());
			connection.setRequestProperty("Content-type", http.getContentType());
			
			connection.setReadTimeout(http.getReadTimeOut());
			connection.setConnectTimeout(http.getConnectTimeout());
			
			connection.connect();
			
		} catch (Exception e) {
			throw new HttpConnectException(e);
		}
		
		return connection;
	}

	/**
	 * 获取响应
	 * 
	 * @param conn
	 * @return
	 */
	private static String getResponseString(String codeset, HttpURLConnection conn) {
		
		String line = "";
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			
			logger.info("doService # response code = " + conn.getResponseCode());
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), codeset));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			logger.error("doService getResponseString: ", e);
		} finally {
			release(reader);
		}
		
		return sb.toString();
	}
	
	/**
	 * 释放链接
	 * @param connection
	 */
	private static void release(HttpURLConnection connection) {
		
		if(connection != null){
			connection.disconnect();
		}
	}
	
	/**
	 * 释放IO
	 * @param connection
	 */
	private static void release(OutputStreamWriter out) {
		
		if(out != null){
			try {
				out.close();
			} catch (IOException e) {
				logger.error("release OutputStreamWriter: ", e);
			}
		}
	}
	
	/**
	 * 释放IO
	 * @param connection
	 */
	private static void release(BufferedReader reader) {
		
		if(reader != null){
			try {
				reader.close();
			} catch (IOException e) {
				logger.error("release BufferedReader: ", e);
			}
		}
	}
	
}
