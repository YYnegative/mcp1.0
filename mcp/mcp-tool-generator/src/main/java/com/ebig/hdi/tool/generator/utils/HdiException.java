package com.ebig.hdi.tool.generator.utils;

/**
 * 类功能说明：自定义异常<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 公司名称：信宜市新屋乐网络科技有限公司 <br/>
 * 作者：luorx <br/>
 * 创建时间：2019年1月19日 下午7:50:55 <br/>
 * 版本：V1.0 <br/>
 */
public class HdiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public HdiException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public HdiException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public HdiException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public HdiException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
