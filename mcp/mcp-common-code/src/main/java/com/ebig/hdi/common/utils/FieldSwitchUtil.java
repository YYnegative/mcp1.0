package com.ebig.hdi.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字段转换工具类
 */
public class FieldSwitchUtil {

    private FieldSwitchUtil(){}

	private static Pattern linePattern = Pattern.compile("_(\\w)");  
	private static Pattern humpPattern = Pattern.compile("[A-Z]");  
	
    /**下划线转驼峰*/  
    public static String lineToHump(String str){  
        str = str.toLowerCase();  
        Matcher matcher = linePattern.matcher(str);  
        StringBuffer sb = new StringBuffer();  
        while(matcher.find()){  
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());  
        }  
        matcher.appendTail(sb);  
        return sb.toString();  
    } 
    
    /**驼峰转下划线*/  
    public static String humpToLine(String str){  
        Matcher matcher = humpPattern.matcher(str);  
        StringBuffer sb = new StringBuffer();  
        while(matcher.find()){  
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());  
        }  
        matcher.appendTail(sb);  
        return sb.toString();  
    }  
    
    /** map key toggle Case **/
    public static List<Map<String, Object>> mapKeyToggleCase(List<Map<String, Object>> mapList, String flag){  
    	List<Map<String, Object>> listMap = new ArrayList<>();
		for(Map<String, Object> map : mapList){
			Map<String, Object> switchMap = new HashMap<>();
			for(Map.Entry<String, Object> entry : map.entrySet()){
				if("lower".equals(flag)){
					switchMap.put(lineToHump(entry.getKey().toLowerCase()), entry.getValue());
				}else if("upper".equals(flag)){
					switchMap.put(lineToHump(entry.getKey().toUpperCase()), entry.getValue());
				}
			}
			listMap.add(switchMap);
		}
		return listMap;
    }
    
}
