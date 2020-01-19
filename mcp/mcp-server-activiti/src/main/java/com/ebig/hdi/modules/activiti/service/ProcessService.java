package com.ebig.hdi.modules.activiti.service;

import org.activiti.engine.repository.Model;

import javax.xml.stream.XMLStreamException;
import java.io.UnsupportedEncodingException;

/**

 */
public interface ProcessService {
    Model convertToModel(String procDefId) throws UnsupportedEncodingException, XMLStreamException;

}
