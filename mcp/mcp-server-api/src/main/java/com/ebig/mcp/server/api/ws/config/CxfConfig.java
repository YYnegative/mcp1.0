package com.ebig.mcp.server.api.ws.config;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ebig.mcp.server.api.ws.service.TestWsService;

@Configuration
public class CxfConfig {
	
	 @Autowired
	 private Bus bus;

	 @Autowired
	 TestWsService testWsService;

	 @Bean
	 public Endpoint endpoint() {
	    EndpointImpl endpoint = new EndpointImpl(bus, testWsService);
	    endpoint.publish("/TestWsService");
	    return endpoint;
	 }
}
