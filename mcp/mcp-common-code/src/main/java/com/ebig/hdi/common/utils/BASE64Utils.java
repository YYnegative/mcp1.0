package com.ebig.hdi.common.utils;


import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @description:BASE64工具类
 * @author: wenchao
 * @time: 2019-10-15 14:29
 */
public class BASE64Utils {

    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private BASE64Utils(){}
    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String key) throws UnsupportedEncodingException {
        return new String(decoder.decode(key), "UTF-8");
    }

    /**
     * BASE64加密
     *
     * @param encodedText
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String encodedText) throws UnsupportedEncodingException {
        return encoder.encodeToString(encodedText.getBytes("UTF-8"));
    }
}
