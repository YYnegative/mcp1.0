package com.ebig.hdi.common.utils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    /**
     * 执行一个HTTP GET请求，返回请求响应的JSON字符串
     *
     * @param url 请求的URL地址
     * @return 返回请求响应的JSON字符串
     */
    public static String doGet(String url) {
        // 构造HttpClient的实例
        HttpClient client = new HttpClient();
        //设置参数
        // 创建GET方法的实例
        GetMethod method = new GetMethod(url);
        // 使用系统提供的默认的恢复策略
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        try {
            // 执行getMethod
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                return StreamUtils.copyToString(method.getResponseBodyAsStream(), Charset.forName("utf-8"));
            }
        } catch (IOException e) {
            logger.error("执行HTTP Get请求" + url + "时，发生异常！", e);
        } finally {
            method.releaseConnection();
        }
        return null;
    }

}