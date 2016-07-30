package com.citic.resteasy.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.citic.resteasy.common.HttpParamBean;
import com.citic.resteasy.httpclient.RibbonHttpClient;
import com.citic.resteasy.server.NettyServer;
import com.netflix.loadbalancer.Server;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"classpath:root-context.xml"})
public class FirstControllerTest {

	@Autowired
	private NettyServer	server;
	
	@Before
	public void init() {
		server.start();
	}

	@Test
	public void testHelloworld() {
		
		List<Server> serverList = new ArrayList<Server> ();
    	serverList.add(new Server("127.0.0.1", 8082));
    	serverList.add(new Server("127.0.0.1", 8083));
		
    	try {
    		
    		for(int i = 0; i < 10; i++) {
    			
    			RibbonHttpClient client = new RibbonHttpClient(serverList);
    			HttpParamBean paramBean = new HttpParamBean();
    	    	paramBean.setUrl("/resteasy/hello/world");
    	    	paramBean.setMethod("GET");
    	    	paramBean.setParams("{'message':'Welcome,HelloWorld'}");
    			
    			String response = client.call(paramBean, false);
        		System.out.println("response: " + response);
        		
        		Assert.assertTrue(StringUtils.isNotBlank(response));
    		}
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    		Assert.fail("test hello world fail ....");
    	}
	}

}
