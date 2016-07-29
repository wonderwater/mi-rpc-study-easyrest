package com.citic.resteasy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.citic.resteasy.controller.FirstController;
import com.citic.resteasy.controller.request.Person;
import com.citic.resteasy.pojo.response.HelloWorld;
import com.citic.resteasy.server.NettyServer;
import com.citic.resteasy.test.FirstControllerTest.TestConfig;

@ContextConfiguration(classes = TestConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class FirstControllerTest {

	@Autowired
	private NettyServer	server;
	
	@Autowired
	private RestOperations restOps;
	
	@Configuration
	public static class TestConfig {

		@Bean
		public NettyServer nettyServer() {
			return new NettyServer();
		}

		@Bean
		public FirstController homeController() {
			return new FirstController();
		}

		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

	@Before
	public void init() {
		server.start();
	}

	@Test
	public void testHelloworld() {
		
		HelloWorld resp = restOps.getForObject(buildUrl("hello/world"), HelloWorld.class);
		assertNotNull(resp);
		assertTrue(StringUtils.hasText(resp.getMessage()));
	}

	@Test
	public void testPOST() {
		
		HttpEntity<MultiValueMap<String, String>> request = null;
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("id", "33");
		map.add("name", "rest");
		
		request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		
		Person resp = restOps.postForObject(buildUrl("hello/form"), request, Person.class);
		assertNotNull(resp);
		assertTrue(resp.getId().equals(33));
		assertTrue(resp.getName().equals("rest"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testPOSTbyList() {
		
		int id = 44;
		String name = "NAME";
		List<Person> paramList = Collections.singletonList(new Person(id, name));
		
		List<Object> resp = restOps.postForObject(buildUrl("hello/json?multi=true"), paramList, List.class);

		assertEquals(1, resp.size());
	}

	private String buildUrl(String target) {
		
		String urlFormat = "http://localhost:%d/%s/%s";
		return String.format(urlFormat, server.getPort(), server.getRootResourcePath(), target);
	}

}
