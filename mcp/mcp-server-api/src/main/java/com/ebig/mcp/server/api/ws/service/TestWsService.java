package com.ebig.mcp.server.api.ws.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "TestWsService", //服务民称
targetNamespace = "http://hdi.ebig.com" //命名空间
)
public interface TestWsService {

	@WebMethod
	@WebResult(name = "String", targetNamespace = "")
	public String sayHello(@WebParam(name = "userName") String name);
	
}
