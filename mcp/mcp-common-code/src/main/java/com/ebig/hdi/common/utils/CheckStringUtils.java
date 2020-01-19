package com.ebig.hdi.common.utils;

import com.ebig.hdi.common.exception.HdiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @program: mcp
 * @description: 校验非空
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2019-12-12 10:04
 **/
public final class CheckStringUtils {

    /**
     * @Description:私有构造器并不允许实例化
     * @Author: ZhengHaiWen
     * @Date: 2019/12/12
     */
    private CheckStringUtils(){
        throw new RuntimeException("CheckStringUtils不允许实例化");
    }
    private final static Logger logger = LoggerFactory.getLogger(CheckStringUtils.class);

    /**
     * @Description:判断String对象是否为空，并抛出异常
     * @Author: ZhengHaiWen
     * @Date: 2019/12/12
     */
    public static void isEmpty(String str,String s) {
        if(null == str || "".equals(str)) {
            throw new HdiException(s);
        }
    }

    /**
     * @Description:判断Object数组对象是否为空，并抛出异常
     * @Author: ZhengHaiWen
     * @Date: 2019/12/12
     */
    public static void isEmpty(Object[] obj,String s) {
        if(null == obj || 0 == obj.length) {
            throw new HdiException(s);
        }
    }

    /**
     * @Description:判断Object对象是否为空，并抛出异常
     * @Author: ZhengHaiWen
     * @Date: 2019/12/12
     */
    public static void isEmpty(Object obj,String s) {
        if(null == obj) {
            throw new HdiException(s);
        }
    }

    /**
     * @Description:判断List类型对象是否为空，并抛出异常
     * @Author: ZhengHaiWen
     * @Date: 2019/12/12
     */
    public static void isEmpty(List<?> list, String s) {
        if(null == list || list.isEmpty()) {
            throw new HdiException(s);
        }
    }

    /**
     * @Description:判断Map类型对象是否为空，并抛出异常
     * @Author: ZhengHaiWen
     * @Date: 2019/12/12
     */
    public static void isEmpty(Map<?, ?> obj, String s) {
        if(null == obj || obj.isEmpty()) {
            throw new HdiException(s);
        }
    }

    /**
     * @Description:判断List类型对象不为空，并抛出异常
     * @Author: ZhengHaiWen
     * @Date: 2019/12/12
     */
    public static void listIsNotEmpty(List<?> list,String s) {
        if(list.size() > 0) {
            throw new HdiException(s);
        }
    }
}
