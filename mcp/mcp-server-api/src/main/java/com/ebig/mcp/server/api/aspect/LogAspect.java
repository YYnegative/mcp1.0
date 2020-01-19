package com.ebig.mcp.server.api.aspect;

import com.ebig.hdi.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @description:service层日志
 * @author: wenchao
 * @time: 2019-10-15 14:29
 */

@Aspect
@Component("logAspect")
@Slf4j
public class LogAspect {
    // 配置切入点
    @Pointcut("@annotation(com.ebig.mcp.server.api.aspect.Log)")
    public void logPointCut() {

    }

    /**
     * 前置通知 用于拦截操作，在方法返回后执行
     *
     * @param joinPoint 切点
     */
    @Before(value = "logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        handleLog(joinPoint);
    }

    @After(value = "logPointCut()")
    public void doAfter(JoinPoint joinPoint) {
        afterLog(joinPoint);
    }

    private void afterLog(JoinPoint joinPoint) {
        try {
            log.info(">>>>>>>>>>>>>方法执行结束:{}", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==后置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 拦截异常操作，有异常时执行
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfter(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint);
    }


    private void handleLog(JoinPoint joinPoint) {
        try {
            // 获得注解
            Log serviceLog = getAnnotationLog(joinPoint);
            if (serviceLog == null) {
                return;
            }
            // 获得方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            String action = serviceLog.action();
            String title = serviceLog.title();
            //打印日志，如有需要还可以存入数据库
            log.info(">>>>>>>>>>>>>模块名称：{}", title);
            log.info(">>>>>>>>>>>>>操作名称：{}", action);
            log.info(">>>>>>>>>>>>>类名：{}", className);
            log.info(">>>>>>>>>>>>>方法名：{}", methodName);
            log.info(">>>>>>>>>>>>>方法执行开始:{}", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private  Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }


}

