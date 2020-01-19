package com.ebig.hdi.common.utils;

import com.ebig.hdi.common.exception.HdiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;


/**
 * 类功能说明：String字符串工具类 <br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 公司名称：信宜市新屋乐网络科技有限公司 <br/>
 * 作者：luorx <br/>
 * 创建时间：2016-8-2 下午4:30:13 <br/>
 * 版本：V1.0 <br/>
 */
public final class StringUtil {

    private final static Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 函数功能说明 ： 私有构造方法,将该工具类设为单例模式<br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：  <br/>
     */
    private StringUtil() {
    }

    /**
     * 函数功能说明 ：  判断字符串是否为空 <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param str
     * 参数：@return <br/>
     * return：boolean <br/>
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }

    /**
     * 函数功能说明 ：  判断对象数组是否为空<br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param obj
     * 参数：@return <br/>
     * return：boolean <br/>
     */
    public static boolean isEmpty(Object[] obj) {
        return null == obj || 0 == obj.length;
    }

    /**
     * 函数功能说明 ：判断对象是否为空 <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param obj
     * 参数：@return <br/>
     * return：boolean <br/>
     */
    public static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).trim().isEmpty();
        }
        return !(obj instanceof Number) ? false : false;
    }
    /**
     * 函数功能说明 ： 判断集合是否为空<br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param obj
     * 参数：@return <br/>
     * return：boolean <br/>
     */
    public static boolean isEmpty(List<?> obj) {
        return null == obj || obj.isEmpty();
    }


    /**
     * 函数功能说明 ： 判断Map集合是否为空 <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param obj
     * 参数：@return <br/>
     * return：boolean <br/>
     */
    public static boolean isEmpty(Map<?, ?> obj) {
        return null == obj || obj.isEmpty();
    }



    /**
     * 函数功能说明 ：获得文件名的后缀名 <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param fileName
     * 参数：@return <br/>
     * return：String <br/>
     */
    public static String getExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 函数功能说明 ： 获取去掉横线的长度为32的UUID串<br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@return <br/>
     * return：String <br/>
     */
    public static String get32UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 函数功能说明 ： 获取带横线的长度为36的UUID串<br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@return <br/>
     * return：String <br/>
     */
    public static String get36UUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 函数功能说明 ： 计算采用utf-8编码方式时字符串所占字节数<br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param content
     * 参数：@return <br/>
     * return：int <br/>
     */
    public static int getByteSize(String content) {
        int size = 0;
        if (null != content) {
            try {
                // 汉字采用utf-8编码时占3个字节
                size = content.getBytes("utf-8").length;
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage());
            }
        }
        return size;
    }

    /**
     * 函数功能说明 ： 截取字符串拼接in查询参数<br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param param
     * 参数：@return <br/>
     * return：List<String> <br/>
     */
    public static String getInParam(String param) {
        String inParem = "";
        if (!StringUtil.isEmpty(param)) {
            boolean flag = param.contains(",");
            if (flag) {
                List<String> list = Arrays.asList(param.split(","));
                for (int i = 0; i < list.size(); i++) {
                    inParem = inParem + "'" + list.get(i) + "',";
                }
                inParem = inParem.substring(0, inParem.length() - 1);
            } else {
                inParem = inParem + "'" + param + "'";
            }
        }
        return inParem.trim();
    }

    /**
     * 函数功能说明 ： 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址<br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param request
     * 参数：@return <br/>
     * return：String <br/>
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 去掉字符串首尾空格
     *
     * @return
     */
    public static String trim(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.trim();
    }

    public static void responsePdf(HttpServletResponse response, String pdfFilePath, String fileName) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.reset();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Content-Type", "application/pdf;charset=UTF-8");
        fileName = fileName + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN) + pdfFilePath.substring(pdfFilePath.lastIndexOf("."), pdfFilePath.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        FileInputStream in = new FileInputStream(pdfFilePath);
        OutputStream out = response.getOutputStream();
        byte[] b = new byte[1024];
        while ((in.read(b)) != -1) {
            out.write(b);
        }
        out.flush();
        in.close();
        out.close();
    }

    /**
     * 判断对象的某些属性是否为空（String 类型的属性需要添加@NotBlank注解,其他类型属性@NotNull）
     *
     * @param object
     * @return
     */
    public static String checkFieldIsNull(Object object) {
        try {
            List<Field> fields = getAllField(object);
            for (Field f : fields) {
                //私有属性
                f.setAccessible(true);
                Annotation anno;
                if (f.getType() == String.class) {
                    anno = f.getAnnotation(NotBlank.class);
                    if (anno != null && (f.get(object) == null || StringUtils.isBlank(f.get(object).toString()))) {
                        return f.getName();
                    }
                } else {
                    anno = f.getAnnotation(NotNull.class);
                    if (anno != null && f.get(object) == null) {
                        return f.getName();
                    }
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取对象的使用属性包括父类
     *
     * @param object
     * @return
     */
    public static List<Field> getAllField(Object object) {
        List<Field> fieldList = new LinkedList<>();
        if (object == null) {
            return new LinkedList<>();
        }
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Collections.addAll(fieldList, fields);
        // 处理父类字段
        Class<?> superClass = clazz.getSuperclass();
        if (superClass.equals(Object.class)) {
            return fieldList;
        }
        fields = superClass.getDeclaredFields();
        Collections.addAll(fieldList, fields);
        return fieldList;

    }
}
