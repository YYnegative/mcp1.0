package com.ebig.mcp.server.api.http.interceptor;

/**
 * 文件名称: ${file_name} <br/>
 * 类路径: ${package_name} <br/>
 * 描述: ${todo} <br/>
 * 作者：wenchao <br/>
 * 时间：${date} ${time} <br/>
 * 版本：V1.0 <br/>
 */

import com.ebig.hdi.common.utils.BASE64Utils;
import com.ebig.hdi.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 */
public class SignInterceptor extends HandlerInterceptorAdapter {

    @Value("${key}")
    private  String key;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String supplierCode = request.getHeader("supplierCode");
        String sign = request.getHeader("sign");
        if(StringUtil.isEmpty(supplierCode) || StringUtil.isEmpty(sign)){
            return false;
        }else {
            if(supplierCode.equals(BASE64Utils.decryptBASE64(sign).replace(key,""))){
                return true;
            }
            return false;
        }
    }

}
