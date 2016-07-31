package com.citic.resteasy.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.citic.resteasy.common.Constant;

@Provider
@Consumes({"application/*+json", "text/json"})
@Produces({"application/*+json", "text/json"})
public class ResteasyFastJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
	
	private static Logger logger = Logger.getLogger(ResteasyFastJsonProvider.class);
	
	@Override
	public boolean isWriteable(Class<?> type, Type generic, Annotation[] annotations, MediaType media) {
		
		return hasMatchingMediaType(media);
	}

	@Override
	public long getSize(Object t, Class<?> type, Type generic, Annotation[] annotations, MediaType media) {
		
		return -1;
	}

	@Override
	public void writeTo(Object t, Class<?> type, Type generic, Annotation[] annotations, MediaType media,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		
		OutputStreamWriter out = null;
		try {
			
			out = new OutputStreamWriter(entityStream, Constant.DEFAULT_CHARSET);
			out.write(JSON.toJSONString(t));
			out.flush();
		} finally {
			release(out);
		}
	}

	@Override
	public boolean isReadable(Class<?> type, Type generic, Annotation[] annotations, MediaType media) {
		
		return hasMatchingMediaType(media);
	}

	@Override
	public Object readFrom(Class<Object> type, Type generic, Annotation[] annotations, MediaType media,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		
		String line = "";
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			
			String charset = httpHeaders.getFirst("Accept-Charset");
			charset = StringUtils.isNotBlank(charset) ? charset : Constant.DEFAULT_CHARSET;
			reader = new BufferedReader(new InputStreamReader(entityStream, charset));
			
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			release(reader);
		}
		
		return JSON.parseObject(sb.toString(), generic);
	}
	
    public boolean hasMatchingMediaType(MediaType mediaType) {
       
        if (mediaType == null) return true;
        
        String subtype = mediaType.getSubtype();
        boolean isJson = "json".equalsIgnoreCase(subtype) || subtype.endsWith("+json");
        boolean isJavascript = "javascript".equals(subtype) || "x-javascript".equals(subtype);
        
        return isJson || isJavascript;
    }
    
    /**
	 * 释放IO
	 * @param connection
	 */
	private void release(BufferedReader reader) {
		
		if(reader != null){
			try {
				reader.close();
			} catch (IOException e) {
				logger.error("release BufferedReader: ", e);
			}
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
	
}
