package com.ebig.mcp.server.api.ws.service.impl;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.ebig.mcp.server.api.ws.service.TestWsService;

@WebService(serviceName = "TestWsService", // 与接口中指定的name一致
targetNamespace = "http://hdi.ebig.com", // 与接口中的命名空间一致,一般是接口的包名倒
endpointInterface = "com.ebig.mcp.server.api.ws.service.TestWsService"// 接口地址
)
@Component
public class TestWsServiceImpl implements TestWsService {

	@Override
	public String sayHello(String name) {
	    return "Hello ," + name;
	}

}
