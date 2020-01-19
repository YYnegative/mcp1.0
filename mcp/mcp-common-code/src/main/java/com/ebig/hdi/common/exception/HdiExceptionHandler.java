package com.ebig.hdi.common.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Hdi;

import lombok.extern.slf4j.Slf4j;

/**
 * 类功能说明：异常处理器<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 公司名称：信宜市新屋乐网络科技有限公司 <br/>
 * 作者：luorx <br/>
 * 创建时间：2019年1月19日 下午8:20:36 <br/>
 * 版本：V1.0 <br/>
 */
@Slf4j
@RestControllerAdvice
public class HdiExceptionHandler {

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

	@ExceptionHandler(DuplicateKeyException.class)
	public Hdi handleDuplicateKeyException(DuplicateKeyException e){
		log.error(e.getMessage(), e);
		return Hdi.error("数据库中已存在该记录");
	}

	@ExceptionHandler(AuthorizationException.class)
	public Hdi handleAuthorizationException(AuthorizationException e){
		log.error(e.getMessage(), e);
		return Hdi.error("没有权限，请联系管理员授权");
	}
	
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Hdi handleException(Exception e){
		if(e instanceof HdiException){
			HdiException vo = (HdiException) e;
			return Hdi.error(vo.getCode(), vo.getMessage());
		}else{
			log.error("系统异常:--> {}", e);
			return Hdi.error(-1, "系统异常");
		}
	}
}
