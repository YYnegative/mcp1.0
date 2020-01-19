package com.ebig.mcp.server.api.ws.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class User implements Serializable {

	/** 
	 * 描述: TODO <br/>
	 * Fields  serialVersionUID : TODO <br/>
	 */
	private static final long serialVersionUID = -8577461031495478101L;
	
	private String userId;
	private String username;
	private String age;
	private Date updateTime;

}
