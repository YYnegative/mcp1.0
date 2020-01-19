package com.ebig.hdi.common.enums;

/**
 * @title: ReturnEnum
 * @projectName mcp
 * @description: 返回码枚举类
 * @author：zhenghaiwen
 * @date：2019-11-26 11:16
 * @version：V1.0
 */
public enum ReturnCodeEnum {
    FAIL(0,"0"),
    SUCCESS(1, "1"),
    ERRORMESSAGE(2,"errorMessage"),
    ;

    /// 成员变量
    private Integer key;
    private String value;

    // 构造方法
    private ReturnCodeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getName(Integer key) {
        for (ReturnCodeEnum c : ReturnCodeEnum.values()) {
            if (c.getKey().equals(key)) {
                return c.value;
            }
        }
        return null;
    }

    public Integer getKey() {
        return key;
    }


    public String getValue() {
        return value;
    }
}
