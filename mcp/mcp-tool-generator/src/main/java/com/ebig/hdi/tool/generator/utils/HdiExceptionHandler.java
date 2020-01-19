package com.ebig.hdi.tool.generator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 类功能说明：异常处理器<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 公司名称：信宜市新屋乐网络科技有限公司 <br/>
 * 作者：luorx <br/>
 * 创建时间：2019年1月19日 下午7:51:20 <br/>
 * 版本：V1.0 <br/>
 */
@RestControllerAdvice
public class HdiExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(HdiException.class)
	public Hdi handleRRException(HdiException e){
		Hdi r = new Hdi();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	@ExceptionHandler(Exception.class)
	public Hdi handleException(Exception e){
		logger.error(e.getMessage(), e);
		return Hdi.error();
	}
}
