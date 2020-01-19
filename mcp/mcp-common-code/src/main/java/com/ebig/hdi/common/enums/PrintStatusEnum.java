package com.ebig.hdi.common.enums;

/**
 * 打印状态 0 未打印, 1 已打印
 */
public enum PrintStatusEnum {

    NOTPRINTED(0, "未打印"),
    PRINTED(1, "已打印");

    // 成员变量
    private Integer key;
    private String value;

    // 构造方法
    private PrintStatusEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getName(Integer key) {
        for (PrintStatusEnum c : PrintStatusEnum.values()) {
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
