package com.ebig.hdi.tool.generator.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类功能说明：返回数据<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 公司名称：信宜市新屋乐网络科技有限公司 <br/>
 * 作者：luorx <br/>
 * 创建时间：2019年1月19日 下午7:50:33 <br/>
 * 版本：V1.0 <br/>
 */
public class Hdi extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public Hdi() {
		put("code", 0);
	}
	
	public static Hdi error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static Hdi error(String msg) {
		return error(500, msg);
	}
	
	public static Hdi error(int code, String msg) {
		Hdi r = new Hdi();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static Hdi ok(String msg) {
		Hdi r = new Hdi();
		r.put("msg", msg);
		return r;
	}
	
	public static Hdi ok(Map<String, Object> map) {
		Hdi r = new Hdi();
		r.putAll(map);
		return r;
	}
	
	public static Hdi ok() {
		return new Hdi();
	}

	public Hdi put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
