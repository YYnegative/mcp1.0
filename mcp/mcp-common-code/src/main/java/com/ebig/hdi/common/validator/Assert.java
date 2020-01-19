/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.common.validator;

import org.apache.commons.lang.StringUtils;

import com.ebig.hdi.common.exception.HdiException;

/**
 * 数据校验
 *
 * @author Mark sunlightcs@gmail.com
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new HdiException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new HdiException(message);
        }
    }
}
