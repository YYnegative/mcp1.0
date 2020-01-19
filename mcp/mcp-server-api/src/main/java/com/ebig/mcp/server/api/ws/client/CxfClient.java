package com.ebig.mcp.server.api.ws.client;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.ebig.mcp.server.api.ws.interceptor.CxfAuthInterceptor;
import com.ebig.mcp.server.api.ws.service.TestWsService;

public class CxfClient {

	public static void main(String[] args) {
	    try {
	      // 接口地址
	      String address = "http://localhost:8081/mcp-server-api/services/TestWsService";

	      // 代理工厂
	      JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
	      // 设置代理地址
	      jaxWsProxyFactoryBean.setAddress(address);
	      // 设置接口类型
	      jaxWsProxyFactoryBean.setServiceClass(TestWsService.class);
	      jaxWsProxyFactoryBean.getOutInterceptors().add(new CxfAuthInterceptor());
	      // 创建一个代理接口实现
	      TestWsService testWsService = (TestWsService) jaxWsProxyFactoryBean.create();
	      // 数据准备
	      String name = "666";
	      // 调用代理接口的方法调用并返回结果
	      String result = testWsService.sayHello(name);
	      System.out.println("返回结果:" + result);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
}
