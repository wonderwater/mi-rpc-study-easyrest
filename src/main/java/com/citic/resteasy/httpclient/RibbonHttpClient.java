package com.citic.resteasy.httpclient;

import java.util.List;

import com.citic.resteasy.common.HttpParamBean;
import com.citic.resteasy.utils.HttpUtil;
import com.netflix.client.DefaultLoadBalancerRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.LoadBalancerStats;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.loadbalancer.reactive.ServerOperation;

import rx.Observable;

public class RibbonHttpClient {
	
    private final ILoadBalancer loadBalancer;
    
    private final RetryHandler retryHandler = new DefaultLoadBalancerRetryHandler(0, 1, true);
    
    public RibbonHttpClient(List<Server> serverList) {
        loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(serverList);
    }
    
    public String call(final HttpParamBean paramBean, final boolean isSSL) throws Exception {
    	
        return LoadBalancerCommand.<String>builder()
                .withLoadBalancer(loadBalancer)
                .withRetryHandler(retryHandler)
                .build()
                .submit(new ServerOperation<String>() {
                	
            @Override
            public Observable<String> call(Server server) {
                try {
                	
                	String protocol = isSSL ? "https://" : "http://";
                	String urlPrefix = protocol + server.getHost() + ":" + server.getPort();
                	paramBean.setUrl(urlPrefix + paramBean.getUrl());
                	
                    return Observable.just(HttpUtil.doService(paramBean));
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        }).toBlocking().first();
    }
    
    public LoadBalancerStats getLoadBalancerStats() {
        return ((BaseLoadBalancer) loadBalancer).getLoadBalancerStats();
    }
}
