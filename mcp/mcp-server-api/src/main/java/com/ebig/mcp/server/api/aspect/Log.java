package com.ebig.mcp.server.api.aspect;

import java.lang.annotation.*;

/**
 * @description:日志注解
 * @author: wenchao
 * @time: 2019-10-15 14:29
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    String title() default "";

    /**
     * 功能
     */
    String action() default "";
}

