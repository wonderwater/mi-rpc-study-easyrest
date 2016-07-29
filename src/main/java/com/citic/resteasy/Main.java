package com.citic.resteasy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.citic.resteasy.server.NettyServer;

public class Main {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

		ApplicationContext ac = new ClassPathXmlApplicationContext("root-context.xml");
		NettyServer netty = ac.getBean(NettyServer.class);
		
		netty.start();

	}
}
