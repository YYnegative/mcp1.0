package com.ebig.mcp.server.api.ws.interceptor;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CxfAuthInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
	
	public CxfAuthInterceptor() {
		//准备发送阶段
		super(Phase.PREPARE_SEND);
	}

	@Override
	public void handleMessage(SoapMessage soapMessage) throws Fault {
		List<Header> headers = soapMessage.getHeaders();

		Document doc = DOMUtils.createDocument();

		Element auth = doc.createElement("auth");

		Element name = doc.createElement("name");
		name.setTextContent("hwj");

		Element password = doc.createElement("password");
		password.setTextContent("123456");

		auth.appendChild(name);
		auth.appendChild(password);

		headers.add(new Header(new QName(""), auth));
	}
}
