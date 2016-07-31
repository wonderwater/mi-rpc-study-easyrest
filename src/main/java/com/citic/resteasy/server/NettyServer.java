package com.citic.resteasy.server;

import java.util.Collection;

import javax.annotation.PreDestroy;

import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.citic.resteasy.provider.ResteasyFastJsonProvider;
import com.google.common.collect.Lists;

@Component
public class NettyServer {

	@Autowired
	ApplicationContext ac;

	String rootResourcePath	= "/resteasy";
	int	port = 8083;
	NettyJaxrsServer netty;

	public void start() {

		ResteasyDeployment dp = new ResteasyDeployment();
		
		ResteasyFastJsonProvider fastjsonProvider = new ResteasyFastJsonProvider();
		Collection<Object> controllers = ac.getBeansWithAnnotation(Controller.class).values();

		dp.getProviders().addAll(Lists.newArrayList(fastjsonProvider));
		dp.getResources().addAll(controllers);
		
		netty = new NettyJaxrsServer();
		netty.setDeployment(dp);
		netty.setPort(port);
		netty.setRootResourcePath("/resteasy");
		netty.setSecurityDomain(null);
		netty.start();
	}

	@PreDestroy
	public void cleanUp() {
		netty.stop();
	}

	public String getRootResourcePath() {
		return rootResourcePath;
	}

	public int getPort() {
		return port;
	}

}
