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

import com.alibaba.fastjson.JSON;
import com.citic.resteasy.common.Constant;
import com.citic.resteasy.common.HttpParamBean;
import com.citic.resteasy.controller.request.Person;
import com.citic.resteasy.httpclient.RibbonHttpClient;
import com.citic.resteasy.server.NettyServer;
import com.netflix.loadbalancer.Server;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"classpath:root-context.xml"})
public class FirstControllerTest {
	
	@Autowired
	private NettyServer	server;
	
	private List<Server> serverList = new ArrayList<Server> ();
	
	@Before
	public void init() {
		
		server.start();
		
		serverList.add(new Server("127.0.0.1", 8082));
    	serverList.add(new Server("127.0.0.1", 8083));
	}

	@Test
	public void test_helloworld_by_get() {
		
    	try {
    		for(int i = 0; i < 10; i++) {
    			
    			RibbonHttpClient client = new RibbonHttpClient(serverList);
    			HttpParamBean paramBean = new HttpParamBean();
    			paramBean.setMethod(Constant.HTTP_METHOD_GET);
    	    	paramBean.setUrl("/resteasy/hello/world");
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
	
	@Test
	public void test_send_data_by_post() {
		
    	try {
    		for(int i = 0; i < 10; i++) {
    			
    			List<Person> list = new ArrayList<Person> ();
    			list.add(new Person(1, "person1"));
    			list.add(new Person(2, "测试人员2"));
    			
    			RibbonHttpClient client = new RibbonHttpClient(serverList);
    			HttpParamBean paramBean = new HttpParamBean();
    			paramBean.setMethod(Constant.HTTP_METHOD_POST);
    	    	paramBean.setUrl("/resteasy/hello/json");
    	    	paramBean.setParams(JSON.toJSONString(list));
    			
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
