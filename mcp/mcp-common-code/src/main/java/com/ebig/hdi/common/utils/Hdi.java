/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.common.utils;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Hdi extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public Hdi() {
		put("code", 0);
		put("msg", "success");
	}
	
	public static Hdi error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static Hdi error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
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
	@Override
	public Hdi put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
