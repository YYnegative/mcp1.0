package com.ebig.hdi.common.utils;

import java.util.Random;

/**
 * 类功能说明：生成随机数工具类<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 公司名称：广州市新橙信息科技有限公司 <br/>
 * 作者：luorx <br/>
 * 创建时间：2017年12月16日 下午2:11:43 <br/>
 * 版本：V1.0 <br/>
 */
public class RandomUtil {

    public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";   
    public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";   
    public static final String numberChar = "0123456789";   

    /**  
     * 返回一个定长的随机字符串(只包含大小写字母、数字)  
     *  
     * @param length 随机字符串长度  
     * @return 随机字符串  
     */   
    public static String generateString(int length) {   
        StringBuffer sb = new StringBuffer();   
        Random random = new Random();   
        for (int i = 0; i < length; i++) {   
            sb.append(allChar.charAt(random.nextInt(allChar.length())));   
        }   
        return sb.toString();   
    }   

    /**  
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)  
     *  
     * @param length 随机字符串长度  
     * @return 随机字符串  
     */   
    public static String generateMixString(int length) {   
        StringBuffer sb = new StringBuffer();   
        Random random = new Random();   
        for (int i = 0; i < length; i++) {   
            sb.append(allChar.charAt(random.nextInt(letterChar.length())));   
        }   
        return sb.toString();   
    }   

    /**  
     * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)  
     *  
     * @param length 随机字符串长度  
     * @return 随机字符串  
     */   
    public static String generateLowerString(int length) {   
        return generateMixString(length).toLowerCase();   
    }   

    /**  
     * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)  
     *  
     * @param length 随机字符串长度  
     * @return 随机字符串  
     */   
    public static String generateUpperString(int length) {   
        return generateMixString(length).toUpperCase();   
    }   
    
    /**
     * 函数功能说明 ：返回一个定长的纯数字字符串（只包含数字） <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：luorx <br/>
     * 参数：@param length
     * 参数：@return <br/>
     * return：String <br/>
     */
    public static String generateInt(int length) {
    	StringBuffer sb = new StringBuffer();   
        Random random = new Random();   
        for (int i = 0; i < length; i++) {   
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));   
        }   
        return sb.toString();
    }

    /**  
     * 生成一个定长的纯0字符串  
     *  
     * @param length 字符串长度  
     * @return 纯0字符串  
     */   
    public static String generateZeroString(int length) {   
        StringBuffer sb = new StringBuffer();   
        for (int i = 0; i < length; i++) {   
            sb.append('0');   
        }   
        return sb.toString();   
    }   

    /**  
     * 根据数字生成一个定长的字符串，长度不够前面补0  
     *  
     * @param num             数字  
     * @param fixdlenth 字符串长度  
     * @return 定长的字符串  
     */   
    public static String toFixdLengthString(long num, int fixdlenth) {   
        StringBuffer sb = new StringBuffer();   
        String strNum = String.valueOf(num);   
        if (fixdlenth - strNum.length() >= 0) {   
            sb.append(generateZeroString(fixdlenth - strNum.length()));   
        } else {   
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth + "的字符串发生异常！");   
        }   
        sb.append(strNum);   
        return sb.toString();   
    }   

    /**  
     * 根据数字生成一个定长的字符串，长度不够后面补0  
     *  
     * @param num             数字  
     * @param fixdlenth 字符串长度  
     * @return 定长的字符串  
     */   
    public static String toFixdLengthString(int num, int fixdlenth) {   
        StringBuffer sb = new StringBuffer();   
        StringBuffer zero = new StringBuffer();
        String strNum = String.valueOf(num); 
        if (fixdlenth - strNum.length() >= 0) { 
        	zero = new StringBuffer(generateZeroString(fixdlenth - strNum.length()));   
        } else {   
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth + "的字符串发生异常！");   
        }   
        sb.append(strNum);
        if(!StringUtil.isEmpty(zero)){
        	sb.append(zero);
        }
        return sb.toString();   
    }   

    /*public static void main(String[] args) {   
        System.out.println(generateString(15));   
        System.out.println(generateMixString(15));   
        System.out.println(generateLowerString(15));   
        System.out.println(generateUpperString(15)); 
        System.out.println(generateInt(6));
        System.out.println(generateZeroString(15));
        System.out.println(toFixdLengthString(123L, 15));
        System.out.println(toFixdLengthString(123, 15));   
    }*/   

}
